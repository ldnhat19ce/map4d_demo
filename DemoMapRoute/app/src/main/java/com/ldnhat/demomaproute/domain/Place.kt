package com.ldnhat.demomaproute.domain

import com.google.gson.annotations.SerializedName

data class Place(

    @SerializedName("code")
    val code : String,

    @SerializedName("result")
    val result : MutableList<Result>? = null
)

data class Result(
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
