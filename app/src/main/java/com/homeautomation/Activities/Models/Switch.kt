package com.homeautomation.Activities.Models


data class RoomsList (
        val name: String,
        val nickName: String,
        val isDimmable: Boolean,
        val numOfSwitches: Int, 
        val switchType: Int,
        var switchList: ArrayList<Switch>
        )

data class Switch (
        val name: String,
        val nickName: String,
        val isDimmable: Boolean,
        val numOfSwitches: Int,
        val switchType: Int
        )