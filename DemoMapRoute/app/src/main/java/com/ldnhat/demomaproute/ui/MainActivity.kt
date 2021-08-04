package com.ldnhat.demomaproute.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ldnhat.demomaproute.R
import com.ldnhat.demomaproute.adapter.ClickListener
import com.ldnhat.demomaproute.adapter.SearchPlaceAdapter
import com.ldnhat.demomaproute.databinding.ActivityMainBinding
import com.ldnhat.demomaproute.viewmodel.AutoSuggessViewModel
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import vn.map4d.map.annotations.MFMarker
import vn.map4d.map.annotations.MFMarkerOptions
import vn.map4d.map.camera.MFCameraPosition
import vn.map4d.map.camera.MFCameraUpdateFactory
import vn.map4d.map.core.LatLng
import vn.map4d.map.core.MFCoordinateBounds
import vn.map4d.map.core.Map4D
import vn.map4d.map.core.OnMapReadyCallback
import vn.map4d.types.MFLocationCoordinate

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private var binding:ActivityMainBinding? = null
    private var map4D:Map4D? = null

    private val viewModel by lazy {
        ViewModelProvider(this).get(AutoSuggessViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding?.mapview?.getMapAsync(this)
        binding?.viewModel = viewModel

        val adapter = SearchPlaceAdapter(ClickListener{
            //Toast.makeText(this, "lat : ${it.lat}, lng: ${it.lng}", Toast.LENGTH_SHORT).show()
//            map4D?.animateCamera(MFCameraUpdateFactory.newCoordinateBounds(
//                MFCoordinateBounds(
//                    MFLocationCoordinate(it.lat, it.lng),
//                    MFLocationCoordinate(it.lat, it.lng)
//                ), 5
//            ))

            map4D?.animateCamera(MFCameraUpdateFactory.newLatLng(MFLocationCoordinate(it.lat, it.lng)))

            val markerOptions = MFMarkerOptions()
            markerOptions.position(MFLocationCoordinate(it.lat, it.lng))
            map4D?.addMarker(markerOptions)

            viewModel.onClickToPosition()
        })
        binding?.rvsearch?.adapter = adapter

        viewModel.place.observe(this, {
            it?.let {
                adapter.submitList(it.result)
            }
        })

        binding?.etInformation?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.updateFilter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        viewModel.checkClick.observe(this, {
            if (it){
                binding?.etInformation?.setText("")
                viewModel.onClicked()
            }
        })

        viewModel.navigateToRoute.observe(this, {
            if (it){
                viewModel.onNavigateToRoute()
                val intent = Intent(this, RouteActivity::class.java)
                startActivity(intent)

            }
        })

        binding?.lifecycleOwner = this
    }

    override fun onMapReady(map4D: Map4D) {
        this.map4D = map4D
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        map4D.isMyLocationEnabled = true
        map4D.uiSettings.isMyLocationButtonEnabled = true
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.mapview?.onDestroy()
    }
}