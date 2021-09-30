package com.homeautomation.Activities.Responses

import com.homeautomation.Activities.Models.MqttResponse

data class publishResponse(var deviceId: String, var mqttPublishResponse: MqttPublishResponse)
