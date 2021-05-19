package com.homeautomation.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.homeautomation.Activities.Responses.GetDevicesResponse
import com.homeautomation.Activities.Responses.GetRoomsResponse
import com.homeautomation.R
import kotlinx.android.synthetic.main.switch_item_layout.view.*


class SwitchesAdapter (private val context: Context, private val list: List<GetRoomsResponse.Device.Switches>)
    : RecyclerView.Adapter<SwitchesAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.switch_item_layout, p0, false)
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

            text_switch_name.text = list[position].switch1
        }
    }
}