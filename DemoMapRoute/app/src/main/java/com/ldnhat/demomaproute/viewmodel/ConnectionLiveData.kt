package com.ldnhat.demomaproute.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import javax.net.SocketFactory

class ConnectionLiveData(context : Context) : LiveData<Boolean>() {
    
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val connectionManager:ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val validNetworks:MutableSet<Network> = HashSet()

    private fun checkValidNetworks(){
        postValue(validNetworks.size > 0)
    }

    override fun onActive() {
        super.onActive()
        checkValidNetworks()
        networkCallback = createNetworkCallback()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        println("on active")
        connectionManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onInactive() {
        super.onInactive()
        connectionManager.unregisterNetworkCallback(networkCallback)
    }


    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback(){
        override fun onAvailable(network: Network) {
            println("on available")
            super.onAvailable(network)

            val networkCapability = connectionManager.getNetworkCapabilities(network)
            val hasInternetCapability = networkCapability?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

            if (hasInternetCapability == true){
                CoroutineScope(Dispatchers.IO).launch {
                    val hasInternet = DoesNetworkHaveInternet.execute(network.socketFactory)
                    if (hasInternet){
                        withContext(Dispatchers.Main){
                            validNetworks.add(network)
                            checkValidNetworks()
                        }
                    }
                }
            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            println("on lost")
            validNetworks.remove(network)
            checkValidNetworks()
        }
    }

}

object DoesNetworkHaveInternet{

    fun execute(socketFactory: SocketFactory) : Boolean{
        return try {
            val socket = socketFactory.createSocket()
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            true
        }catch (E : IOException){
            false
        }
    }
}
