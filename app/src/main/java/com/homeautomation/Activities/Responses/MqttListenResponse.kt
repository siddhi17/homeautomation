package com.homeautomation.Activities.Responses

data class MqttListenResponse(
    val timestamp: String,
    val timestamp_epoch: Long,
    val ChannelId_1: Int,
    val ChannelId_2: Int,
    val ChannelId_3: Int,
    val ChannelId_4: Int,
    val ChannelId_5: Int
)