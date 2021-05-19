package com.homeautomation.Activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.homeautomation.Activities.Responses.GetRoomsResponse
import com.homeautomation.Adapters.SwitchesAdapter
import com.homeautomation.R
import com.homeautomation.Utils.ProgressDialogUtils
import com.homeautomation.base.BaseActivity
import com.homeautomation.databinding.ActivityAddDeviceBinding
import com.homeautomation.showToast
import com.homeautomation.viewModels.DeviceViewModel
import kotlinx.android.synthetic.main.activity_add_device.*
import kotlinx.android.synthetic.main.activity_add_device.et_enterPass
import kotlinx.android.synthetic.main.activity_add_device.et_wifi_network
import kotlinx.android.synthetic.main.activity_add_device.imageView_show
import kotlinx.android.synthetic.main.activity_add_device.linear_container
import kotlinx.android.synthetic.main.activity_connect_wifi.*
import kotlinx.android.synthetic.main.add_device_dialog.*

class AddDeviceActivity : BaseActivity(), View.OnClickListener {

    lateinit var deviceViewModel:DeviceViewModel
    var switchesAdapter: SwitchesAdapter? = null
    var switchesList: ArrayList<GetRoomsResponse.Device.Switches> = ArrayList()
    var wifiNetworkList: ArrayList<String> = ArrayList()
    lateinit var dialogNewOrderRequest: Dialog

    var roomCount: Int = 0
    var locationCount: Int = 0

    override fun init() {


        linear_container.visibility = View.GONE
       // btn_connect.visibility = View.GONE

        initStatusDialog("")

        wifiNetworkList.add("WiFi 1")
        wifiNetworkList.add("WiFi 2")
        wifiNetworkList.add("WiFi 3")
        wifiNetworkList.add("WiFi 4")

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityAddDeviceBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_add_device)

        deviceViewModel = ViewModelProvider(this).get(DeviceViewModel::class.java)
        dialogNewOrderRequest = Dialog(this)

        binding.data = deviceViewModel
        binding.click = this


        init()
        setSwitchesAdapter()

    }
    //Set Switches Adapter
    private fun setSwitchesAdapter() {

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@AddDeviceActivity)
        rv_switches.layoutManager = layoutManager

        if (switchesAdapter == null) {
            switchesAdapter = SwitchesAdapter(this, switchesList)
            rv_switches.adapter = switchesAdapter
        } else {
            switchesAdapter?.notifyDataSetChanged()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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

                content.startRippleAnimation();

                Handler().postDelayed({

                    ProgressDialogUtils.getInstance().hideProgress()

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                }, 5000)


            }

            R.id.centerImage ->
            {

                showToast("Ripple effect started")
            }
            R.id.et_wifi_network -> {

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
/*
            R.id.btn_connect -> {

                ProgressDialogUtils.getInstance()
                    .showProgress(this, false)

                showToast("Connecting to WiFi Network")

                Handler().postDelayed({

                    ProgressDialogUtils.getInstance().hideProgress()

                    showToast("WiFi Network Connected, you can add your device now.")

                    startActivity(Intent(this, AddDeviceActivity::class.java))
                    finish()

                }, 3000)


            }*/
        }

    }
    private fun initStatusDialog(msg: String) {

        dialogNewOrderRequest.setContentView(R.layout.add_device_dialog)

        dialogNewOrderRequest.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialogNewOrderRequest.window?.setGravity(Gravity.CENTER)

        dialogNewOrderRequest.show()

        Handler().postDelayed({

            dialogNewOrderRequest.dismiss()
            ProgressDialogUtils.getInstance().hideProgress()

            deviceConnected("")

        }, 5000)

        dialogNewOrderRequest.tv_cancel.setOnClickListener{

            dialogNewOrderRequest.dismiss()
        }

    }
    private fun deviceConnected(msg: String) {

        dialogNewOrderRequest.setContentView(R.layout.add_device_dialog)

        dialogNewOrderRequest.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialogNewOrderRequest.window?.setGravity(Gravity.CENTER)

        dialogNewOrderRequest.show()

        dialogNewOrderRequest.linear_add_device.visibility = View.GONE
      //  dialogNewOrderRequest.linear_device_details.visibility = View.VISIBLE

        Handler().postDelayed({

            ProgressDialogUtils.getInstance().hideProgress()

            dialogNewOrderRequest.dismiss()

            linear_container.visibility = View.VISIBLE
           // btn_connect.visibility = View.VISIBLE

        }, 5000)

        dialogNewOrderRequest.tv_cancel.setOnClickListener{

            dialogNewOrderRequest.dismiss()
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

}