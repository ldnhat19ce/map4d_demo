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

    init {
        _checkClick.value = false
    }

    fun updateFilter(input : String){
        getPropertiesDeferred(input)
    }

    fun getPropertiesDeferred(text : String){
        coroutineScope.launch {
            val propertyDeferred = MapNetwork.map.findByTextAndLocation("7e5d8bd61f83e15e80506dedf2fbe77f", text)
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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}