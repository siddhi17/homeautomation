package com.homeautomation.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.homeautomation.Activities.Models.Device
import com.homeautomation.Activities.Models.StaticRoom
import com.homeautomation.Activities.Responses.GetLocationsResponse
import com.homeautomation.Activities.Responses.GetRoomsResponse
import com.homeautomation.Adapters.AvailableRoomsAdapter
import com.homeautomation.R
import com.homeautomation.Utils.ErrorUtil
import com.homeautomation.Utils.NetworkUtils
import com.homeautomation.Utils.ProgressDialogUtils
import com.homeautomation.base.BaseActivity
import com.homeautomation.databinding.ActivityLocationBinding
import com.homeautomation.databinding.ActivityRoomsBinding
import com.homeautomation.showToast
import com.homeautomation.viewModels.DeviceViewModel
import com.homeautomation.viewModels.HomeViewModel
import com.homeautomation.viewModels.LocationViewModel
import com.homeautomation.viewModels.RoomsViewModel
import kotlinx.android.synthetic.main.activity_location.*
import kotlinx.android.synthetic.main.activity_rooms.*
import java.lang.reflect.Type


class RoomsActivity : BaseActivity(), View.OnClickListener {

    lateinit var roomsViewModel: RoomsViewModel
    lateinit var homeViewModel: HomeViewModel
    lateinit var deviceViewModel: DeviceViewModel
    var roomAdapter: AvailableRoomsAdapter? = null
    var roomList: ArrayList<GetRoomsResponse.Room> = ArrayList()

    var roomId: String = ""

    override fun init() {

        val binding: ActivityRoomsBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_rooms)

        roomsViewModel = ViewModelProvider(this).get(RoomsViewModel::class.java)
        deviceViewModel = ViewModelProvider(this).get(DeviceViewModel::class.java)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding.data = roomsViewModel
        binding.click = this

        roomsViewModel.locationId = intent.getStringExtra("locationId").toString()
        roomsViewModel.userId = preference.userId
        homeViewModel.userId = preference.userId

      /*  roomList.add(StaticRoom("Balcony", R.drawable.ic_balcony,false))
        roomList.add(StaticRoom("Backyard", R.drawable.ic_backyard,false))
        roomList.add(StaticRoom("Basement", R.drawable.ic_basement,false))
        roomList.add(StaticRoom("Bed Room", R.drawable.ic_bed_room,false))


        roomList.add(StaticRoom("Common Area", R.drawable.ic_common_area,false))
        roomList.add(StaticRoom("Conference Room", R.drawable.ic_conference_room,false))
        roomList.add(StaticRoom("Corridor", R.drawable.ic_corridor,false))

        roomList.add(StaticRoom("Dinning Room", R.drawable.ic_dinniing_room,false))
        roomList.add(StaticRoom("Gate", R.drawable.ic_gate,false))
        roomList.add(StaticRoom("Hall", R.drawable.ic_hall,false))
        roomList.add(StaticRoom("Kitchen", R.drawable.ic_kitchen,false))

        roomList.add(StaticRoom("Living Room", R.drawable.ic_living_room,false))
        roomList.add(StaticRoom("Office Cabin", R.drawable.ic_office_cabin,false))
        roomList.add(StaticRoom("Out Door", R.drawable.ic_outdoor,false))

        roomList.add(StaticRoom("Stairs", R.drawable.ic_stairs,false))
        roomList.add(StaticRoom("Stair Way", R.drawable.ic_stairway,false))
        roomList.add(StaticRoom("Store Room", R.drawable.ic_store_room,false))
        roomList.add(StaticRoom("Study Room", R.drawable.ic_study_room,false))
        roomList.add(StaticRoom("Wash Room", R.drawable.ic_washroom,false))*/

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //   setContentView(R.layout.activity_rooms)

        init()

        setRoomsAdapter()
        myObserver()

    }

    override fun onResume() {
        super.onResume()
        getRooms()
    }
    //Set Rooms Adapter
    private fun setRoomsAdapter() {

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@RoomsActivity)
        rv_rooms.layoutManager = layoutManager


        if (roomAdapter == null) {
            roomAdapter = AvailableRoomsAdapter(this, roomList,
                    object : AvailableRoomsAdapter.RoomsListener{
                        override fun clickItem(pos: Int, list: ArrayList<GetRoomsResponse.Room>) {

                            roomAdapter?.updateStatus()
                            roomsViewModel.roomName = list[pos].roomName.toString()

                            preference.roomId = list[pos].id

                        }
                    })
            rv_rooms.adapter = roomAdapter
        } else {
            roomAdapter?.notifyDataSetChanged()
        }

    }


    fun addRoomApi(){
        if (NetworkUtils.isInternetAvailable(this)) {
            roomsViewModel.hitAddRoomApi()
        }
        else {
            showToast(getString(R.string.error_internet))
        }
    }

    fun addDeviceApi(){
        if (NetworkUtils.isInternetAvailable(this)) {

            deviceViewModel.userId = preference.userId
            deviceViewModel.locationId = intent.getStringExtra("locationId").toString()
            deviceViewModel.roomId = roomId
            deviceViewModel.deviceName = preference.deviceId
            deviceViewModel.deviceId = preference.deviceId


            deviceViewModel.hitCreateDeviceApi()
        }
        else {
            showToast(getString(R.string.error_internet))
        }
    }
    fun getRooms(){
        if (NetworkUtils.isInternetAvailable(this)) {
            homeViewModel.hitGetRoomsApi(true)
        }
        else {
            showToast(getString(R.string.error_internet))
        }
    }
    private fun myObserver() {

        homeViewModel.getRoomsResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            if (it.result == "true") {
                roomList.clear()

                it.rooms.forEach {

                    if(!roomList.map { it.roomName }.contains(it.roomName))
                        roomList.add(it)

                }

               // roomList.addAll(it.rooms)
                setRoomsAdapter()
            }
        })

        homeViewModel.errorGetRooms.observe(this, Observer {

            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)

        })

        deviceViewModel.createDeviceResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()

           // startActivity(Intent(this@RoomsActivity,ConnectWifiActivity::class.java))

        })

        deviceViewModel.errorCreateDevice.observe(this, Observer {

            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)

        })

        roomsViewModel.addRoomResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()

            roomId = it.roomId
            preference.locationId = it.roomId
          //  addDeviceApi()

        })

        roomsViewModel.errorAddRoom.observe(this, Observer {

            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)

        })

        roomsViewModel.mProgess.observe(this, Observer {
            if (it) {
                ProgressDialogUtils.getInstance().hideProgress()
                ProgressDialogUtils.getInstance().showProgress(this, true)
            } else {
                ProgressDialogUtils.getInstance().hideProgress()
            }
        })

    }

    override fun onClick(v: View?) {

        when(v!!.id)
        {
            R.id.imageView_back -> {
                finish()
            }
            R.id.btn_proceed -> {
                if(roomsViewModel.roomName == "")
                {
                    showToast(getString(R.string.add_room_error))
                }
                else {
                    startActivity(Intent(this@RoomsActivity, ConnectWifiActivity::class.java))
                    finish()
                }
            }
        }

    }
}