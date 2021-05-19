package com.homeautomation.Activities.Responses
import com.google.gson.annotations.SerializedName


data class RegisterResponse(
    @SerializedName("id")
    val id: String
)