package com.homeautomation.Activities.Responses

data class GetDeviceDetailsResponse(
    val Model_Number: String,
    val Device_Id: String,
    val Firmware_Version: String,
    val Number_of_Channels: String,
    val MAC: String,
    val Product_By: String,
    val Device_Name: String
)