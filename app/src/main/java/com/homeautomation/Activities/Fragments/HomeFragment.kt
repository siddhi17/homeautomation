package com.homeautomation.Activities.Fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.homeautomation.Activities.DeviceDetailsActivity
import com.homeautomation.Activities.LocationActivity
import com.homeautomation.Activities.MainActivity
import com.homeautomation.Activities.Models.AllSwitchesObject
import com.homeautomation.Activities.Models.MqttResponse
import com.homeautomation.Activities.Models.sendObject
import com.homeautomation.Activities.Responses.GetLocationsResponse
import com.homeautomation.Activities.Responses.GetRoomsResponse
import com.homeautomation.Activities.Responses.topicResponse
import com.homeautomation.Activities.RoomDetailActivity
import com.homeautomation.Adapters.DevicesAdapter
import com.homeautomation.Adapters.RoomsAdapter
import com.homeautomation.R
import com.homeautomation.Utils.ErrorUtil
import com.homeautomation.Utils.MqttClientHelper
import com.homeautomation.Utils.NetworkUtils
import com.homeautomation.Utils.ProgressDialogUtils
import com.homeautomation.base.BaseFragment
import com.homeautomation.base.Constants.Companion.SOLACE_MQTT_HOST
import com.homeautomation.getCurrentTime
import com.homeautomation.showToast
import kotlinx.android.synthetic.main.fragment_home.*
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : BaseFragment(), View.OnClickListener {

    private var devicesAdapter: DevicesAdapter? = null
    private var roomsAdapter: RoomsAdapter? = null
    var gson = Gson()
    var mqttListenResponse = MutableLiveData<topicResponse>()
    private var roomsList: ArrayList<GetRoomsResponse.Room> = ArrayList()

    var userName: String = ""

    var mqttClient: MqttClientHelper? = null
    lateinit var mainActivity: MainActivity
    private val TEN_SECONDS = 10000
    var isApiHit = 0

    override fun init() {
        mainActivity = activity as MainActivity

        mainActivity.homeViewModel.userId = preference.userId
        mainActivity.locationViewModel.userId = preference.userId

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        init()
        mqttClient = MqttClientHelper(mainActivity)


        myObserver(false)
        setMqttCallBack()
        controlInit()
        setDevicesAdapter(true)
        setRoomsAdapter()

        val c = Calendar.getInstance().time

        val df = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        txt_date.text = df.format(c)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    fun getRooms(){
        if (NetworkUtils.isInternetAvailable(requireContext())) {
            mainActivity.homeViewModel.hitGetRoomsApi(true)
        }
        else {
            showToast(getString(R.string.error_internet))
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        myObserver(false)
    }

    fun controlInit()
    {
        linear_add.setOnClickListener(this)
        relative_location.setOnClickListener(this)
    }

    //Set Devices Adapter
    private fun setDevicesAdapter(isProgress: Boolean) {

        if (devicesAdapter == null) {
            devicesAdapter = DevicesAdapter(requireContext(), mainActivity.homeViewModel.devicesList, object : DevicesAdapter.DevicesListener {
                override fun clickItem(pos: Int, list: ArrayList<GetRoomsResponse.Device>) {

                    val gSon = Gson()

                    startActivity(Intent(requireContext(), DeviceDetailsActivity::class.java)
                            .putExtra("deviceObject", gSon.toJson(list[pos])))
                }

                override fun changeStatus(pos: Int, device: GetRoomsResponse.Device, value: String) {
                    var topic = ""
                    var gson = Gson()
                    var sendObject = gson.toJson(AllSwitchesObject("111",value, getCurrentTime()))


                    if(device.status == "Online") {
                   //     ProgressDialogUtils.getInstance().showProgress(requireContext(), false)
                        mainActivity.homeViewModel.mProgess.value = true
                        topic = device.deviceId.trim() + "\\2"
                        try {
                            isApiHit = 1
                            mqttClient?.let { mainActivity.homeViewModel.hitMqttServerPublish(it, topic, sendObject) }

                        } catch (ex: MqttException) {
                            "Error subscribing to topic: $topic"
                        } catch (e: IllegalArgumentException) {
                        }
                    }
                    else{
                        showToast("Device Is Offline Currently.")
                    }
                }

                override fun stopProgressBar(position: Int) {
                    super.stopProgressBar(position)

                    if (!isProgress)
                        mainActivity.homeViewModel.mProgess.value = isProgress

                }

            })
            rv_devices.adapter = devicesAdapter
        } else {

            devicesAdapter?.notifyDataSetChanged()
        }
    }
    //Set Rooms Adapter
    private fun setRoomsAdapter() {

        if (roomsAdapter == null) {
            roomsAdapter = RoomsAdapter(requireContext(), roomsList, object : RoomsAdapter.RoomsListener {
                override fun clickItem(pos: Int, list: ArrayList<GetRoomsResponse.Room>) {

                    val gSon = Gson()

                    startActivity(Intent(requireContext(), RoomDetailActivity::class.java)
                            .putExtra("roomObject", gSon.toJson(list[pos]))
                            .putExtra("deviceObject", gSon.toJson(list[pos].device))
                            .putExtra("roomName", list[pos].roomName))
                }
            })
            rv_rooms.adapter = roomsAdapter
        } else {

            roomsAdapter?.notifyDataSetChanged()
        }
    }


    fun getLocations(){
        if (NetworkUtils.isInternetAvailable(requireContext())) {
            mainActivity.locationViewModel.hitGetLocationsApi()
        }
        else {
            showToast(getString(R.string.error_internet))
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun myObserver(isReceived : Boolean) {
        var isReceivedResponse = isReceived

        mainActivity.loginViewModel.getUserResponse.observe(viewLifecycleOwner, Observer {

            preference.firstName = it.user.firstName
            preference.lastName = it.user.lastName
            preference.userId = it.user.id
            preference.userEmailId = it.user.email

            userName = it.user.firstName + " " + it.user.lastName

            txt_welcome.text = "Hello, " + preference.firstName + " !"

            // init()

        })

        mainActivity.loginViewModel.errorGetUser.observe(viewLifecycleOwner, Observer {

            ErrorUtil.handlerGeneralError(requireContext(), it)
        })


        mainActivity.homeViewModel.getDevicesResponse.observe(viewLifecycleOwner, Observer {

            if (it.result == "true") {
                mainActivity.homeViewModel.devicesList.clear()
                mainActivity.homeViewModel.devicesList.addAll(it.devices)

                getRooms()
                setDevicesAdapter(true)
                mainActivity.listenToMqtt(mainActivity.homeViewModel.devicesList)
            } else {
                showToast("No Devices Available")
                ProgressDialogUtils.getInstance().hideProgress()
            }

        })

        mainActivity.homeViewModel.errorGetDevices.observe(viewLifecycleOwner, Observer {

            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(requireContext(), it)

        })


        mainActivity.homeViewModel.getRoomsResponse.observe(viewLifecycleOwner, Observer {

            val tempRoomsList: ArrayList<GetRoomsResponse.Room> = ArrayList()

            if (it.result == "true") {
                tempRoomsList.clear()
                roomsList.clear()
                tempRoomsList.addAll(it.rooms)
                val tempDevicesList: ArrayList<GetRoomsResponse.Device> = ArrayList()

                for (device in mainActivity.homeViewModel.devicesList) {
                    for (rooms in tempRoomsList) {
                        if (device.roomId == rooms.id) {
                            device.roomName = rooms.roomName
                            devicesAdapter?.notifyDataSetChanged()
                        }
                    }
                }

                for (rooms in tempRoomsList) {

                    tempDevicesList.clear()
                    tempDevicesList.addAll(mainActivity.homeViewModel.devicesList.filter { it.roomId == rooms.id })
                    if (tempDevicesList.size > 0)
                        roomsList.add(GetRoomsResponse.Room(rooms.id, rooms.roomName, rooms.userId, rooms.locationId,
                                tempDevicesList.filter { it.roomId.isNotEmpty() }, false, 0))
                }
                setRoomsAdapter()
                getLocations()
            } else {
                showToast("No Rooms Available")
                ProgressDialogUtils.getInstance().hideProgress()
            }

        })

        mainActivity.homeViewModel.errorGetRooms.observe(viewLifecycleOwner, Observer {

            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(requireContext(), it)

        })

        mainActivity.locationViewModel.getLocationsResponse.observe(viewLifecycleOwner, Observer {

            val tempLocationsList: ArrayList<GetLocationsResponse.Location> = ArrayList()

            if (it.result == "true") {
                tempLocationsList.clear()
                tempLocationsList.addAll(it.locations)

                for (location in tempLocationsList) {


                    if (mainActivity.homeViewModel.devicesList.filter { !it.isLocationFailed }.map
                            {
                                it.locationId
                            }.contains(location.id)) {

                                val tempId: String = mainActivity.homeViewModel.devicesList.filter { !it.isLocationFailed }[mainActivity.homeViewModel.devicesList.filter
                                { !it.isLocationFailed }.map
                                { it.locationId }.indexOf(location.id)]._id

                        mainActivity.homeViewModel.devicesList[mainActivity.homeViewModel.devicesList.map
                        {
                            it._id
                        }.indexOf(tempId)].locationName = location.locationName ?: ""

                        mainActivity.homeViewModel.devicesList[mainActivity.homeViewModel.devicesList.map
                        {
                            it._id
                        }.indexOf(tempId)].isLocationFailed = true
                    }
                }
                setDevicesAdapter(true)
            }
        })

        mainActivity.locationViewModel.errorGetLocations.observe(viewLifecycleOwner, Observer {

            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(requireContext(), it)

        })


        mainActivity.homeViewModel.mProgess.observe(viewLifecycleOwner, Observer {
            if (it) {
                ProgressDialogUtils.getInstance().hideProgress()
                ProgressDialogUtils.getInstance().showProgress(requireContext(), false)
            } else {
                ProgressDialogUtils.getInstance().hideProgress()
            }
        })


        MqttClientHelper.mqttListenResponse.observe(mainActivity, Observer { it ->
            Log.d("MqttResponse:", it.mqttResponse.DeviceId)

            isReceivedResponse = it.isReceived

                var switchesList: ArrayList<GetRoomsResponse.Device.Switches> = ArrayList()

                for (device in mainActivity.homeViewModel.devicesList) {
                    if (it.mqttResponse.DeviceId.trim() == device.deviceId.trim()) {

                        device.isOnline = isTimeUnder10Seconds(it.mqttResponse.timestamp_epoch) == true

                        Log.d("online", device.isOnline.toString() + " time :" + it.mqttResponse.timestamp_epoch)

                        if (device.isOnline == true)
                            device.status = "Online"
                        else
                            device.status = "Offline"

                        if (mainActivity.homeViewModel.devicesList.map { it.deviceId.trim() }.contains(it.mqttResponse.DeviceId.trim())) {

                            for (device in mainActivity.homeViewModel.devicesList) {

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
                                            if (element.value!! <= 100 && element.value != 0) {
                                                device.value = 100
                                            } else {
                                                device.value = 0
                                            }
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
                                //     devicesAdapter?.updateList(mainActivity.homeViewModel.devicesList)
                                setDevicesAdapter(false)
                                setRoomsAdapter()
                                mainActivity.homeViewModel.mProgess.value = false
                                ProgressDialogUtils.getInstance().hideProgress()
                            }
                        }
                    }
                }
        })


        if(!isReceivedResponse)
        {
            Handler().postDelayed({

                setDevicesAdapter(false)
                setRoomsAdapter()
                mainActivity.homeViewModel.mProgess.value = false
                ProgressDialogUtils.getInstance().hideProgress()

            }, 30000)
        }
    }

    fun isTimeUnder10Seconds(timeStamp: Long) : Boolean{

        var isTime: Boolean = false
        try {
         /*   if(timeStamp.toString().length < 13)
            {
                isTime = ((System.currentTimeMillis() + 19800000) - (timeStamp * 1000)) <= 20000L
            } else
                isTime = (System.currentTimeMillis() + 19800000 - (timeStamp)) <= 20000L*/

            isTime = (System.currentTimeMillis() - (timeStamp * 1000)) <= 20000L
        }
        catch (exception : Exception)
        {
            exception.toString()
        }
        return isTime
    }
    override fun onClick(v: View?) {

        when (v!!.id) {

            R.id.linear_add -> {

                startActivity(Intent(requireContext(), LocationActivity::class.java))

            }
            R.id.relative_location -> {

                startActivity(Intent(requireContext(), LocationActivity::class.java))
            }

        }

    }
    private fun setMqttCallBack() {

        mqttClient?.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(b: Boolean, s: String) {
                val snackbarMsg = "Connected to host:\n'$SOLACE_MQTT_HOST'."
                Log.w("Debug", snackbarMsg)

                //  showToast(snackbarMsg)
            }

            override fun connectionLost(throwable: Throwable) {
                val snackbarMsg = "Connection to host lost:\n'$SOLACE_MQTT_HOST'"
                Log.w("Debug", snackbarMsg)
                // showToast(snackbarMsg)
            }

            @Throws(Exception::class)
            override fun messageArrived(topic: String, mqttMessage: MqttMessage) {
                Log.w("Debug", "Message received from host '$SOLACE_MQTT_HOST': $mqttMessage")

                mqttListenResponse.value = topicResponse(gson.fromJson(mqttMessage.toString(), MqttResponse::class.java),true)
            }

            override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken) {
                Log.w("Debug", "Message published to host '$SOLACE_MQTT_HOST'")
            }
        })
    }
    override fun onDestroy() {
        super.onDestroy()
    }
}