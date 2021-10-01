package com.homeautomation.Activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.homeautomation.Activities.Models.sendObject
import com.homeautomation.Activities.Responses.GetRoomsResponse
import com.homeautomation.Adapters.RoomDetailsAdapter
import com.homeautomation.Adapters.SwitchDetailsAdapter
import com.homeautomation.R
import com.homeautomation.Utils.ErrorUtil
import com.homeautomation.Utils.MqttClientHelper
import com.homeautomation.Utils.ProgressDialogUtils
import com.homeautomation.base.BaseActivity
import com.homeautomation.base.Constants
import com.homeautomation.getCurrentTime
import com.homeautomation.millisToDate
import com.homeautomation.showToast
import com.homeautomation.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.activity_device_details.*
import kotlinx.android.synthetic.main.activity_room_detail.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.sql.Timestamp
import java.util.*
import kotlin.collections.ArrayList

class DeviceDetailsActivity : BaseActivity(), View.OnClickListener {

    var roomDetailsAdapter: RoomDetailsAdapter? = null
    lateinit var getRoomResponse: GetRoomsResponse.Device
    val gSon = Gson()
    var timer = Timer()
    var topic: String = ""
    var tempSecond: Long = 0
    private val TEN_SECONDS = 10 * 60 * 10000

    private lateinit var homeViewModel: HomeViewModel
    var mqttClient: MqttClientHelper? = null

    var isApiHit = 0
    var isDeviceOnline = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_details)

        init()

    }

    override fun init() {

        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)

        mqttClient = MqttClientHelper(this)

        myObserver()

        iv_back.setOnClickListener(this)

        getRoomResponse = gSon.fromJson(
                intent.getStringExtra("deviceObject"),
                GetRoomsResponse.Device::class.java
        )
        homeViewModel.devicesList.addAll(listOf(getRoomResponse))

        topic = getRoomResponse.deviceId + "\\1"
        listenToMqtt(homeViewModel.devicesList)

        // text_title.text = getRoomResponse.deviceName

        /*       if(devicesList.size > 1)
                   text_devices.text = devicesList.size.toString() + " Device(s)"
               else
                   text_devices.text = devicesList.size.toString() + " Device"
       */
        setDeviceDetailsAdapter()
        setMqttCallBack()
    }

    fun listenToMqtt(devicesList: java.util.ArrayList<GetRoomsResponse.Device>) {

        lateinit var topic: String
        try {
        //   timer.scheduleAtFixedRate(object : TimerTask() {
               // override fun run() {
                    for (device in devicesList) {
                        topic = device.deviceId.trim() + "\\1"
                        mqttClient?.let { homeViewModel.hitMqttServer(it, topic) }
              //      }
                }
          //  }, 10000, 1000)
        } catch (e: MqttException) {
            // More code to handle exception
        } catch (e: java.lang.IllegalArgumentException) {
        }
    }

    //Set DeviceDetails Adapter
    private fun setDeviceDetailsAdapter() {

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@DeviceDetailsActivity)
        rv_device_detail.layoutManager = layoutManager

        if (roomDetailsAdapter == null) {
            roomDetailsAdapter = RoomDetailsAdapter(this, homeViewModel.devicesList, object : RoomDetailsAdapter.RoomDetailListener {
                override fun clickItem(status: String, deviceId: String, pos: Int, list: GetRoomsResponse.Device.Switches, value: String) {

                    var gson = Gson()
                    var sendObject = gson.toJson(sendObject("switching", list.id.toString(), value,getCurrentTime()))

                  //  ProgressDialogUtils.getInstance().showProgress(this@DeviceDetailsActivity, false)
                    homeViewModel.mProgess.value = true

                    if(status == "Online") {
                        isDeviceOnline = true
                        topic = deviceId.trim() + "\\2"
                        try {
                            isApiHit = 1
                            mqttClient?.let { homeViewModel.hitMqttServerPublish(it, topic, sendObject) }

                          /*  Handler().postDelayed({
                              //  ProgressDialogUtils.getInstance().hideProgress()
                                homeViewModel.mProgess.value = false
                            }, 7000)
*/
                        } catch (ex: MqttException) {
                            "Error subscribing to topic: $topic"
                        } catch (e: IllegalArgumentException) {
                        }
                    }
                    else{
                        isDeviceOnline = false
                        showToast("Device Is Offline Currently.")
                        homeViewModel.mProgess.value = false
                       // ProgressDialogUtils.getInstance().hideProgress()
                    }

                }

                override fun seekBarUpdate(status: String, deviceId: String, pos: Int, list: GetRoomsResponse.Device.Switches, value: String) {
                    super.seekBarUpdate(status,deviceId, pos, list, value)

                    var gson = Gson()
                    var sendObject = gson.toJson(sendObject("switching", list.id.toString(), value, getCurrentTime()))
                   // ProgressDialogUtils.getInstance().showProgress(this@DeviceDetailsActivity, false)

                 //   homeViewModel.mProgess.value = true
                    topic = deviceId.trim() + "\\2"
                    if(status == "Online") {
                        isDeviceOnline = true
                        try {
                            mqttClient?.let {
                                isApiHit = 1
                                homeViewModel.hitMqttServerPublish(it, topic, sendObject) }

                       /*     Handler().postDelayed({

                                homeViewModel.mProgess.value = false
                          //      ProgressDialogUtils.getInstance().hideProgress()
                            }, 2000)*/

                        } catch (ex: MqttException) {
                            "Error subscribing to topic: $topic"
                        } catch (e: IllegalArgumentException) {
                        }
                    }
                    else{
                        isDeviceOnline = false
                        showToast("Device Is Offline Currently.")
                        homeViewModel.mProgess.value = false
                        //ProgressDialogUtils.getInstance().hideProgress()
                    }
                }
            })
            rv_device_detail.adapter = roomDetailsAdapter
        } else {
            roomDetailsAdapter?.notifyDataSetChanged()
        }
    }

    private fun setMqttCallBack() {
        mqttClient?.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(b: Boolean, s: String) {
                val snackbarMsg = "Connected to host:\n'${Constants.SOLACE_MQTT_HOST}'."
                Log.w("Debug", snackbarMsg)

                // showToast(snackbarMsg)
            }

            override fun connectionLost(throwable: Throwable) {
                val snackbarMsg = "Connection to host lost:\n'${Constants.SOLACE_MQTT_HOST}'"
                Log.w("Debug", snackbarMsg)
                // showToast(snackbarMsg)
            }

            @Throws(Exception::class)
            override fun messageArrived(topic: String, mqttMessage: MqttMessage) {
                Log.w("Debug", "Message received from host '${Constants.SOLACE_MQTT_HOST}': $mqttMessage")

            }

            override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken) {
                Log.w("Debug", "Message published to host '${Constants.SOLACE_MQTT_HOST}'")
            }
        })
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                finish()
            }
        }
    }

    private fun myObserver() {

/*        homeViewModel.getDevicesResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()

            if (it.result == "true") {
                homeViewModel.devicesList.clear()
                homeViewModel.devicesList.addAll(it.devices)

                setDeviceDetailsAdapter()

            } else {
                showToast("No Devices Available")
            }

        })

        homeViewModel.errorGetDevices.observe(this, Observer {

            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)

        })*/

        homeViewModel.mProgess.observe(this, Observer {
            if (it) {
                ProgressDialogUtils.getInstance().hideProgress()
                ProgressDialogUtils.getInstance().showProgress(this, true)
            } else {
                ProgressDialogUtils.getInstance().hideProgress()
            }
        })


        MqttClientHelper.mqttListenResponse.observe(this, Observer { it ->
            Log.d("MqttResponse:", it.mqttResponse.DeviceId)

         //   if(isApiHit == 0) {

                var switchesList: ArrayList<GetRoomsResponse.Device.Switches> = ArrayList()

                for (device in homeViewModel.devicesList) {
                    if (it.mqttResponse.DeviceId.trim() == device.deviceId.trim()) {

                        device.isOnline = isTimeUnder10Seconds(it.mqttResponse.timestamp_epoch) == true

                        Log.d("online", device.isOnline.toString() + " time :" + it.mqttResponse.timestamp_epoch)

                        if (device.isOnline == true)
                            device.status = "Online"
                        else
                            device.status = "Offline"

                        if (homeViewModel.devicesList.map { it.deviceId.trim() }.contains(it.mqttResponse.DeviceId.trim())) {
                            for (device in homeViewModel.devicesList) {

                                if ((device.switches.size == 1 && it.mqttResponse.ChannelId_1 != null )|| (device.switches.size == 2 && it.mqttResponse.ChannelId_2 != null )
                                        || (device.switches.size == 3 && it.mqttResponse.ChannelId_3 != null )|| (device.switches.size == 4 && it.mqttResponse.ChannelId_4 != null )
                                        || (device.switches.size == 5 && it.mqttResponse.ChannelId_5 != null) || (device.switches.size == 6 && it.mqttResponse.ChannelId_6 != null )
                                        || (device.switches.size == 7 && it.mqttResponse.ChannelId_7 != null )|| (device.switches.size == 8 && it.mqttResponse.ChannelId_8 != null )
                                        || (device.switches.size == 9 && it.mqttResponse.ChannelId_10 != null ) || (device.switches.size == 11 && it.mqttResponse.ChannelId_11 != null )
                                        || (device.switches.size == 12 && it.mqttResponse.ChannelId_12 != null )) {

                                            switchesList.addAll(device.switches)


                                    switchesList.forEachIndexed { index, element ->

                                        if (element.id == "1") {
                                            element.value = it.mqttResponse.ChannelId_1
                                        } else if (element.id == "2") {
                                            element.value = it.mqttResponse.ChannelId_2
                                        } else if (element.id == "3") {
                                            element.value = it.mqttResponse.ChannelId_3
                                        } else if (element.id == "4") {
                                            element.value = it.mqttResponse.ChannelId_4
                                        } else if (element.id == "5") {
                                            element.value = it.mqttResponse.ChannelId_5
                                        } else if (element.id == "6") {
                                            element.value = it.mqttResponse.ChannelId_6
                                        } else if (element.id == "7") {
                                            element.value = it.mqttResponse.ChannelId_7
                                        } else if (element.id == "8") {
                                            element.value = it.mqttResponse.ChannelId_8
                                        } else if (element.id == "9") {
                                            element.value = it.mqttResponse.ChannelId_9
                                        } else if (element.id == "10") {
                                            element.value = it.mqttResponse.ChannelId_10
                                        } else if (element.id == "11") {
                                            element.value = it.mqttResponse.ChannelId_11
                                        } else if (element.id == "12") {
                                            element.value = it.mqttResponse.ChannelId_12
                                        }
                                    }
                                    switchesList.clear()
                                }
                            }
                        }
                    }
                }
                if (roomDetailsAdapter?.childItemAdapter?.isProgressStarted == false)
                    roomDetailsAdapter?.updateList(homeViewModel.devicesList)
            homeViewModel.mProgess.value = false
                 //   ProgressDialogUtils.getInstance().hideProgress()
            /*}
            else{
                if(isApiHit != 2) {
                    isApiHit = 2
                    if(isDeviceOnline)
                        roomDetailsAdapter?.updateItem()

                    Handler().postDelayed({
                        isApiHit = 0
                    }, 1000)
                    ProgressDialogUtils.getInstance().hideProgress()
                }
                ProgressDialogUtils.getInstance().hideProgress()
            }*/
           // ProgressDialogUtils.getInstance().hideProgress()
        })

    }

    fun isTimeUnder10Seconds(timeStamp: Long): Boolean {

        var isTime: Boolean = false
        try {
  /*          if (timeStamp.toString().length < 13) {
                isTime = ((System.currentTimeMillis() + 19800000) - (timeStamp * 1000)) <= 20000L
            } else
                isTime = (System.currentTimeMillis() + 19800000 - (timeStamp)) <= 20000L*/
            isTime = (System.currentTimeMillis() - (timeStamp * 1000)) <= 20000L
        } catch (exception: Exception) {
            exception.toString()
        }
        return isTime
    }

    override fun onDestroy() {
        timer.cancel();
        super.onDestroy()
    }
}