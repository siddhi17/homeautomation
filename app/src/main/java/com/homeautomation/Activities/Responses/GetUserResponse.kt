package com.homeautomation.Activities.Responses


import com.google.gson.annotations.SerializedName

data class GetUserResponse(
    @SerializedName("result")
    val result: String,
    @SerializedName("user")
    val user: User
) {
    data class User(
        @SerializedName("_id")
        val id: String,
        @SerializedName("firstName")
        val firstName: String,
        @SerializedName("lastName")
        val lastName: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("pwd")
        val pwd: String
    )
}