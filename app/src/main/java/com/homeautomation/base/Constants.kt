package com.homeautomation.base

interface Constants {

    companion object {
        const val BACK_PRESS_TIME_INTERVAL: Long = 2000
        // Api Constants
        const val BASE_URL = "https://kushall.com/app/"

        /*LOGIN & REGISTER*/
        const val LOGIN_URL = "auth"
        const val REGISTER_URL = "createUser"
        const val GET_USER = "user"
        const val ADD_LOCATION = "createLocation"
        const val GET_LOCATIONS = "locations"
        const val GET_DEVICES = "devices"
        const val GET_ROOMS = "rooms"
        const val ADD_ROOM = "createRoom"
        const val ADD_DEVICE = "createDevice"

        const val PREFRENCE_NAME = "MyPrefs"

        const val SOLACE_MQTT_HOST = "tcp://kushall.com:1883"

        // Other options
        const val SOLACE_CONNECTION_TIMEOUT = 1
        const val SOLACE_CONNECTION_KEEP_ALIVE_INTERVAL = 60
        const val SOLACE_CONNECTION_CLEAN_SESSION = true
        const val SOLACE_CONNECTION_RECONNECT = true

        const val GET_DEVICE_DETAILS = "http://192.168.4.22/getDeviceDetails"
        const val GET_NETWORKS = "http://192.168.4.22/getNetworks"
        const val SET_NETWORK = "http://192.168.4.22/setNetwork"
        const val GET_CONNECTION_STATUS  = "http://192.168.4.22/getConnectionStatus"
        const val RESTART_DEVICE = "http://192.168.4.22/restartDevice"

        // Formats
        const val EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    }

}