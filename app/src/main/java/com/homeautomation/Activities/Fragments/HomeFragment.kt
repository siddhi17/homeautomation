package com.homeautomation.Activities.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.homeautomation.Activities.LocationActivity
import com.homeautomation.Activities.Responses.GetDevicesResponse
import com.homeautomation.Activities.Responses.GetLocationsResponse
import com.homeautomation.Activities.Responses.GetRoomsResponse
import com.homeautomation.Activities.RoomDetailActivity
import com.homeautomation.Adapters.DevicesAdapter
import com.homeautomation.Adapters.RoomsAdapter
import com.homeautomation.R
import com.homeautomation.Utils.ErrorUtil
import com.homeautomation.Utils.NetworkUtils
import com.homeautomation.Utils.ProgressDialogUtils
import com.homeautomation.base.BaseFragment
import com.homeautomation.showToast
import com.homeautomation.viewModels.HomeViewModel
import com.homeautomation.viewModels.LocationViewModel
import com.homeautomation.viewModels.LoginViewModel
import kotlinx.android.synthetic.main.activity_rooms.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.rv_rooms
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : BaseFragment(), View.OnClickListener {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var locationViewModel: LocationViewModel

    private var devicesAdapter: DevicesAdapter? = null
    private var roomsAdapter: RoomsAdapter? = null

    private var devicesList: ArrayList<GetRoomsResponse.Device> = ArrayList()
    private var roomsList: ArrayList<GetRoomsResponse.Room> = ArrayList()



    override fun init() {

        val c = Calendar.getInstance().time

        val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        txt_date.text = df.format(c)

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        loginViewModel =
                ViewModelProvider(this).get(LoginViewModel::class.java)
        locationViewModel =
                ViewModelProvider(this).get(LocationViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        homeViewModel.userId = preference.userId
        locationViewModel.userId = preference.userId

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
        controlInit()

        getDevices()
        myObserver()
        setDevicesAdapter()
        setRoomsAdapter()
    }

    override fun onResume() {
        super.onResume()

        txt_welcome.text = "Hello, " + loginViewModel.firstname+ " " + loginViewModel.lastName + " !"
        txt_welcome.text = "Hello, " + preference.userName + " !"
    }

    override fun onStart() {
        super.onStart()
        txt_welcome.text = "Hello, " + loginViewModel.firstname+ " " + loginViewModel.lastName + " !"
        txt_welcome.text = "Hello, " + preference.userName + " !"
    }

    fun controlInit()
    {
        linear_add.setOnClickListener(this)
        relative_location.setOnClickListener(this)
    }

    //Set Devices Adapter
    private fun setDevicesAdapter() {

        if (devicesAdapter == null) {
            devicesAdapter = DevicesAdapter(requireContext(), devicesList)
            rv_devices.adapter = devicesAdapter
        } else {
            devicesAdapter?.notifyDataSetChanged()
        }
    }
    //Set Rooms Adapter
    private fun setRoomsAdapter() {

        if (roomsAdapter == null) {
            roomsAdapter = RoomsAdapter(requireContext(), roomsList, object: RoomsAdapter.RoomsListener{
                override fun clickItem(pos: Int, list: ArrayList<GetRoomsResponse.Room>) {

                    val gSon = Gson()

                    startActivity(Intent(requireContext(),RoomDetailActivity::class.java)
                        .putExtra("roomObject", gSon.toJson(list[pos]))
                        .putExtra("roomName",list[pos].roomName))

                }
            })
            rv_rooms.adapter = roomsAdapter
        } else {
            roomsAdapter?.notifyDataSetChanged()
        }
    }

    fun getDevices(){
        if (NetworkUtils.isInternetAvailable(requireContext())) {
            homeViewModel.hitGetDevicesApi()
        }
        else {
            showToast(getString(R.string.error_internet))
        }
    }
    fun getRooms(){
        if (NetworkUtils.isInternetAvailable(requireContext())) {
            homeViewModel.hitGetRoomsApi()
        }
        else {
            showToast(getString(R.string.error_internet))
        }
    }
    fun getLocations(){
        if (NetworkUtils.isInternetAvailable(requireContext())) {
            locationViewModel.hitGetLocationsApi()
        }
        else {
            showToast(getString(R.string.error_internet))
        }
    }

    private fun myObserver() {

        homeViewModel.getDevicesResponse.observe(viewLifecycleOwner, Observer {
            ProgressDialogUtils.getInstance().hideProgress()

            if(it.result == "true") {
                devicesList.clear()
                devicesList.addAll(it.devices)

                getRooms()
                setDevicesAdapter()
            }
            else
            {
               showToast("No Devices Available")
            }

        })

        homeViewModel.errorGetDevices.observe(viewLifecycleOwner, Observer {

            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(requireContext(), it)

        })


        homeViewModel.getRoomsResponse.observe(viewLifecycleOwner, Observer {
            ProgressDialogUtils.getInstance().hideProgress()

            val tempRoomsList: ArrayList<GetRoomsResponse.Room> = ArrayList()

            if(it.result == "true") {
                tempRoomsList.clear()
                tempRoomsList.addAll(it.rooms)
                val tempDevicesList: ArrayList<GetRoomsResponse.Device> = ArrayList()

                for (rooms in tempRoomsList) {

                    tempDevicesList.clear()
                    tempDevicesList.addAll(devicesList.filter { it.roomId == rooms.id })
                    roomsList.add(GetRoomsResponse.Room(rooms.id,rooms.roomName,rooms.userId,rooms.locationId,
                            tempDevicesList.filter { it.roomId.isNotEmpty() }))
                }
                setRoomsAdapter()
                getLocations()
            }
            else
            {
                showToast("No Rooms Available")
            }
        })

        homeViewModel.errorGetRooms.observe(viewLifecycleOwner, Observer {

            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(requireContext(), it)

        })

        locationViewModel.getLocationsResponse.observe(viewLifecycleOwner, Observer {
            ProgressDialogUtils.getInstance().hideProgress()

            val tempLocationsList: ArrayList<GetLocationsResponse.Location> = ArrayList()

            if(it.result == "true") {
                tempLocationsList.clear()
                tempLocationsList.addAll(it.locations)

                for (location in tempLocationsList) {

                    if(devicesList.map { it.locationId }.contains(location.id))
                        devicesList[devicesList.map { it.locationId }.indexOf(location.id)].locationName = location.locationName?: ""

                }

                setDevicesAdapter()
            }
        })

        homeViewModel.errorGetDevices.observe(viewLifecycleOwner, Observer {

            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(requireContext(), it)

        })


        homeViewModel.mProgess.observe(viewLifecycleOwner, Observer {
            if (it) {
                ProgressDialogUtils.getInstance().hideProgress()
                ProgressDialogUtils.getInstance().showProgress(requireContext(), true)
            } else {
                ProgressDialogUtils.getInstance().hideProgress()
            }
        })

    }
    override fun onClick(v: View?) {

        when (v!!.id) {

          R.id.linear_add -> {

              startActivity(Intent(requireContext(),LocationActivity::class.java))

          }
            R.id.relative_location -> {

                startActivity(Intent(requireContext(),LocationActivity::class.java))
            }

        }

    }

}