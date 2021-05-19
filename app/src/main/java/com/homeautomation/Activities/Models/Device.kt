package com.homeautomation.Activities.Models
import com.google.gson.annotations.SerializedName


data class Device(
    @SerializedName("_id")
    val id: String,
    @SerializedName("deviceId")
    val deviceId: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("roomId")
    val roomId: String,
    @SerializedName("locationId")
    val locationId: String,
    @SerializedName("switches")
    val switches: List<Switches>
) {
    data class Switches(
        @SerializedName("switch1")
        val switch1: String,
        @SerializedName("switch2")
        val switch2: String
    )
}