package com.ldnhat.demomaproute.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ldnhat.demomaproute.domain.Direction
import com.ldnhat.demomaproute.network.MapNetwork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.map4d.map.annotations.MFPolyline
import vn.map4d.types.MFLocationCoordinate
import java.util.ArrayList

class RouteViewModel : ViewModel() {

    private val viewModelJob = Job()

    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _backClick = MutableLiveData<Boolean>()

    val backClick:LiveData<Boolean>
    get() = _backClick

    private val _direction = MutableLiveData<Direction>()

    val direction:LiveData<Direction>
    get() = _direction

    private val _listStepLocation = MutableLiveData<MutableList<MFLocationCoordinate>>()

    val listStepLocation:LiveData<MutableList<MFLocationCoordinate>>
    get() = _listStepLocation

    private val _findPathClick = MutableLiveData<Boolean>()

    val findPathClick:LiveData<Boolean>
    get() = _findPathClick

    private val _startLocationClick = MutableLiveData<Boolean>()

    val startLocationClick:LiveData<Boolean>
    get() = _startLocationClick

    private val _endLocationClick = MutableLiveData<Boolean>()

    val endLocationClick:LiveData<Boolean>
        get() = _endLocationClick

    private val _isStartLocation = MutableLiveData<Boolean>()

    val isStartLocation:LiveData<Boolean>
    get() = _isStartLocation

    private val _clickChoosePosition = MutableLiveData<Boolean>()

    val clickChoosePosition:LiveData<Boolean>
    get() = _clickChoosePosition

    private val _markerChooseStartPosition = MutableLiveData<MFLocationCoordinate>()

    val markerChooseStartPosition:LiveData<MFLocationCoordinate>
    get() = _markerChooseStartPosition

    private val _markerChooseEndPosition = MutableLiveData<MFLocationCoordinate>()

    val markerChooseEndPosition:LiveData<MFLocationCoordinate>
    get() = _markerChooseEndPosition

    init {
        _startLocationClick.value = false
    }

    fun setMarkerStartPosition(mfLocationCoordinate: MFLocationCoordinate){
        _markerChooseStartPosition.value = mfLocationCoordinate
    }

    fun setMarkerEndPosition(mfLocationCoordinate: MFLocationCoordinate){
        _markerChooseEndPosition.value = mfLocationCoordinate
    }

    fun onClickChoosePosition(){
        _clickChoosePosition.value = true
    }

    fun onClickChoosePositionSuccess(){
        _clickChoosePosition.value = false
    }

    fun onStartLocationClick(){
        _isStartLocation.value = true
        _startLocationClick.value = true
    }

    fun onStartLocationClickSuccess(){
        _startLocationClick.value = false
    }

    fun onEndLocationClick(){
        _isStartLocation.value = false
        _endLocationClick.value = true
    }

    fun onEndLocationClickSuccess(){
        _endLocationClick.value = false
    }


    fun onBackClick(){
        _backClick.value = true
    }

    fun onBackCliked(){
        _backClick.value = false
    }

    fun onFindPathClick(){
        _findPathClick.value = true
    }

    fun onFindPathSuccess(){
        _findPathClick.value = false
    }

    private fun getRouteDeferred(origin: String, destination: String, mode: String) {
        coroutineScope.launch {
            val directionDeferred = MapNetwork.map.routeAsync("7e5d8bd61f83e15e80506dedf2fbe77f",
                origin, destination, mode, 1
            )
            try {
               _direction.value = directionDeferred.await()
                drawPolyline()
            }catch (e  : Exception){
                e.printStackTrace()
            }
        }

    }

    fun getLocation(origin: String, destination: String, mode: String){
        getRouteDeferred(origin, destination, mode)
    }

    fun printList(){
        listStepLocation.value?.forEach {
            println("lat  "+it.latitude + "lng : "+it.longitude)
        }
    }

    private fun drawPolyline(){
        val listMF = ArrayList<MFLocationCoordinate>()
        direction.value?.directionResult?.routes?.forEach {
            it.legs.forEach {
                it.steps.forEach {
                    //println(it.startLocation.lat)
                    val mfLocation = MFLocationCoordinate(it.startLocation.lat, it.startLocation.lng)
                    val mf = MFLocationCoordinate(it.endLocation.lat, it.endLocation.lng)
                    listMF.add(mfLocation)
                    listMF.add(mf)

                }
            }
        }
        _listStepLocation.value = listMF
    }
}