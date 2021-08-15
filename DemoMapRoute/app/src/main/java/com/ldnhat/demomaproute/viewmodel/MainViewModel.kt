package com.ldnhat.demomaproute.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ldnhat.demomaproute.domain.Place
import com.ldnhat.demomaproute.domain.PlaceDetail
import com.ldnhat.demomaproute.network.MapNetwork
import com.ldnhat.demomaproute.utils.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _place = MutableLiveData<Place>()

    val place:LiveData<Place>
    get() = _place

    private val _checkClick = MutableLiveData<Boolean>()

    val checkClick:LiveData<Boolean>
    get() = _checkClick

    private val _navigateToRoute = MutableLiveData<Boolean>()

    val navigateToRoute:LiveData<Boolean>
    get() = _navigateToRoute

    private val _bottomSheetState = MutableLiveData<Boolean>()

    val bottomSheetState:LiveData<Boolean>
    get() = _bottomSheetState

    private val _modeMap = MutableLiveData<Boolean>()

    val modelMap:LiveData<Boolean>
    get() = _modeMap

    private val _modelMapClick = MutableLiveData<Boolean>()

    val modelMapClick:LiveData<Boolean>
    get() = _modelMapClick

    private val _placeDetail = MutableLiveData<PlaceDetail>()

    val placeDetail:LiveData<PlaceDetail>
    get() = _placeDetail

    init {
        _navigateToRoute.value = false
        _checkClick.value = false
        _modeMap.value = false
        _modelMapClick.value = false
        _placeDetail.value = null
    }

    fun updateFilter(input : String){
        getPropertiesDeferred(input)
    }

    private fun getPropertiesDeferred(text : String){
        coroutineScope.launch {
            val propertyDeferred = MapNetwork.map.findByTextAndLocationAsync(Constant.KEY, text)
            try {
                _place.value = propertyDeferred.await()
            }catch (e : NetworkErrorException){

            }
        }
    }

    fun getPlaceDetail(id : String){
        _getPlaceDetail(id)
    }

    private fun _getPlaceDetail(id : String){
       coroutineScope.launch {
           val placeDetailDeferred = MapNetwork.map.findPlaceDetailByIdAsync(id, Constant.KEY)

           try {
               _placeDetail.value = placeDetailDeferred.await()
           }catch (e : NetworkErrorException){
                e.printStackTrace()
           }
       }
    }

    fun onClicked(){
        _checkClick.value = false
    }

    fun onClickToPosition(){
        _checkClick.value = true
    }

    fun onFabClicked(){
        _navigateToRoute.value = true
    }

    fun onNavigateToRoute(){
        _navigateToRoute.value = false
    }

    fun onBottomSheetExpanded(){
        _bottomSheetState.value = true
    }

    fun onBottomSheetCollapsed(){
        _bottomSheetState.value = false
    }

    fun onModeMapClick(){
        _modelMapClick.value = true
    }

    fun onModeMapClicked(){
        _modelMapClick.value = false
    }

    fun onModeMapIs2D(){
        _modeMap.value = false
    }

    fun onModeMapIs3D(){
        _modeMap.value = true
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}