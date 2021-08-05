package com.ldnhat.demomaproute.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Direction(

    @SerializedName("code")
    val code : String,

    @SerializedName("result")
    val directionResult:DirectionResult
)


data class DirectionResult(
    @SerializedName("routes")
    val routes : MutableList<Routes>

)

data class Routes(
    @SerializedName("status")
    val status : String,

    @SerializedName("legs")
    val legs : MutableList<Legs>,

    @SerializedName("summary")
    val summary : String,

    @SerializedName("distance")
    val distance: Distance,

    @SerializedName("duration")
    val duration: Duration,

    @SerializedName("snappedWaypoints")
    val snappedWayPoints : MutableList<Location>
)

data class Legs(

    @SerializedName("distance")
    val distance: Distance,

    @SerializedName("duration")
    val duration: Duration,

    @SerializedName("endAddress")
    val endAddress : String,

    @SerializedName("startAddress")
    val startAddress : String,

    @SerializedName("endLocation")
    val endLocation: Location,

    @SerializedName("startLocation")
    val startLocation : Location,

    @SerializedName("steps")
    val steps : MutableList<Steps>

)

data class Steps(

    @SerializedName("distance")
    val distance: Distance,

    @SerializedName("duration")
    val duration: Duration,

    @SerializedName("endLocation")
    val endLocation: Location,

    @SerializedName("startLocation")
    val startLocation: Location,

    @SerializedName("travelMode")
    val travelMode : String,

    @SerializedName("streetName")
    val streetName : String
)

enum class DirectionVehicleFilter(val value : String){
    BIKE("bike"),
    CAR("car"),
    MOTORCYCLE("motorcycle"),
    FOOT("foot")
}
