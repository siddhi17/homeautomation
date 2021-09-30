package com.storepanel.webservice

import android.content.Context
import com.homeautomation.Activities.Models.*
import com.homeautomation.Activities.Responses.*
import com.homeautomation.Utils.MqttClientHelper
import com.homeautomation.base.Constants
import com.homeautomation.showToast
import io.reactivex.Observable
import okhttp3.MultipartBody
import org.eclipse.paho.client.mqttv3.MqttException
import retrofit2.http.*


interface ApiInterface {

    /*Register*/
    @POST(Constants.REGISTER_URL)
    fun registerApi(
            @Body user: User
    ): Observable<RegisterResponse>

    /*Login*/
    @POST(Constants.LOGIN_URL)
    fun loginApi(
            @Body login: Login
    ): Observable<LoginResponse>

    /*Get User*/
    @GET(Constants.GET_USER)
    fun getUserApi(
            @Query("email") email: String
    ): Observable<GetUserResponse>

    /*Add Location*/
    @POST(Constants.ADD_LOCATION)
    fun addLocationApi(
            @Body addLocation: AddLocation
    ): Observable<AddLocationResponse>

    /*Get Locations*/
    @GET(Constants.GET_LOCATIONS)
    fun getLocationsApi(
            @Query("userId") userId: String
    ): Observable<GetLocationsResponse>

    /*Get Devices*/
    @GET(Constants.GET_DEVICES)
    fun getDevicesApi(
        @Query("userId") userId: String
    ): Observable<GetRoomsResponse>

    /*Add Room*/
    @POST(Constants.ADD_ROOM)
    fun addRoomApi(
            @Body addRoom: AddRoom
    ): Observable<AddRoomResponse>

    /*Get Rooms*/
    @GET(Constants.GET_ROOMS)
    fun getRoomsApi(
            @Query("userId") userId: String
    ): Observable<GetRoomsResponse>


    /*Get Device Details API*/
    @GET(Constants.GET_DEVICE_DETAILS)
    fun getDeviceDetailsApi(): Observable<GetDeviceDetailsResponse>

    /*Get Networks API*/
    @GET(Constants.GET_NETWORKS)
    fun getNetworksApi(): Observable<GetNetworks>


    /*Set Network*/
    @POST(Constants.SET_NETWORK)
    fun setNetworkApi(
        @Query("network_ssid") network_ssid: String,
        @Query("password") password: String
    ): Observable<GetConnectionStatusResponse>

    /*Get Connection Status API*/
    @GET(Constants.GET_CONNECTION_STATUS)
    fun getConnectionStatusApi(): Observable<GetConnectionStatusResponse>

    /*Restart Device API*/
    @GET(Constants.RESTART_DEVICE)
    fun restartDeviceApi(): Observable<RestartDeviceResponse>


    /*Add Device*/
    @POST(Constants.ADD_DEVICE)
    fun createDeviceApi(
            @Body device: AddDevice
    ): Observable<AddDeviceResponse>


    fun hitMqttServer(context: Context, topic: String)
    {

    }

}