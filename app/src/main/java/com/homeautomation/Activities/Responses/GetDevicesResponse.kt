package com.homeautomation.Activities.Responses

data class GetDevicesResponse(
    val result: String,
    val devices: List<Device>
) {
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
                val name: String?,
                val type: String?,
                val id: String?,
                val value: Int?
        )
    }
}