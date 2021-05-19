package com.homeautomation.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.homeautomation.Activities.Models.StaticRoom
import com.homeautomation.Activities.Responses.GetLocationsResponse
import com.homeautomation.Adapters.AvailableRoomsAdapter
import com.homeautomation.Adapters.RoomAdapter
import com.homeautomation.R
import com.homeautomation.Utils.ErrorUtil
import com.homeautomation.Utils.NetworkUtils
import com.homeautomation.Utils.ProgressDialogUtils
import com.homeautomation.base.BaseActivity
import com.homeautomation.databinding.ActivityLocationBinding
import com.homeautomation.databinding.ActivityRoomsBinding
import com.homeautomation.showToast
import com.homeautomation.viewModels.LocationViewModel
import com.homeautomation.viewModels.RoomsViewModel
import kotlinx.android.synthetic.main.activity_location.*
import kotlinx.android.synthetic.main.activity_rooms.*


class RoomsActivity : BaseActivity(), View.OnClickListener {

    lateinit var roomsViewModel: RoomsViewModel
    var roomAdapter: AvailableRoomsAdapter? = null
    var roomList: ArrayList<StaticRoom> = ArrayList()


    override fun init() {

        val binding: ActivityRoomsBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_rooms)

        roomsViewModel = ViewModelProvider(this).get(RoomsViewModel::class.java)

        binding.data = roomsViewModel
        binding.click = this

        roomsViewModel.locationId = intent.getStringExtra("locationId").toString()
        roomsViewModel.userId = preference.userId

        roomList.add(StaticRoom("Living Room", R.drawable.ic_living_room))
        roomList.add(StaticRoom("Bed Room", R.drawable.ic_bed_room))
        roomList.add(StaticRoom("Study Room", R.drawable.ic_study_room))

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     //   setContentView(R.layout.activity_rooms)

        init()
        setRoomsAdapter()
        myObserver()

    }
    //Set Rooms Adapter
    private fun setRoomsAdapter() {

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@RoomsActivity)
        rv_rooms.layoutManager = layoutManager


        if (roomAdapter == null) {
            roomAdapter = AvailableRoomsAdapter(this, roomList,
                    object : AvailableRoomsAdapter.RoomsListener{
                        override fun clickItem(pos: Int, list: ArrayList<StaticRoom>) {

                            roomsViewModel.roomName = list[pos].roomName
                            addRoomApi()

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
    private fun myObserver() {

        roomsViewModel.addRoomResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()

            if (it.roomId.isEmpty())
                showToast("Room Already Exist")
            else
                startActivity(Intent(this@RoomsActivity,AddDeviceActivity::class.java))
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
        }

    }
}