package com.homeautomation.Activities.Responses

data class GetNetworks(
    val Device: String,
    val Available_Networks: List<String>,
    val Count: Int
)