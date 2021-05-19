package com.homeautomation.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.homeautomation.Activities.Models.StaticRoom
import com.homeautomation.Activities.Responses.GetLocationsResponse
import com.homeautomation.Activities.Responses.GetRoomsResponse
import com.homeautomation.R
import kotlinx.android.synthetic.main.item_location_layout.view.*
import kotlinx.android.synthetic.main.item_rooms_layout.view.*

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

            text_room.text = list[position]?.roomName

            if(list[position].device.size > 1)
                txt_no_devices.text = list[position].device.size.toString() + " Device(s)"
            else
                txt_no_devices.text = list[position].device.size.toString() + " Device"


            card_view_room.setOnClickListener {

                click.clickItem(position, list)
            }

        }
    }
    interface RoomsListener {

        fun clickItem(pos: Int, list: ArrayList<GetRoomsResponse.Room>)

    }
}