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
        var name: String,
        var nickName: String,
        var isDimmable: Boolean,
        val numOfSwitches: Int)