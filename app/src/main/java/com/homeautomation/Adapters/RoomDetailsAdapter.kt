package com.homeautomation.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.homeautomation.Activities.Responses.GetDevicesResponse
import com.homeautomation.Activities.Responses.GetRoomsResponse
import com.homeautomation.R
import kotlinx.android.synthetic.main.item_switch_layout.view.*

class RoomDetailsAdapter (private val context: Context, private var list: ArrayList<GetRoomsResponse.Device>)
    : RecyclerView.Adapter<RoomDetailsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    private val viewPool = RecyclerView.RecycledViewPool()

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.item_switch_layout, p0, false)
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

            txt_device_name.text = list[position].deviceName

            val layoutManager = LinearLayoutManager(
                rv_switches.context,
                LinearLayoutManager.VERTICAL,
                false
            )
            layoutManager.initialPrefetchItemCount = list[position]
                .switches
                .size

            val childItemAdapter = SwitchDetailsAdapter(
                context,
                list[position].switches
            )
            rv_switches.layoutManager = layoutManager
            rv_switches.adapter = childItemAdapter
            rv_switches.setRecycledViewPool(viewPool)

        }
    }
}