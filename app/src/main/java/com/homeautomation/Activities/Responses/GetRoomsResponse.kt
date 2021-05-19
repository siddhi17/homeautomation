package com.homeautomation.Activities.Responses


import com.google.gson.annotations.SerializedName

data class GetRoomsResponse(
    @SerializedName("result")
    val result: String,
    @SerializedName("rooms")
    val rooms: List<Room>,
    @SerializedName("devices")
    val devices: List<Device>
) {
    data class Room(
            @SerializedName("_id")
            val id: String,
            @SerializedName("roomName")
            val roomName: String,
            @SerializedName("userId")
            val userId: String,
            @SerializedName("locationId")
            val locationId: String,
            val device: List<Device>
    )
    data class Device(
        val _id: String,
        val deviceId: String,
        val deviceName: String,
        val userId: String,
        val roomId: String,
        val locationId: String,
        var locationName: String,
        val switches: List<Switches>
    ) {
        data class Switches(
            val switch1: String?,
            val Type: String?,
            val switch2: String?
        )
    }
}