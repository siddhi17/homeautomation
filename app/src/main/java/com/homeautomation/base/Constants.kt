package com.homeautomation.base

interface Constants {

    companion object {
        const val BACK_PRESS_TIME_INTERVAL: Long = 2000
        // Api Constants
        const val BASE_URL = "http://15.206.91.225:8000"

        /*LOGIN & REGISTER*/
        const val LOGIN_URL = "/auth"
        const val REGISTER_URL = "/createUser"
        const val GET_USER = "/user"
        const val ADD_LOCATION = "/createLocation"
        const val GET_LOCATIONS = "/locations"
        const val GET_DEVICES = "/devices"
        const val GET_ROOMS = "/rooms"
        const val ADD_ROOM = "/createRoom"

        const val PREFRENCE_NAME = "MyPrefs"

        // Formats
        const val EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    }

}