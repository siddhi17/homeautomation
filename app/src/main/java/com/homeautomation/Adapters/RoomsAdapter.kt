package com.homeautomation.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.homeautomation.Activities.Responses.GetRoomsResponse
import com.homeautomation.R
import com.homeautomation.Utils.WordUtils
import com.homeautomation.capitalizeString
import kotlinx.android.synthetic.main.item_room_layout.view.*
import kotlinx.android.synthetic.main.item_rooms_layout.view.*
import kotlinx.android.synthetic.main.item_rooms_layout.view.img_room
import kotlinx.android.synthetic.main.item_rooms_layout.view.text_room

class RoomsAdapter (private val context: Context, private var list: ArrayList<GetRoomsResponse.Room>,
                    val click: RoomsAdapter.RoomsListener)
    : RecyclerView.Adapter<RoomsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.item_rooms_layout, p0, false)
        return MyViewHolder(
            view
        )
    }

    //Return size
    override fun getItemCount(): Int {
        return list.size
    }

    //Bind View Holder
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.apply {

            text_room.text = WordUtils.capitalize(list[position]?.roomName)

            if(list[position].device.size > 1)
                txt_no_devices.text = list[position].device.size.toString() + " Device(s)"
            else
                txt_no_devices.text = list[position].device.size.toString() + " Device"

            if(list[position].roomName == "balcony")
                img_room.setImageResource(R.drawable.ic_balcony)
            else if(list[position].roomName == "backyard")
                img_room.setImageResource(R.drawable.ic_backyard)
            else if(list[position].roomName == "basement")
                img_room.setImageResource(R.drawable.ic_basement)
            else if(list[position].roomName == "bedroom")
                img_room.setImageResource(R.drawable.ic_bed_room)
            else if(list[position].roomName == "common area")
                img_room.setImageResource(R.drawable.ic_common_area)
            else if(list[position].roomName == "conference room")
                img_room.setImageResource(R.drawable.ic_conference_room)
            else if(list[position].roomName == "corridor")
                img_room.setImageResource(R.drawable.ic_corridor)
            else if(list[position].roomName == "dinning room")
                img_room.setImageResource(R.drawable.ic_dinniing_room)
            else if(list[position].roomName == "gate")
                img_room.setImageResource(R.drawable.ic_gate)
            else if(list[position].roomName == "hall")
                img_room.setImageResource(R.drawable.ic_hall)
            else if(list[position].roomName == "kitchen")
                img_room.setImageResource(R.drawable.ic_kitchen)
            else if(list[position].roomName == "living room")
                img_room.setImageResource(R.drawable.ic_living_room)
            else if(list[position].roomName == "office cabin")
                img_room.setImageResource(R.drawable.ic_office_cabin)
            else if(list[position].roomName == "out door")
                img_room.setImageResource(R.drawable.ic_outdoor)
            else if(list[position].roomName == "stairs")
                img_room.setImageResource(R.drawable.ic_stairs)
            else if(list[position].roomName == "stair way")
                img_room.setImageResource(R.drawable.ic_stairway)
            else if(list[position].roomName == "store room")
                img_room.setImageResource(R.drawable.ic_store_room)
            else if(list[position].roomName == "study room")
                img_room.setImageResource(R.drawable.ic_study_room)
            else if(list[position].roomName == "wash room")
                img_room.setImageResource(R.drawable.ic_washroom)


            card_view_room.setOnClickListener {

                if(list[position].device.isNotEmpty())
                    click.clickItem(position, list)
            }

        }
    }
    interface RoomsListener {

        fun clickItem(pos: Int, list: ArrayList<GetRoomsResponse.Room>)

    }
}