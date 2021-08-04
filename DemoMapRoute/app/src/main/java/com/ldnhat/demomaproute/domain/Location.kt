package com.ldnhat.demomaproute.domain

import com.google.gson.annotations.SerializedName

data class Location(

    @SerializedName("lat")
    val lat : Double,

    @SerializedName("lng")
    val lng : Double
)