package com.ldnhat.demomaproute.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ldnhat.demomaproute.domain.Place
import com.ldnhat.demomaproute.network.MapNetwork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AutoSuggessViewModel : ViewModel() {

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

    init {
        _navigateToRoute.value = false
        _checkClick.value = false
    }

    fun updateFilter(input : String){
        getPropertiesDeferred(input)
    }

    private fun getPropertiesDeferred(text : String){
        coroutineScope.launch {
            val propertyDeferred = MapNetwork.map.findByTextAndLocationAsync("7e5d8bd61f83e15e80506dedf2fbe77f", text)
            try {
                _place.value = propertyDeferred.await()
            }catch (e : NetworkErrorException){

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
        println("fab clicked")
        _navigateToRoute.value = true
    }

    fun onNavigateToRoute(){
        _navigateToRoute.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}