package com.ldnhat.demomaproute.domain

import com.google.gson.annotations.SerializedName

data class Duration(

    @SerializedName("text")
    val text : String,

    @SerializedName("value")
    val value : Double
)