package com.storepanel.webservice

import com.homeautomation.Activities.Models.AddLocation
import com.homeautomation.Activities.Models.AddRoom
import com.homeautomation.Activities.Models.User
import com.homeautomation.Activities.Responses.*
import com.homeautomation.base.Constants
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*


interface ApiInterface {

    /*Register*/
    @POST(Constants.REGISTER_URL)
    fun registerApi(
            @Body user: User
    ): Observable<RegisterResponse>

    /*Login*/
    @FormUrlEncoded
    @POST(Constants.LOGIN_URL)
    fun loginApi(
        @Field("email") email: String,
        @Field("pwd") pwd: String
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

}