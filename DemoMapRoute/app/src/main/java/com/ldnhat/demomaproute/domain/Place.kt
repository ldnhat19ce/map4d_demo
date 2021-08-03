package com.ldnhat.demomaproute.domain

data class Place(
    val code : String,
    val result : MutableList<Result>? = null
)

data class Result(
    val id : String,
    val name : String,
    val address : String,
    val location: Location,
    val types : MutableList<String>
)

data class Location(
    val lat : Double,
    val lng : Double
)