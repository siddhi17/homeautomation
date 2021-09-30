package com.homeautomation.Activities.Models
import com.google.gson.annotations.SerializedName


data class Device(
    @SerializedName("_id")
    val id: String,
    @SerializedName("deviceId")
    val deviceId: String,
    @SerializedName("deviceName")
    val deviceName: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("roomId")
    val roomId: String,
    @SerializedName("locationId")
    val locationId: String,
    @SerializedName("switches")
    val switches: List<Switche>
) {
    data class Switche(
            val name: String,
            val type: String,
            val id: String
    )
}

data class AddDevice(
        @SerializedName("deviceId")
        val deviceId: String,
        @SerializedName("deviceName")
        val deviceName: String,
        @SerializedName("userId")
        val userId: String,
        @SerializedName("roomId")
        val roomId: String,
        @SerializedName("locationId")
        val locationId: String,
        @SerializedName("switches")
        val switches: List<AddDevice.Switche>
) {
    data class Switche(
            val name: String,
            val type: String,
            val id: String
    )
}