package com.ldnhat.demomaproute.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ldnhat.demomaproute.R
import com.ldnhat.demomaproute.databinding.ActivityRouteBinding
import com.ldnhat.demomaproute.viewmodel.RouteViewModel
import vn.map4d.map.core.Map4D
import vn.map4d.map.core.OnMapReadyCallback

class RouteActivity : AppCompatActivity(), OnMapReadyCallback {

    private var binding : ActivityRouteBinding? = null

    private val routeViewModel by lazy {
        ViewModelProvider(this).get(RouteViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_route)

        binding?.viewModel = routeViewModel

        routeViewModel.backClick.observe(this, {
            if (it){
                routeViewModel.onBackCliked()
                onBackPressed()
            }
        })
    }

    override fun onMapReady(map4D: Map4D) {

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