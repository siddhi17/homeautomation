package com.homeautomation.Activities.Models


import com.google.gson.annotations.SerializedName

data class AddLocation(
    @SerializedName("locationName")
    val locationName: String,
    @SerializedName("userId")
    val userId: String
)