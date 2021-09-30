package com.homeautomation.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.homeautomation.Activities.Responses.GetRoomsResponse
import com.homeautomation.R
import com.homeautomation.showToast
import kotlinx.android.synthetic.main.item_switch_layout.view.*

class RoomDetailsAdapter (private val context: Context, private var list: ArrayList<GetRoomsResponse.Device>,
                          val click: RoomDetailsAdapter.RoomDetailListener)
    : RecyclerView.Adapter<RoomDetailsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    private val viewPool = RecyclerView.RecycledViewPool()

    var childItemAdapter: SwitchDetailsAdapter? = null
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
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.apply {

            txt_device_name.text = list[position].deviceName

            if(list[position].status == "Online")
                txt_status_device.text = "Online"
            else
                txt_status_device.text = "Offline"

            val layoutManager = LinearLayoutManager(
                rv_switches.context,
                LinearLayoutManager.VERTICAL,
                false
            )
            layoutManager.initialPrefetchItemCount = list[position]
                .switches
                .size

            childItemAdapter = SwitchDetailsAdapter(
                context,
                list[position].switches,
                    object : SwitchDetailsAdapter.changeStatusListener{
                        override fun clickItem(pos: Int, switchlist: GetRoomsResponse.Device.Switches,value: String) {

                            if(list[position].status != null)
                                click.clickItem(list[position].status,list[position].deviceId,pos, list[position].switches[pos],value)
                            else
                                Toast.makeText(context,"Device Is Offline Currently.",Toast.LENGTH_SHORT).show()

                        }

                        override fun seekBarUpdate(pos: Int, switchlist: GetRoomsResponse.Device.Switches, value: String) {
                            super.seekBarUpdate(pos, switchlist, value)
                            if(list[position].status != null)
                                click.seekBarUpdate(list[position].status,list[position].deviceId,pos,list[position].switches[pos],value)
                            else
                                Toast.makeText(context,"Device Is Offline Currently.",Toast.LENGTH_SHORT).show()

                        }
                    }
            )
            rv_switches.layoutManager = layoutManager
            rv_switches.adapter = childItemAdapter
            rv_switches.setRecycledViewPool(viewPool)

        }
    }
    interface RoomDetailListener {

        fun clickItem(status: String,deviceId: String, pos: Int, list: GetRoomsResponse.Device.Switches, value: String)
        fun seekBarUpdate(status: String,deviceId: String, pos: Int, list: GetRoomsResponse.Device.Switches, value: String)
        {
        }
    }

    fun updateItem()
    {
        childItemAdapter?.updateStatus()
    }

    fun updateList(listN: ArrayList<GetRoomsResponse.Device>) {
        list = listN
        notifyDataSetChanged()
    }
}