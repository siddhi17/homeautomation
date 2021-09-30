package com.homeautomation.Activities.Models

data class MqttResponse(
    val DeviceId: String,
    val timestamp_epoch: Long,
    val ChannelId_1: Int?,
    val ChannelId_2: Int?,
    val ChannelId_3: Int?,
    val ChannelId_4: Int?,
    val ChannelId_5: Int?,
    val ChannelId_6: Int?,
    val ChannelId_7: Int?,
    val ChannelId_8: Int?,
    val ChannelId_9: Int?,
    val ChannelId_10: Int?,
    val ChannelId_11: Int?,
    val ChannelId_12: Int?
)