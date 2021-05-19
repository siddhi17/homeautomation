package com.homeautomation.base

import androidx.appcompat.app.AppCompatActivity
import com.homeautomation.Utils.NetworkUtils
import com.homeautomation.Utils.SharedPreferenceUtil

abstract class BaseActivity : AppCompatActivity() {


    var doubleBackToExitPressedOnce = false


    abstract fun init()

    val preference by lazy {
        SharedPreferenceUtil.getInstance(applicationContext)
    }

    val checkInternet by lazy {
        NetworkUtils.isInternetAvailable(this)
    }

}