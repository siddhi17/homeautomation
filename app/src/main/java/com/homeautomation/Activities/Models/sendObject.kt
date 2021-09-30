package com.homeautomation.Activities.Models

import java.sql.Timestamp

data class sendObject(
    val operation: String,
    val channelId: String,
    val value: String,
    val timestamp_epoch: Long
)

data class AllSwitchesObject(
    val channelId: String,
    val value: String,
    val timestamp_epoch: Long
)