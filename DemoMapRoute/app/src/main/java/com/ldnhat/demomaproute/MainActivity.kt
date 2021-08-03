package com.ldnhat.demomaproute

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ldnhat.demomaproute.adapter.ClickListener
import com.ldnhat.demomaproute.adapter.SearchPlaceAdapter
import com.ldnhat.demomaproute.databinding.ActivityMainBinding
import com.ldnhat.demomaproute.viewmodel.AutoSuggessViewModel
import vn.map4d.map.camera.MFCameraUpdateFactory
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

        val adapter = SearchPlaceAdapter(ClickListener{
            //Toast.makeText(this, "lat : ${it.lat}, lng: ${it.lng}", Toast.LENGTH_SHORT).show()
            map4D?.animateCamera(MFCameraUpdateFactory.newCoordinateBounds(
                MFCoordinateBounds(
                    MFLocationCoordinate(it.lat, it.lng),
                    MFLocationCoordinate(it.lat + 0.001, it.lng + 0.001)
                ), 0
            ))
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
    }

    override fun onMapReady(map4D: Map4D) {
        this.map4D = map4D
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.mapview?.onDestroy()
    }
}