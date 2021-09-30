package com.homeautomation.Activities.Responses

import com.homeautomation.Activities.Models.MqttResponse
import java.sql.Timestamp

data class topicResponse(var mqttResponse: MqttResponse, var isReceived : Boolean)
