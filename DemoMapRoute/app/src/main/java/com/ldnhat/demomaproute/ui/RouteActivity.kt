package com.ldnhat.demomaproute.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ldnhat.demomaproute.R
import com.ldnhat.demomaproute.databinding.ActivityRouteBinding
import com.ldnhat.demomaproute.viewmodel.RouteViewModel
import vn.map4d.map.annotations.MFMarker
import vn.map4d.map.annotations.MFMarkerOptions
import vn.map4d.map.annotations.MFPolyline
import vn.map4d.map.annotations.MFPolylineOptions
import vn.map4d.map.camera.MFCameraUpdateFactory
import vn.map4d.map.core.Map4D
import vn.map4d.map.core.OnMapReadyCallback
import vn.map4d.types.MFLocationCoordinate

class RouteActivity : AppCompatActivity(), OnMapReadyCallback {

    private var binding : ActivityRouteBinding? = null

    private val routeViewModel by lazy {
        ViewModelProvider(this).get(RouteViewModel::class.java)
    }

    private var map4D:Map4D? = null
    private var marker:MFMarker? = null
    private var polyline:MFPolyline? = null
    private var markerStartPosition:MFMarker? = null

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
                marker?.remove()
                polyline?.remove()
                routeViewModel.getLocation(binding?.startLocation?.text.toString(), binding?.endLocation?.text.toString(), "bike")
                routeViewModel.onFindPathSuccess()
            }
        })

        routeViewModel.listStepLocation.observe(this, {
            routeViewModel.printList()
            val latLngList = it

            polyline = map4D?.addPolyline(
                MFPolylineOptions().add(*latLngList.toTypedArray())
                    .color(Color.RED)
                    .width(4.0f))

            marker = map4D?.addMarker(MFMarkerOptions()
                .position(MFLocationCoordinate(latLngList[latLngList.size - 1].latitude,
                    latLngList[latLngList.size - 1].longitude))
                .title("demo")
                .snippet("sdsdssdsdsdsds")
            )
            map4D?.moveCamera(MFCameraUpdateFactory.newCoordinate(MFLocationCoordinate(latLngList[latLngList.size - 1].latitude,
                latLngList[latLngList.size - 1].longitude)))
        })

        routeViewModel.startLocationClick.observe(this, {
            if (it){
                val centerOfMap: MFLocationCoordinate? = map4D?.cameraPosition?.target
                Toast.makeText(this, "center: "+centerOfMap?.latitude+" lng: "+centerOfMap?.longitude, Toast.LENGTH_SHORT)
                    .show()
                marker = map4D?.addMarker(MFMarkerOptions()
                    .position(centerOfMap!!)
                    .draggable(true)
                )
                routeViewModel.onStartLocationClickSuccess()
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
}