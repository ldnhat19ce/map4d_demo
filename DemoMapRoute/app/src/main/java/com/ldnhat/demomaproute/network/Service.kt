package com.ldnhat.demomaproute.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.ldnhat.demomaproute.domain.Direction
import com.ldnhat.demomaproute.domain.Place
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MapService{

    @GET("autosuggest")
    fun findByTextAndLocationAsync(@Query("key") key : String, @Query("text") text : String
                                      ) : Deferred<Place>

    @GET("route")
    fun routeAsync(@Query("key") key : String,
                   @Query("origin") origin : String,
                   @Query("destination") destination : String,
                   @Query("mode") mode : String
              ) : Deferred<Direction>
}

object MapNetwork{

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.map4d.vn/sdk/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val map = retrofit.create(MapService::class.java)
}