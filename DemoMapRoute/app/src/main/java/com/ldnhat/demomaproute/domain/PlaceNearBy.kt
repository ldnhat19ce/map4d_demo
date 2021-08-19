package com.ldnhat.demomaproute.domain

import com.google.gson.annotations.SerializedName

data class PlaceNearBy(
    @SerializedName("code")
    val code : String,

    @SerializedName("result")
    val placeNearByResult: MutableList<PlaceNearByResult>
)

data class PlaceNearByResult(

    @SerializedName("id")
    val id : String,

    @SerializedName("name")
    val name : String,

    @SerializedName("address")
    val address : String,

    @SerializedName("location")
    val location: Location,

    @SerializedName("types")
    val types : MutableList<String>
)