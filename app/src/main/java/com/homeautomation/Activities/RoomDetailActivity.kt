package com.homeautomation.Activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.homeautomation.Activities.Responses.GetRoomsResponse
import com.homeautomation.Adapters.RoomDetailsAdapter
import com.homeautomation.R
import com.homeautomation.base.BaseActivity
import kotlinx.android.synthetic.main.activity_room_detail.*

class RoomDetailActivity : BaseActivity(), View.OnClickListener {


    var roomDetailsAdapter: RoomDetailsAdapter? = null
    val devicesList: ArrayList<GetRoomsResponse.Device> = ArrayList()
    lateinit var getRoomResponse: GetRoomsResponse.Room
    val gSon = Gson()

    override fun init() {
        
        getRoomResponse = gSon.fromJson(
            intent.getStringExtra("roomObject"),
            GetRoomsResponse.Room::class.java
        )

        devicesList.addAll(getRoomResponse.device)

        text_title.text = getRoomResponse.roomName

        if(devicesList.size > 1)
            text_devices.text = devicesList.size.toString() + " Device(s)"
        else
            text_devices.text = devicesList.size.toString() + " Device"
        
        setRoomDetailsAdapter()

        control()
    }

    fun control()
    {
        imageView_back.setOnClickListener(this)
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
            roomDetailsAdapter = RoomDetailsAdapter(this,devicesList)
            rv_room_detail.adapter = roomDetailsAdapter
        } else {
            roomDetailsAdapter?.notifyDataSetChanged()
        }

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