package com.ldnhat.demomaproute.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ldnhat.demomaproute.R
import com.ldnhat.demomaproute.databinding.ActivityRouteBinding
import com.ldnhat.demomaproute.domain.DirectionVehicleFilter
import com.ldnhat.demomaproute.viewmodel.RouteViewModel
import vn.map4d.map.annotations.*
import vn.map4d.map.camera.MFCameraUpdateFactory
import vn.map4d.map.core.Map4D
import vn.map4d.map.core.OnMapReadyCallback
import vn.map4d.types.MFLocationCoordinate

class RouteActivity : AppCompatActivity(), OnMapReadyCallback, PopupMenu.OnMenuItemClickListener {

    private var binding : ActivityRouteBinding? = null

    private val routeViewModel by lazy {
        ViewModelProvider(this).get(RouteViewModel::class.java)
    }

    private var map4D:Map4D? = null
    private var marker:MFMarker? = null
    private var polyline:MFPolyline? = null
    private var markerStartPosition:MFMarker? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_route)

        binding?.lifecycleOwner = this
        binding?.viewModel = routeViewModel
        binding?.mapRoute?.getMapAsync(this)

        routeViewModel.backClick.observe(this, {
            if (it){
                routeViewModel.onBackCliked()
                onBackPressed()
            }
        })

        routeViewModel.findPathClick.observe(this, {
            if (it){
                if (binding?.startLocation?.text?.toString()?.trim()?.isBlank() == false &&
                    binding?.endLocation?.text?.toString()?.trim()?.isBlank() == false
                ){
                    marker?.remove()
                    polyline?.remove()
                    routeViewModel.getLocation(binding?.startLocation?.text.toString(), binding?.endLocation?.text.toString(),
                        DirectionVehicleFilter.BIKE.value)
                    routeViewModel.onFindPathSuccess()
                }else{
                    routeViewModel.onFindPathSuccess()
                    Toast.makeText(this, "??i???n ?????y ?????", Toast.LENGTH_SHORT).show()
                }
            }
        })

        routeViewModel.listStepLocation.observe(this, {

            polyline = map4D?.addPolyline(
                MFPolylineOptions().add(*it.toTypedArray())
                    .color(Color.RED)
                    .width(4.0f))

            marker = map4D?.addMarker(MFMarkerOptions()
                .position(MFLocationCoordinate(it[it.size - 1].latitude,
                    it[it.size - 1].longitude))
                .title("demo")
            )
            map4D?.moveCamera(MFCameraUpdateFactory.newCoordinate(MFLocationCoordinate(it[it.size - 1].latitude,
                it[it.size - 1].longitude)))
        })

        routeViewModel.startLocationClick.observe(this, {
            if (it){
                val centerOfMap: MFLocationCoordinate? = map4D?.cameraPosition?.target
//                Toast.makeText(this, "center: "+centerOfMap?.latitude+" lng: "+centerOfMap?.longitude, Toast.LENGTH_SHORT)
//                    .show()
                markerStartPosition = map4D?.addMarker(MFMarkerOptions()
                    .position(centerOfMap!!)
                    .draggable(true)
                    .icon(MFBitmapDescriptorFactory.fromResource(R.drawable.location_64px))
                )
                binding?.btnSubmitPosition?.visibility = View.VISIBLE
                binding?.textWayDragg?.visibility = View.VISIBLE
                routeViewModel.onStartLocationClickSuccess()
            }
        })

        routeViewModel.endLocationClick.observe(this, {
            if (it){
                val centerOfMap: MFLocationCoordinate? = map4D?.cameraPosition?.target
                markerStartPosition = map4D?.addMarker(MFMarkerOptions()
                    .position(centerOfMap!!)
                    .draggable(true)
                    .icon(MFBitmapDescriptorFactory.fromResource(R.drawable.location_64px))
                )
                binding?.btnSubmitPosition?.visibility = View.VISIBLE
                binding?.textWayDragg?.visibility = View.VISIBLE
                routeViewModel.onEndLocationClickSuccess()
            }
        })


        routeViewModel.clickChoosePosition.observe(this, {
            if (it){
                if (routeViewModel.isStartLocation.value == true){
                    routeViewModel.setMarkerStartPosition(MFLocationCoordinate(markerStartPosition?.position!!.latitude,
                        markerStartPosition?.position!!.longitude
                        ))
                } else{
                    routeViewModel.setMarkerEndPosition(MFLocationCoordinate(markerStartPosition?.position!!.latitude,
                        markerStartPosition?.position!!.longitude))
                }
                markerStartPosition?.remove()
                binding?.btnSubmitPosition?.visibility = View.GONE
                binding?.textWayDragg?.visibility = View.GONE
                routeViewModel.onClickChoosePositionSuccess()
            }
        })

        routeViewModel.clickShowPopup.observe(this, {
            if (it){
                val popupMenu:PopupMenu = PopupMenu(this, binding?.showPopupMenu)
                popupMenu.inflate(R.menu.option_menu)
                popupMenu.setOnMenuItemClickListener(this)
                popupMenu.show()
                routeViewModel.onClickShowPopupSuccess()
            }
        })
//        routeViewModel.getLocation("10.763144874399996, 106.68213951019406",
//            "10.775461025160295, 106.69533960130343", "bike")

    }

    override fun onMapReady(map4D: Map4D) {
        this.map4D = map4D
    }


    override fun onBackPressed() {
        super.onBackPressed()
        shutdown(0, Intent())
    }

    private fun shutdown(i : Int, intent : Intent){
        setResult(i, intent)
        finish()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        marker?.remove()
        polyline?.remove()
        routeViewModel.getLocation(
            "${routeViewModel.markerChooseStartPosition.value?.latitude}," +
                    " ${routeViewModel.markerChooseStartPosition.value?.longitude}",
            "${routeViewModel.markerChooseEndPosition.value?.latitude}," +
                    " ${routeViewModel.markerChooseEndPosition.value?.longitude}",
            when(item?.itemId){
                R.id.bike -> DirectionVehicleFilter.BIKE.value
                R.id.car -> DirectionVehicleFilter.CAR.value
                R.id.foot -> DirectionVehicleFilter.FOOT.value
                else -> DirectionVehicleFilter.MOTORCYCLE.value
            }
        )

        return true
    }
}