package com.homeautomation.Activities


import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.homeautomation.Adapters.KushallDeviceAdapter
import com.homeautomation.R
import com.homeautomation.Utils.*
import com.homeautomation.base.BaseActivity
import com.homeautomation.showToast
import com.homeautomation.viewModels.DeviceViewModel
import com.thanosfisherman.wifiutils.WifiUtils
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionErrorCode
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener
import kotlinx.android.synthetic.main.activity_connect_wifi.*
import kotlinx.android.synthetic.main.activity_rooms.*
import kotlinx.android.synthetic.main.add_device_dialog.*


class ConnectWifiActivity : BaseActivity(), View.OnClickListener, WifiReceiver.Message {

    lateinit var deviceViewModel: DeviceViewModel
    var kushallNetworkList: ArrayList<String> = ArrayList()
    lateinit var dialogNewOrderRequest: Dialog
    var isPermissionGiven: Boolean = false
    lateinit var wifiManager: WifiManager
    var receiverWifi: WifiReceiver? = null
    var kushallDeviceAdapter: KushallDeviceAdapter? = null
    var isKushallDevice: Boolean = false
    var wifiDeviceSSID: String = ""
    var isKushallDeviceConnection: Boolean = false
    var success: Boolean = false
    var isDeviceFound: Int = 0
    var cancelClicked: Boolean = false
    var wifiEnabled = false

    override fun init() {

        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (wifiManager.isWifiEnabled) {
            initStatusDialog("")

            // wifi is enabled
            text_wifi.visibility = View.GONE
            wifiEnabled = wifiManager.isWifiEnabled

            val intentFilter = IntentFilter()
            receiverWifi = WifiReceiver(wifiManager)
            intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
            registerReceiver(receiverWifi, intentFilter)

            receiverWifi!!.setMessage(this);

            success = wifiManager.startScan()
            if (!success) {
                // scan failure handling
                scanFailure()
            }

        }
        else {
            text_wifi.text = "Please Turn On Wifi To Connect With Kushall Device"
        }

    }

    private fun scanSuccess() {
        val results = wifiManager.scanResults
        Log.d("results", results.toString())
    }

    private fun scanFailure() {
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        val results = wifiManager.scanResults
        Log.d("resultsFailure", results.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect_wifi)
        deviceViewModel = ViewModelProvider(this).get(DeviceViewModel::class.java)

        dialogNewOrderRequest = Dialog(this)

        setupPermissions()

        init()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v!!.id) {

       /*     R.id.et_wifi_network -> {

                showWifiNetworkList(wifiNetworkList, et_wifi_network, this)

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

            R.id.btn_connect -> {

                deviceViewModel.mProgess.value = true

                showToast("Connecting to WiFi Network")

                *//*  Handler().postDelayed({

                    ProgressDialogUtils.getInstance().hideProgress()

                    showToast("WiFi Network Connected, you can add your device now.")

                    startActivity(Intent(this, AddDeviceActivity::class.java))
                    finish()

                }, 3000)*//*

                //  connectToANewWifiNetwork()
                isKushallDeviceConnection = false
                connectWithWpa(this,et_wifi_network.text.toString(),et_enterPass.text.toString())

            }*/

        }
    }

    private fun initStatusDialog(msg: String) {

        dialogNewOrderRequest.setContentView(R.layout.add_device_dialog)

        dialogNewOrderRequest.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialogNewOrderRequest.window?.setGravity(Gravity.CENTER)
        dialogNewOrderRequest.setCancelable(false)

        if(!cancelClicked)
            dialogNewOrderRequest.show()

        Handler().postDelayed({

            ProgressDialogUtils.getInstance().hideProgress()

            if (isDeviceFound == 0)
                deviceConnected("")

        }, 10000)

        dialogNewOrderRequest.tv_cancel.setOnClickListener {
            isDeviceFound = 3
            unregisterReceiver(receiverWifi)
            cancelClicked = true
            dialogNewOrderRequest.dismiss()
            finish()
            startActivity(Intent(this,LocationActivity::class.java))
        }

    }


    private fun deviceConnected(msg: String) {

        dialogNewOrderRequest.setContentView(R.layout.add_device_dialog)

        dialogNewOrderRequest.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialogNewOrderRequest.window?.setGravity(Gravity.CENTER)
        dialogNewOrderRequest.setCancelable(false)

        dialogNewOrderRequest.show()

        dialogNewOrderRequest.linear_add_device.visibility = View.GONE
        dialogNewOrderRequest.linear_device_details.visibility = View.VISIBLE

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@ConnectWifiActivity)
        dialogNewOrderRequest.rv_wifiList.layoutManager = layoutManager


        if (kushallDeviceAdapter == null) {
            kushallDeviceAdapter = KushallDeviceAdapter(this, kushallNetworkList,
                object : KushallDeviceAdapter.ClickListener {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun clickItem(pos: String) {

                        deviceViewModel.mProgess.value = true

                        wifiDeviceSSID = pos
                        isKushallDeviceConnection = true

                        if (isPermissionGiven)
                            connectWithWpa(this@ConnectWifiActivity, wifiDeviceSSID, "testpass")
                        else
                            setupPermissions()

                        dialogNewOrderRequest.dismiss()
                    }
                })
            dialogNewOrderRequest.rv_wifiList.adapter = kushallDeviceAdapter
        } else {
            kushallDeviceAdapter?.notifyDataSetChanged()
        }

        dialogNewOrderRequest.tv_cancel.setOnClickListener {
            isDeviceFound = 3
            unregisterReceiver(receiverWifi)
            cancelClicked = true
            dialogNewOrderRequest.dismiss()
            finish()
            startActivity(Intent(this,LocationActivity::class.java))
        }

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

    override fun getMsg(arrayList: ArrayList<String>) {

        for (item in arrayList) {

            if (item.contains("KushallSwitch")) {
                kushallNetworkList.add(item)
                isKushallDevice = true
            } else {
                isKushallDevice = false
            }
        }
        kushallDeviceAdapter?.notifyDataSetChanged()

        if(kushallNetworkList.isEmpty()) {
            if (isDeviceFound <= 2) {
                Toast.makeText(
                    this,
                    "Could Not Find Kushall Device Wifi, Trying Again.",
                    Toast.LENGTH_SHORT
                ).show()
                success = wifiManager.startScan()
                dialogNewOrderRequest.dismiss()
                initStatusDialog("")
                isDeviceFound++
            }
            else {
                unregisterReceiver(receiverWifi)
                dialogNewOrderRequest.dismiss()
                finish()
                startActivity(Intent(this,LocationActivity::class.java))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun connectWithWpa(context: Context, id: String, pass: String) {

        WifiUtils.withContext(context)
                .connectWith(id, pass)
                .setTimeout(15000)
                .onConnectionResult(object : ConnectionSuccessListener {
                    override fun success() {
                        Toast.makeText(context, "Connected to device WiFi.", Toast.LENGTH_SHORT)
                            .show()

                        startActivity(
                            Intent(
                                this@ConnectWifiActivity,
                                AddDeviceActivity::class.java
                            ).putExtra("deviceSSID", wifiDeviceSSID)
                        )
                        finish()
                        ProgressDialogUtils.getInstance().hideProgress()
                    }

                    override fun failed(errorCode: ConnectionErrorCode) {
                        Toast.makeText(context, "Failed to connect to WiFi.", Toast.LENGTH_SHORT)
                            .show()
                        if (isPermissionGiven)
                            connectWithWpa(this@ConnectWifiActivity, wifiDeviceSSID, "testpass")
                        else
                            setupPermissions()
                    }
                })
                .start()

    }
}