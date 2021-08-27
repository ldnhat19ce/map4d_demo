package com.ldnhat.demomaproute.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.ldnhat.demomaproute.domain.*
import com.ldnhat.demomaproute.utils.Constant
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryName

interface MapService{

    @GET("autosuggest")
    fun findByTextAndLocationAsync(@Query("key") key : String, @Query("text") text : String
                                      ) : Deferred<Place>

    @GET("route")
    fun routeAsync(@Query("key") key: String,
                   @Query("origin") origin: String,
                   @Query("destination") destination: String,
                   @Query("mode") mode: String,
                   @Query("weight") weight : Int
              ) : Deferred<Direction>

    @GET("place/detail/{id}")
    fun findPlaceDetailByIdAsync(@Path("id") id : String, @Query("key") key: String)
    : Deferred<PlaceDetail>

    @GET("place/nearby-search")
    fun findPlaceNearAsync(@Query("key") key : String,
                           @Query("location") location : String,
                           @Query("radius") radius : String,
                           @Query("types")  types : String
                      ) : Deferred<PlaceNearBy>
}

object MapNetwork{

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constant.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val map = retrofit.create(MapService::class.java)
}