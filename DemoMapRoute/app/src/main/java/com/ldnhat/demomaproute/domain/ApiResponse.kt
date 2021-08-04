package com.ldnhat.demomaproute.domain

import com.google.gson.annotations.SerializedName

class ApiResponse<T> {

    @SerializedName("code")
    val code:String? = null

    @SerializedName("message")
    val message:String? = null

    @SerializedName("result")
    val result:T? = null
}