package com.homeautomation.Activities.Responses


import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

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
            val device: List<Device>,
            var isSelected: Boolean,
            var img: Int
    )
    data class Device(
            val _id: String,
            val deviceId: String,
            val deviceName: String,
            val userId: String,
            val roomId: String,
            val locationId: String,
            var locationName: String,
            var isLocationFailed: Boolean = false,
            var roomName: String,
            var status: String = "Offline",
            val switches: ArrayList<Switches>,
            var isOnline: Boolean? = false,
            var timestamp: Long = 0,
            var value: Int?
    ) {

        data class Switches(
                val name: String?,
                val type: String?,
                val id: String?,
                var value: Int?
        )
    }
}