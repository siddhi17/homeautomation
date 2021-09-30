package com.homeautomation.Activities.Responses


import com.google.gson.annotations.SerializedName

data class GetLocationsResponse(
    @SerializedName("result")
    val result: String,
    @SerializedName("error")
    val error: String,
    @SerializedName("locations")
    val locations: ArrayList<Location>
) {
    data class Location(
        @SerializedName("_id")
        val id: String?,
        @SerializedName("locationName")
        val locationName: String?,
        @SerializedName("userId")
        val userId: String?,
        var isSelected: Boolean
    )
}
