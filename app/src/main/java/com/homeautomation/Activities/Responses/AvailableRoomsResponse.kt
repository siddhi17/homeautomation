package com.homeautomation.Activities.Responses


import com.google.gson.annotations.SerializedName

data class AvailableRoomsResponse(
    @SerializedName("result")
    val result: String,
    @SerializedName("rooms")
    val rooms: List<Room>
) {
    data class Room(
        @SerializedName("_id")
        val id: String,
        @SerializedName("roomName")
        val roomName: String,
        @SerializedName("userId")
        val userId: String,
        @SerializedName("locationId")
        val locationId: String
    )
}