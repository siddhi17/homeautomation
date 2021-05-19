package com.homeautomation.Activities.Responses


import com.google.gson.annotations.SerializedName

data class AddLocationResponse(
    @SerializedName("locationId")
    val locationId: String,
    @SerializedName("error")
    val error: String
)