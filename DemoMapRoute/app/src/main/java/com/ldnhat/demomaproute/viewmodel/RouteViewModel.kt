package com.ldnhat.demomaproute.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ldnhat.demomaproute.domain.Direction
import com.ldnhat.demomaproute.network.MapNetwork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RouteViewModel : ViewModel() {

    private val viewModelJob = Job()

    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _backClick = MutableLiveData<Boolean>()

    val backClick:LiveData<Boolean>
    get() = _backClick

    private val _direction = MutableLiveData<Direction>()

    val direction:LiveData<Direction>
    get() = _direction

    fun onBackClick(){
        _backClick.value = true
    }

    fun onBackCliked(){
        _backClick.value = false
    }

    private fun getRouteDeferred(origin : String, destination : String, mode : String) {
        coroutineScope.launch {
            val directionDeferred = MapNetwork.map.routeAsync("7e5d8bd61f83e15e80506dedf2fbe77f",
                origin, destination, mode
            )
            try {
               _direction.value = directionDeferred.await()
            }catch (e  : NetworkErrorException){

            }
        }
    }

    fun getLocation(origin : String, destination : String, mode : String){
        getRouteDeferred(origin, destination, mode)
    }
}