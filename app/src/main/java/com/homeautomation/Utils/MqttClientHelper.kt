package com.homeautomation.Utils

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.homeautomation.Activities.Models.MqttResponse
import com.homeautomation.Activities.Responses.MqttListenResponse
import com.homeautomation.Activities.Responses.MqttPublishResponse
import com.homeautomation.Activities.Responses.publishResponse
import com.homeautomation.Activities.Responses.topicResponse
import com.homeautomation.base.Constants.Companion.SOLACE_CONNECTION_CLEAN_SESSION
import com.homeautomation.base.Constants.Companion.SOLACE_CONNECTION_KEEP_ALIVE_INTERVAL
import com.homeautomation.base.Constants.Companion.SOLACE_CONNECTION_RECONNECT
import com.homeautomation.base.Constants.Companion.SOLACE_CONNECTION_TIMEOUT
import com.homeautomation.base.Constants.Companion.SOLACE_MQTT_HOST
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.MqttClient


class MqttClientHelper(context: Context?) {

    companion object {
        const val TAG = "MqttClientHelper"
        var mqttListenResponse = MutableLiveData<topicResponse>()
        var mqttPublishResponse = MutableLiveData<publishResponse>()
    }

    var mqttAndroidClient: MqttAndroidClient? = null
    val serverUri = SOLACE_MQTT_HOST
    private val clientId: String = MqttClient.generateClientId()
    var mProgess = MutableLiveData<Boolean>()
    var errorMqttResponse = MutableLiveData<Throwable>()
    var gson = Gson()

    fun setCallback(callback: MqttCallbackExtended?) {
        mqttAndroidClient?.setCallback(callback)
    }

    init {

        mqttAndroidClient = MqttAndroidClient(context, serverUri, clientId)
        mqttAndroidClient!!.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(b: Boolean, s: String) {
                Log.w(TAG, s)

            }

            override fun connectionLost(throwable: Throwable) {}


            @Throws(Exception::class)
            override fun messageArrived(
                topic: String,
                mqttMessage: MqttMessage
            ) {
                Log.w(TAG, mqttMessage.toString())

                mqttListenResponse.value = topicResponse(gson.fromJson(mqttMessage.toString(),MqttResponse::class.java),true)

            }

            override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken) {}
        })
        connect()
    }

    private fun connect() {

        if (mqttAndroidClient != null) {

            val mqttConnectOptions = MqttConnectOptions()
            mqttConnectOptions.isAutomaticReconnect = SOLACE_CONNECTION_RECONNECT
            mqttConnectOptions.isCleanSession = SOLACE_CONNECTION_CLEAN_SESSION
            /*   mqttConnectOptions.userName = SOLACE_CLIENT_USER_NAME
        mqttConnectOptions.password = SOLACE_CLIENT_PASSWORD.toCharArray()*/
            mqttConnectOptions.connectionTimeout = SOLACE_CONNECTION_TIMEOUT
            mqttConnectOptions.keepAliveInterval = SOLACE_CONNECTION_KEEP_ALIVE_INTERVAL
            try {
                mqttAndroidClient?.connect(mqttConnectOptions, null, object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken) {
                        val disconnectedBufferOptions =
                                DisconnectedBufferOptions()
                        disconnectedBufferOptions.isBufferEnabled = true
                        disconnectedBufferOptions.bufferSize = 100
                        disconnectedBufferOptions.isPersistBuffer = false
                        disconnectedBufferOptions.isDeleteOldestMessages = false
                        mqttAndroidClient?.setBufferOpts(disconnectedBufferOptions)
                    }

                    override fun onFailure(
                            asyncActionToken: IMqttToken,
                            exception: Throwable
                    ) {
                        Log.w(TAG, "Failed to connect to: $serverUri ; $exception")
                    }
                })
            } catch (ex: MqttException) {
                ex.printStackTrace()
            }
        }
    }

    fun subscribe(subscriptionTopic: String, qos: Int = 0) {

        if (mqttAndroidClient != null) {

            if (mqttAndroidClient?.isConnected == true) {

                try {
                    mqttAndroidClient?.subscribe(subscriptionTopic, qos, null, object : IMqttActionListener {
                        override fun onSuccess(asyncActionToken: IMqttToken) {
                            Log.w(TAG, "Subscribed to topic'$subscriptionTopic'")
                        }

                        override fun onFailure(
                                asyncActionToken: IMqttToken,
                                exception: Throwable
                        ) {
                            Log.w(TAG, "Subscription to topic '$subscriptionTopic' failed!")
                        }
                    })
                } catch (ex: MqttException) {
                    System.err.println("Exception whilst subscribing to topic '$subscriptionTopic'")
                    ex.printStackTrace()
                }
            }
        }
    }

    fun publish(topic: String, msg: String, qos: Int = 0) {

        if (mqttAndroidClient != null && mqttAndroidClient!!.isConnected()) {

            try {
                val message = MqttMessage()
                message.payload = msg.toByteArray()
                mqttAndroidClient?.publish(topic, message.payload, qos, false)
                Log.d(TAG, "Message published to topic `$topic`: $msg")

            } catch (e: MqttException) {
                Log.d(TAG, "Error Publishing to $topic: " + e.message)
                e.printStackTrace()
            }
        }

    }

    fun isConnected() : Boolean? {
        return mqttAndroidClient?.isConnected
    }

    fun destroy() {
        mqttAndroidClient?.unregisterResources()
        mqttAndroidClient?.disconnect()
    }
}