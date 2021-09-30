package com.homeautomation.Activities

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.homeautomation.*
import com.homeautomation.Activities.Models.sendObject
import com.homeautomation.Activities.Responses.GetRoomsResponse
import com.homeautomation.Adapters.RoomDetailsAdapter
import com.homeautomation.Utils.MqttClientHelper
import com.homeautomation.Utils.ProgressDialogUtils
import com.homeautomation.Utils.WordUtils
import com.homeautomation.base.BaseActivity
import com.homeautomation.base.Constants
import com.homeautomation.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.activity_room_detail.*
import kotlinx.android.synthetic.main.item_rooms_layout.view.*
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.util.*
import kotlin.collections.ArrayList

class RoomDetailActivity : BaseActivity(), View.OnClickListener {


    var roomDetailsAdapter: RoomDetailsAdapter? = null
    val devicesList: ArrayList<GetRoomsResponse.Device> = ArrayList()
    lateinit var getRoom: GetRoomsResponse.Room

    val gSon = Gson()
    var mqttClient: MqttClientHelper? = null
    private lateinit var homeViewModel: HomeViewModel
    var topic: String = ""
    var timer = Timer()
    var isApiHit = 0


    override fun init() {


        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)

        mqttClient = MqttClientHelper(this)

        myObserver()

        getRoom = gSon.fromJson(
                intent.getStringExtra("roomObject"),
                GetRoomsResponse.Room::class.java
        )

        homeViewModel.devicesList.addAll(getRoom.device)

        listenToMqtt(homeViewModel.devicesList)

        text_title.text = WordUtils.capitalize(getRoom.roomName)


        if(getRoom.roomName == "balcony")
            imageView_room.setImageResource(R.drawable.ic_balcony)
        else if(getRoom.roomName == "backyard")
            imageView_room.setImageResource(R.drawable.ic_backyard)
        else if(getRoom.roomName == "basement")
            imageView_room.setImageResource(R.drawable.ic_basement)
        else if(getRoom.roomName == "bedroom")
            imageView_room.setImageResource(R.drawable.ic_bed_room)
        else if(getRoom.roomName == "common area")
            imageView_room.setImageResource(R.drawable.ic_common_area)
        else if(getRoom.roomName == "conference room")
            imageView_room.setImageResource(R.drawable.ic_conference_room)
        else if(getRoom.roomName == "corridor")
            imageView_room.setImageResource(R.drawable.ic_corridor)
        else if(getRoom.roomName == "dinning room")
            imageView_room.setImageResource(R.drawable.ic_dinniing_room)
        else if(getRoom.roomName == "gate")
            imageView_room.setImageResource(R.drawable.ic_gate)
        else if(getRoom.roomName == "hall")
            imageView_room.setImageResource(R.drawable.ic_hall)
        else if(getRoom.roomName == "kitchen")
            imageView_room.setImageResource(R.drawable.ic_kitchen)
        else if(getRoom.roomName == "living room")
            imageView_room.setImageResource(R.drawable.ic_living_room)
        else if(getRoom.roomName == "office cabin")
            imageView_room.setImageResource(R.drawable.ic_office_cabin)
        else if(getRoom.roomName == "out door")
            imageView_room.setImageResource(R.drawable.ic_outdoor)
        else if(getRoom.roomName == "stairs")
            imageView_room.setImageResource(R.drawable.ic_stairs)
        else if(getRoom.roomName == "stair way")
            imageView_room.setImageResource(R.drawable.ic_stairway)
        else if(getRoom.roomName == "store room")
            imageView_room.setImageResource(R.drawable.ic_store_room)
        else if(getRoom.roomName == "study room")
            imageView_room.setImageResource(R.drawable.ic_study_room)
        else if(getRoom.roomName == "wash room")
            imageView_room.setImageResource(R.drawable.ic_washroom)


        if (homeViewModel.devicesList.size > 1)
            text_devices.text = homeViewModel.devicesList.size.toString() + " Device(s)"
        else
            text_devices.text = homeViewModel.devicesList.size.toString() + " Device"

        setRoomDetailsAdapter()
        setMqttCallBack()
        control()
    }

    fun control() {
        imageView_back.setOnClickListener(this)
    }

    fun listenToMqtt(devicesList: java.util.ArrayList<GetRoomsResponse.Device>) {

        lateinit var topic: String
        try {
        //    timer.scheduleAtFixedRate(object : TimerTask() {
            //    override fun run() {
                    for (device in devicesList) {
                        topic = device.deviceId.trim() + "\\1"
                        mqttClient?.let { homeViewModel.hitMqttServer(it, topic) }
                    }
             //   }
        //    }, 10000, 1000)
        } catch (e: MqttException) {
            // More code to handle exception
        } catch (e: java.lang.IllegalArgumentException) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_detail)


        init()

    }

    //Set RoomDetails Adapter
    private fun setRoomDetailsAdapter() {

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@RoomDetailActivity)
        rv_room_detail.layoutManager = layoutManager

        if (roomDetailsAdapter == null) {
            roomDetailsAdapter = RoomDetailsAdapter(this, homeViewModel.devicesList, object : RoomDetailsAdapter.RoomDetailListener {
                override fun clickItem(
                    status: String,
                    deviceId: String,
                    pos: Int,
                    list: GetRoomsResponse.Device.Switches,
                    value: String
                ) {
                    homeViewModel.mProgess.value = true
                //    ProgressDialogUtils.getInstance().showProgress(this@RoomDetailActivity, false)
                    if(status == "Online") {
                        var gson = Gson()
                        var sendObject = gson.toJson(
                            sendObject(
                                "switching",
                                list.id.toString(),
                                value,
                                getCurrentTime()
                            )
                        )

                        topic = deviceId.trim() + "\\2"

                        try {
                            isApiHit = 1
                            mqttClient?.let {
                                homeViewModel.hitMqttServerPublish(
                                    it,
                                    topic,
                                    sendObject
                                ) }

                         /*   Handler().postDelayed({
                                homeViewModel.mProgess.value = false
                            }, 7000)
*/
                        } catch (e: MqttException) {
                            // More code to handle exception
                        } catch (e: java.lang.IllegalArgumentException) {
                        }
                    }
                    else{
                        showToast("Device Is Offline Currently.")
                        homeViewModel.mProgess.value = false
                    }

                }

                override fun seekBarUpdate(
                    status: String,
                    deviceId: String,
                    pos: Int,
                    list: GetRoomsResponse.Device.Switches,
                    value: String
                ) {
                    super.seekBarUpdate(status,deviceId, pos, list, value)
                    if(status == "Online") {
                        var gson = Gson()
                        var sendObject = gson.toJson(
                            sendObject(
                                "switching",
                                list.id.toString(),
                                value,
                                getCurrentTime()
                            )
                        )

                     //   homeViewModel.mProgess.value = true
                    //    ProgressDialogUtils.getInstance().showProgress(this@RoomDetailActivity, false)

                        topic = deviceId.trim() + "\\2"

                        try {

                            mqttClient?.let {
                                isApiHit = 1
                                homeViewModel.hitMqttServerPublish(it, topic, sendObject)
                            }
/*
                            Handler().postDelayed({
                                homeViewModel.mProgess.value = false
                            }, 2000)*/
                        } catch (e: MqttException) {
                            // More code to handle exception
                        } catch (e: java.lang.IllegalArgumentException) {
                        }
                    }
                    else{
                        showToast("Device Is Offline Currently.")
                        homeViewModel.mProgess.value = false
                    }

                }
            })
            rv_room_detail.adapter = roomDetailsAdapter
        } else {
            roomDetailsAdapter?.notifyDataSetChanged()
        }

    }

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.imageView_back -> {

                finish()
            }

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

    private fun myObserver() {

/*
        MqttClientHelper.mqttPublishResponse.observe(this, Observer {
            try {
                mqttClient?.subscribe(topic)
                "Subscribed to topic '$topic'"

                //if (it.mqttPublishResponse.status.isNullOrEmpty())
               //     showToast("Unable to change device status, please try again later.")
                //else
                    roomDetailsAdapter?.switchBtnUpdate()

            } catch (ex: MqttException) {
                "Error subscribing to topic: $topic"
            }
        })
*/

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

           // if(isApiHit == 0) {
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

                                if ((device.switches.size == 1 && it.mqttResponse.ChannelId_1 != null) || (device.switches.size == 2 && it.mqttResponse.ChannelId_2 != null)
                                        || (device.switches.size == 3 && it.mqttResponse.ChannelId_3 != null) || (device.switches.size == 4 && it.mqttResponse.ChannelId_4 != null)
                                        || (device.switches.size == 5 && it.mqttResponse.ChannelId_5 != null) || (device.switches.size == 6 && it.mqttResponse.ChannelId_6 != null)
                                        || (device.switches.size == 7 && it.mqttResponse.ChannelId_7 != null) || (device.switches.size == 8 && it.mqttResponse.ChannelId_8 != null)
                                        || (device.switches.size == 9 && it.mqttResponse.ChannelId_10 != null) || (device.switches.size == 11 && it.mqttResponse.ChannelId_11 != null)
                                        || (device.switches.size == 12 && it.mqttResponse.ChannelId_12 != null)) {
                                    switchesList.addAll(device.switches)

                                    switchesList.forEachIndexed { index, element ->

                                        if (element.id == "1" && it.mqttResponse.DeviceId.trim() == device.deviceId.trim()) {
                                            element.value = it.mqttResponse.ChannelId_1
                                        } else if (element.id == "2" && it.mqttResponse.DeviceId.trim() == device.deviceId.trim()) {
                                            element.value = it.mqttResponse.ChannelId_2
                                        } else if (element.id == "3" && it.mqttResponse.DeviceId.trim() == device.deviceId.trim()) {
                                            element.value = it.mqttResponse.ChannelId_3
                                        } else if (element.id == "4" && it.mqttResponse.DeviceId.trim() == device.deviceId.trim()) {
                                            element.value = it.mqttResponse.ChannelId_4
                                        } else if (element.id == "5" && it.mqttResponse.DeviceId.trim() == device.deviceId.trim()) {
                                            element.value = it.mqttResponse.ChannelId_5
                                        } else if (element.id == "6" && it.mqttResponse.DeviceId.trim() == device.deviceId.trim()) {
                                            element.value = it.mqttResponse.ChannelId_6
                                        } else if (element.id == "7" && it.mqttResponse.DeviceId.trim() == device.deviceId.trim()) {
                                            element.value = it.mqttResponse.ChannelId_7
                                        } else if (element.id == "8" && it.mqttResponse.DeviceId.trim() == device.deviceId.trim()) {
                                            element.value = it.mqttResponse.ChannelId_8
                                        } else if (element.id == "9" && it.mqttResponse.DeviceId.trim() == device.deviceId.trim()) {
                                            element.value = it.mqttResponse.ChannelId_9
                                        } else if (element.id == "10" && it.mqttResponse.DeviceId.trim() == device.deviceId.trim()) {
                                            element.value = it.mqttResponse.ChannelId_10
                                        } else if (element.id == "11" && it.mqttResponse.DeviceId.trim() == device.deviceId.trim()) {
                                            element.value = it.mqttResponse.ChannelId_11
                                        } else if (element.id == "12" && it.mqttResponse.DeviceId.trim() == device.deviceId.trim()) {
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

            /*}
                else{
                if(isApiHit != 2) {
                    isApiHit = 2
                    roomDetailsAdapter?.updateItem()
                    Handler().postDelayed({
                        isApiHit = 0
                    }, 1000)
                    homeViewModel.mProgess.value = false
               //     ProgressDialogUtils.getInstance().hideProgress()
                }
                homeViewModel.mProgess.value = false*/
              //  ProgressDialogUtils.getInstance().hideProgress()
          //  }
           // ProgressDialogUtils.getInstance().hideProgress()
        })
    }
    
    fun isTimeUnder10Seconds(timeStamp: Long): Boolean {

        var isTime: Boolean = false
        try {
          /*  if (timeStamp.toString().length < 13) {
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
        timer.cancel()
        mqttClient?.destroy()
        super.onDestroy()
    }

}