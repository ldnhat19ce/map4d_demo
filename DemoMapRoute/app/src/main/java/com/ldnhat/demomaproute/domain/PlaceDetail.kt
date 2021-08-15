package com.ldnhat.demomaproute.domain

import com.google.gson.annotations.SerializedName

data class PlaceDetail(

    @SerializedName("code")
    val code : String,

    @SerializedName("result")
    val result : PlaceDetailResult
)

data class PlaceDetailResult(

    @SerializedName("description")
    val description : String,

    @SerializedName("tags")
    val tags : MutableList<String>,

    @SerializedName("metadata")
    val metaData: MutableList<MetaData>,

    @SerializedName("photos")
    val photos: MutableList<Photos>,

    @SerializedName("addressComponents")
    val addressComponents: MutableList<AddressComponents>,

    @SerializedName("objectId")
    val objectId : String,

    @SerializedName("id")
    val id : String,

    @SerializedName("location")
    val location: Location,

    @SerializedName("address")
    val address : String,

    @SerializedName("name")
    val name : String,

    @SerializedName("types")
    val types : MutableList<String>
)

data class MetaData(

    @SerializedName("id")
    val id : String,

    @SerializedName("type")
    val type : String,

    @SerializedName("name")
    val name : String,

    @SerializedName("content")
    val content : String,

    @SerializedName("order")
    val order : Double
)

data class Photos(

    @SerializedName("url")
    val url : String,

    @SerializedName("name")
    val name : String
)

data class AddressComponents(

    @SerializedName("code")
    val code : String,

    @SerializedName("type")
    val type : String,

    @SerializedName("name")
    val name : String
)



