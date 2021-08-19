package com.ldnhat.demomaproute.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.ldnhat.demomaproute.R
import com.ldnhat.demomaproute.adapter.ClickListener
import com.ldnhat.demomaproute.adapter.SearchPlaceAdapter
import com.ldnhat.demomaproute.databinding.ActivityMainBinding
import com.ldnhat.demomaproute.domain.ChipCategory
import com.ldnhat.demomaproute.utils.AlertUtils
import com.ldnhat.demomaproute.viewmodel.ConnectionLiveData
import com.ldnhat.demomaproute.viewmodel.MainViewModel
import vn.map4d.map.annotations.MFMarkerOptions
import vn.map4d.map.camera.MFCameraUpdateFactory
import vn.map4d.map.core.Map4D
import vn.map4d.map.core.OnMapReadyCallback
import vn.map4d.types.MFLocationCoordinate
import java.util.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback,
    Map4D.OnMyLocationButtonClickListener
{

    private var binding:ActivityMainBinding? = null
    private var map4D:Map4D? = null
    private var locationPermissionGranted = false

    private val connectionLiveData:LiveData<Boolean> by lazy {
        ConnectionLiveData(this)
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        enableMyLocation()

        binding?.mapview?.getMapAsync(this)
        binding?.viewModel = viewModel

        val bottomSheetBehavior = BottomSheetBehavior.from(binding?.bottomSheet?.bottomSheet as View)
        binding?.bottomSheet?.viewModel = viewModel
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        viewModel.bottomSheetState.observe(this, {
            if (it){

            }
        })

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

        connectionLiveData.observe(this, {
            if (!it){
               //Toast.makeText(this, "No network connection", Toast.LENGTH_SHORT).show()
                AlertUtils.showAlertDialog(this, R.string.alert_title, R.string.network_not_connected)
            }
        })

        val adapter = SearchPlaceAdapter(ClickListener{
            map4D?.animateCamera(MFCameraUpdateFactory.newCoordinate(MFLocationCoordinate(it.lat, it.lng)))

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
                if (connectionLiveData.value == false){
                    Toast.makeText(this@MainActivity, "không tìm thấy kết quả!", Toast.LENGTH_SHORT).show()
                }else{
                    viewModel.updateFilter(s.toString())
                }
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

        viewModel.modelMap.observe(this, {
            if (it){
                map4D?.enable3DMode(true)
            }else{
                map4D?.enable3DMode(false)
            }
        })

        viewModel.modelMapClick.observe(this, {
            if (it){
                if (map4D?.is3DMode == true){
                    viewModel.onModeMapIs2D()
                }else{
                    viewModel.onModeMapIs3D()
                    map4D?.setOnBuildingClickListener { buildingId, name, location ->
                        map4D?.setSelectedBuildings(listOf(buildingId))
                        Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
                    }
                }
                viewModel.onModeMapClicked()
            }
        })

        viewModel.placeDetail.observe(this, {
            if (it != null){
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }else{
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        })

        val chipGroup = binding?.category

        val inflator = LayoutInflater.from(chipGroup?.context)

        val chip :Chip  = inflator.inflate(R.layout.chip_category, chipGroup, false) as Chip
        chip.text = ChipCategory.ATM.value
        chip.tag = ChipCategory.ATM.value

        chipGroup?.removeAllViews()
        chipGroup?.addView(chip)

        binding?.lifecycleOwner = this

    }

    override fun onMapReady(map4D: Map4D) {
        this.map4D = map4D
//        map4D.setOnMapClickListener {
//            Toast.makeText(this, it.latitude.toString(), Toast.LENGTH_SHORT).show()
//        }

        map4D.setOnPOIClickListener { placeId, title, mfLocationCoordinate ->
            println(placeId)
            viewModel.getPlaceDetail(placeId)
        }

        updateLocationUI()


    }

    private fun enableMyLocation(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            if (ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
                locationPermissionGranted = true
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionGranted = false
        when(requestCode){
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    locationPermissionGranted = true
                }else{
                    locationPermissionGranted = false
                    enableMyLocation()
                }
            }
        }
        updateLocationUI()
    }

    private fun updateLocationUI(){
        try {
            if (locationPermissionGranted){
                map4D?.isMyLocationEnabled = true
                map4D?.uiSettings?.isMyLocationButtonEnabled = true

            }else{
                map4D?.isMyLocationEnabled = false
                map4D?.uiSettings?.isMyLocationButtonEnabled = false
            }
        }catch (e : SecurityException){
            println(e.message)
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        startActivity(Intent("android.settings.LOCATION_SOURCE_SETTINGS"))
        return true
    }


    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 99
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.mapview?.onDestroy()
    }


}