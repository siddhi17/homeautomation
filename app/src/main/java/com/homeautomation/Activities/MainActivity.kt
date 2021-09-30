package com.homeautomation.Activities

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.homeautomation.Activities.Models.MqttResponse
import com.homeautomation.Activities.Models.StaticRoom
import com.homeautomation.Activities.Responses.GetLocationsResponse
import com.homeautomation.Activities.Responses.GetRoomsResponse
import com.homeautomation.Activities.Responses.topicResponse
import com.homeautomation.R
import com.homeautomation.Utils.ErrorUtil
import com.homeautomation.Utils.MqttClientHelper
import com.homeautomation.Utils.NetworkUtils
import com.homeautomation.Utils.ProgressDialogUtils
import com.homeautomation.base.BaseActivity
import com.homeautomation.showToast
import com.homeautomation.viewModels.HomeViewModel
import com.homeautomation.viewModels.LocationViewModel
import com.homeautomation.viewModels.LoginViewModel
import com.homeautomation.viewModels.RoomsViewModel
import org.eclipse.paho.client.mqttv3.MqttException
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : BaseActivity() {

    lateinit var homeViewModel: HomeViewModel
    lateinit var loginViewModel: LoginViewModel
    lateinit var locationViewModel: LocationViewModel
    lateinit var roomsViewModel: RoomsViewModel
    var mqttClient: MqttClientHelper? = null
    var timer = Timer()
    var timerDevice = Timer()
    var gson = Gson()
    override fun init() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = Navigation.findNavController(this,R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_scheduler, R.id.navigation_favourites, R.id.navigation_settings)
                .build()
       // navView.itemIconTintList = null

        navView.setupWithNavController(navController)

        homeViewModel.mProgess.value = true

        getDevices()

        mqttClient = MqttClientHelper(this)

    }

    override fun onResume() {
        super.onResume()

        init()
    }

    fun listenToMqtt(devicesList: ArrayList<GetRoomsResponse.Device>) {
        try {
            lateinit var topic: String
            homeViewModel.mProgess.value = true

            timer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    for (device in devicesList) {
                        topic = device.deviceId.trim() + "\\1"

                        mqttClient?.let { homeViewModel.hitMqttServer(it, topic)

                        }
                    }
                }
            }, 10000, 10000)

        } catch (e: MqttException) {
            // More code to handle exception
        } catch (e: IllegalArgumentException) {
        }
    }

    fun getDevices() {

        if (NetworkUtils.isInternetAvailable(this)) {
            homeViewModel.userId = preference.userId
            homeViewModel.hitGetDevicesApi(true)
        } else {
            showToast(getString(R.string.error_internet))
        }
    }

    private fun getUser() {

        if (NetworkUtils.isInternetAvailable(this)) {
            loginViewModel.emailId = preference.userEmailId
            loginViewModel.hitGetUserApi()
        } else {
            showToast(getString(R.string.error_internet))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        loginViewModel =
                ViewModelProvider(this).get(LoginViewModel::class.java)
        locationViewModel =
                ViewModelProvider(this).get(LocationViewModel::class.java)
        roomsViewModel = ViewModelProvider(this).get(RoomsViewModel::class.java)

        homeViewModel.userId = preference.userId
        locationViewModel.userId = preference.userId

        getUser()
        //init()

        myObserver()
    }

    private fun myObserver() {


    }

    override fun onDestroy() {
        timer.cancel();
        super.onDestroy()
    }
}