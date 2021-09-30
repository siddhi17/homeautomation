package com.homeautomation.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.gson.Gson
import com.homeautomation.Activities.Models.AddDevice
import com.homeautomation.Activities.Models.Switch
import com.homeautomation.Activities.Responses.GetLocationsResponse
import com.homeautomation.Adapters.SwitchesAdapter
import com.homeautomation.R
import com.homeautomation.Utils.AlertDialogUtil
import com.homeautomation.Utils.ErrorUtil
import com.homeautomation.Utils.NetworkUtils
import com.homeautomation.Utils.ProgressDialogUtils
import com.homeautomation.base.BaseActivity
import com.homeautomation.databinding.ActivityAddDeviceBinding
import com.homeautomation.isConnectingToInternet
import com.homeautomation.showToast
import com.homeautomation.viewModels.DeviceViewModel
import com.homeautomation.viewModels.LocationViewModel
import com.thanosfisherman.wifiutils.WifiUtils
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionErrorCode
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener
import kotlinx.android.synthetic.main.activity_add_device.*
import kotlinx.android.synthetic.main.activity_add_device.et_enterPass
import kotlinx.android.synthetic.main.activity_add_device.et_wifi_network
import kotlinx.android.synthetic.main.activity_add_device.imageView_show
import kotlinx.android.synthetic.main.activity_add_device.linear_container
import kotlinx.android.synthetic.main.activity_connect_wifi.*
import kotlinx.android.synthetic.main.add_device_dialog.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.fixedRateTimer

class AddDeviceActivity : BaseActivity(), View.OnClickListener {

    lateinit var deviceViewModel:DeviceViewModel
    lateinit var locationViewModel: LocationViewModel
    var switchesAdapter: SwitchesAdapter? = null
    var switchesList: ArrayList<Switch> = ArrayList()
    var wifiNetworkList: ArrayList<String> = ArrayList()
    lateinit var dialogNewOrderRequest: Dialog
    var isPermissionGiven: Boolean = false
    var roomCount: Int = 0
    var locationCount: Int = 0
    var switchCount: Int = 0
    var isLocationsAdded: Boolean = false
    var locationCounter: Int = 0
    var connectedToWifi = false

    var isDeviceAdded = false

    var areSwitchesAdded = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun init() {

        if (NetworkUtils.isInternetAvailable(this@AddDeviceActivity)) {
            deviceViewModel.hitGetDeviceDetailsApi()
        } else {
            showToast(getString(R.string.error_internet))
        }

        myObserver()

        linear_switch_details.visibility = View.GONE
    }

    fun fixedRateTimer(): Timer {
        val fixedRate = fixedRateTimer("timer",true,50000,5000){
            this@AddDeviceActivity.runOnUiThread {

                if(!isDeviceAdded)
                    addDeviceApi()
            }
        }
        return fixedRate
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityAddDeviceBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_add_device)

        deviceViewModel = ViewModelProvider(this).get(DeviceViewModel::class.java)

        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)

        dialogNewOrderRequest = Dialog(this)

        binding.data = deviceViewModel
        binding.click = this

        init()
        setupPermissions()
        setSwitchesAdapter()

    }
    //Set Switches Adapter
    private fun setSwitchesAdapter() {

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@AddDeviceActivity)
        rv_switches.layoutManager = layoutManager

        if (switchesAdapter == null) {
            switchesAdapter = SwitchesAdapter(this, switchesList, object : SwitchesAdapter.SwitchListener {
                override fun clickItem(pos: Int, isSelected: Boolean) {

                    switchesList[pos].isDimmable = isSelected
                    switchesAdapter?.notifyDataSetChanged()
                }
            })
            rv_switches.adapter = switchesAdapter
        } else {
            switchesAdapter?.notifyDataSetChanged()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v!!.id) {

       /*     R.id.img_add_bedroom -> {

                roomCount ++
                tv_bedroom_count.setText(roomCount.toString())
            }
            R.id.img_minus_bedroom -> {

                if(roomCount != 0) {
                    roomCount --
                    tv_bedroom_count.setText(roomCount.toString())
                }
            }
            R.id.img_add_location -> {

                locationCount ++
                tv_location_count.text = locationCount.toString()
            }
            R.id.img_minus_location -> {
                if(locationCount != 0) {
                    locationCount --
                    tv_location_count.text = locationCount.toString()
                }
            }*/
            R.id.btn_add_device -> {

                linear_container.visibility = View.GONE
                connection_layout.visibility = View.VISIBLE

                Log.d("SwitchesList", switchesList.toString())

                content.startRippleAnimation();

                Handler().postDelayed({

                    ProgressDialogUtils.getInstance().hideProgress()

                    tv_msg.visibility = View.VISIBLE
                    tv_msg1.visibility = View.VISIBLE
                    tv_msg2.visibility = View.VISIBLE
                    tv_msg3.visibility = View.VISIBLE
                    tv_msg4.visibility = View.VISIBLE

                }, 4000)

                if (NetworkUtils.isInternetAvailable(this@AddDeviceActivity)) {
                    deviceViewModel.hitRestartDeviceApi()
                } else {
                    showToast(getString(R.string.error_internet))
                }
            }

            R.id.centerImage -> {
                showToast("Ripple effect started")
            }
            R.id.et_wifi_network -> {
                if (NetworkUtils.isInternetAvailable(this@AddDeviceActivity)) {
                    deviceViewModel.hitGetNetworksApi()
                } else {
                    showToast(getString(R.string.error_internet))
                }

            }

            R.id.imageView_show -> {
                if (deviceViewModel.isPasswordVisible) {
                    deviceViewModel.isPasswordVisible = false
                    et_enterPass.transformationMethod = null
                    imageView_show.setImageResource(R.drawable.eye_show)
                } else {
                    deviceViewModel.isPasswordVisible = true
                    et_enterPass.transformationMethod =
                            PasswordTransformationMethod.getInstance()
                    imageView_show.setImageResource(R.drawable.eye_hide)
                }

            }
            R.id.btn_connect_wifi -> {

                deviceViewModel.mProgess.value = true

                if (NetworkUtils.isInternetAvailable(this@AddDeviceActivity)) {
                    deviceViewModel.hitSetNetworkApi()
                } else {
                    showToast(getString(R.string.error_internet))
                }

                if (NetworkUtils.isInternetAvailable(this@AddDeviceActivity)) {
                    deviceViewModel.hitGetConnectionStatusApi()
                } else {
                    showToast(getString(R.string.error_internet))
                }

            }
            R.id.imageView_back -> {

                finish()
            }

        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun showWifiNetworkList(array: ArrayList<String>, view: View, context: Context) {

        var list = ArrayList<String>()
        list.addAll(array.map { it })

        val popupMenu = ListPopupWindow(this)
        popupMenu.setAdapter(
                ArrayAdapter(
                        this,
                        R.layout.spinner_dropdown,
                        list
                )
        )
        popupMenu.anchorView = view
        popupMenu.verticalOffset = -20
        popupMenu.setBackgroundDrawable(getDrawable(R.drawable.round_background_filled_white))

        popupMenu.setOnItemClickListener{ adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->

            et_wifi_network.setText(array[i])

            popupMenu.dismiss()
            list.clear()
        }
        popupMenu.isModal = true
        popupMenu.setOnDismissListener {
        }

        popupMenu.show()
    }

    fun addDeviceApi(){
        if (isConnectingToInternet(this@AddDeviceActivity)) {
            connectedToWifi = true
            deviceViewModel.userId = preference.userId
            deviceViewModel.locationId =  preference.locationId
            deviceViewModel.roomId =  preference.roomId
            deviceViewModel.deviceName = preference.deviceName
            deviceViewModel.deviceId = preference.deviceId

            if(!areSwitchesAdded) {
                for (switch in switchesList) {

                    var type: String = ""

                    if (switch.isDimmable)
                        type = "Dimmable"
                    else
                        type = "Non-Dimmable"

                    deviceViewModel.switchesList.add(AddDevice.Switche(switch.nickName.toLowerCase(), type,
                            (switchesList.indexOf(switch) + 1).toString()))
                }
            }
            areSwitchesAdded = true

            if(!isDeviceAdded)
                deviceViewModel.hitCreateDeviceApi()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun myObserver() {

        deviceViewModel.getDeviceDetailsResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()

            tv_device_name_.text = it.Device_Id
            switchCount = it.Number_of_Channels.toInt()

            preference.deviceId = it.Device_Id
            preference.deviceName = it.Device_Name

            for (i in 0 until switchCount) {
                var switch: Int = i + 1
                switchesList.add(Switch("Switch $switch", "", false, 0))
            }

        })

        deviceViewModel.errorGetDeviceDetails.observe(this, Observer {

            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)

        })
        deviceViewModel.restartDeviceResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()


            if(!isDeviceAdded)
                fixedRateTimer()

        })

        deviceViewModel.errorRestartDevice.observe(this, Observer {

            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)

        })


        deviceViewModel.getNetworksResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()

            wifiNetworkList.clear()
            wifiNetworkList.addAll(it.Available_Networks)
            showWifiNetworkList(wifiNetworkList, et_wifi_network, this)
        })

        deviceViewModel.errorGetNetworks.observe(this, Observer {

            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)

        })

        deviceViewModel.getConnectionStatusResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()

            if (it.status == "400:Connection Failed") {
                Toast.makeText(this, "Device Connection Failed.", Toast.LENGTH_SHORT).show()
                btn_connect_wifi.visibility = View.VISIBLE
                linear_container.visibility = View.VISIBLE
                linear_switch_details.visibility = View.GONE
            }
            else {
                Toast.makeText(this, "Device Connected Successfully.", Toast.LENGTH_SHORT).show()
                wifi_bar.visibility = View.GONE
                linear_container.visibility = View.VISIBLE
                linear_switch_details.visibility = View.VISIBLE
            }

        })

        deviceViewModel.errorGetConnectionStatus.observe(this, Observer {

            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)

        })
        deviceViewModel.createDeviceResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()

            if(it.deviceId != "") {
                isDeviceAdded = true

                if(isDeviceAdded)
                    fixedRateTimer().cancel()

                finishAffinity()
                Toast.makeText(this@AddDeviceActivity,"Kushall Device Added Successfully.",
                    Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@AddDeviceActivity, MainActivity::class.java))
            }
            else
            {
                if(isDeviceAdded)
                    Toast.makeText(this@AddDeviceActivity, "This Kushall Device Is Already Exist.",
                        Toast.LENGTH_SHORT).show()
                finishAffinity()
                startActivity(Intent(this@AddDeviceActivity,MainActivity::class.java))
            }
        })

        deviceViewModel.errorCreateDevice.observe(this, Observer {

            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)

        })
        deviceViewModel.mProgess.observe(this, Observer {
            if (it) {
                ProgressDialogUtils.getInstance().hideProgress()
                ProgressDialogUtils.getInstance().showProgress(this, true)
            } else {
                ProgressDialogUtils.getInstance().hideProgress()
            }
        })

    }

    /* Set up run time permissions */
    private fun setupPermissions() {

        askPermission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.CHANGE_NETWORK_STATE
        ) {
            //all of your permissions have been accepted by the user

            isPermissionGiven = true

        }.onDeclined { e ->
            //at least one permission have been declined by the user

            AlertDialogUtil.showSimpleAlertDialog(
                    this,
                    "Please give permissions to connect with Wi-Fi.",
                    true
            )

        }
    }

    override fun onStop() {
        super.onStop()
      //  timer.cancel()
    }
}