package com.homeautomation.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.homeautomation.Activities.Responses.GetRoomsResponse
import com.homeautomation.R
import com.homeautomation.Utils.WordUtils
import kotlinx.android.synthetic.main.item_device_layout.view.*
import kotlinx.android.synthetic.main.item_device_layout.view.img_status
import kotlinx.android.synthetic.main.item_single_switch.view.*

class DevicesAdapter (private val context: Context, private var list: ArrayList<GetRoomsResponse.Device>,
                      val click: DevicesAdapter.DevicesListener)
    : RecyclerView.Adapter<DevicesAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    var selectedPosition=-1
    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.item_device_layout, p0, false)
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

            txt_loc_name.text = WordUtils.capitalize(list[position].locationName)+ " | "
            txt_device.text = list[position].deviceName
            txt_room_name.text = WordUtils.capitalize(list[position].roomName)

            if(list[position].status == "Online")
                text_status.text = "Online"
            else
                text_status.text = "Offline"


            if(list[position].value != null && list[position].value != 0)
                img_status.setImageResource(R.drawable.component_33)
            else
                img_status.setImageResource(R.drawable.component_32)


            img_status.setOnClickListener{

                if(list[position].value == 0) {
                    selectedPosition = position
                    click.changeStatus(position, list[position], "100")
                }
                else {
                    selectedPosition = position
                    click.changeStatus(position, list[position], "0")
                }
            }


            if (position % 2 == 1) {
                root.setBackgroundResource(R.color.white)
                txt_device.setTextColor(Color.parseColor("#2596CD"))
                txt_room_name.setTextColor(Color.parseColor("#2596CD"))
                txt_loc_name.setTextColor(Color.parseColor("#2596CD"))
                text_status.setTextColor(Color.parseColor("#2596CD"))
            } else {
                root.setBackgroundResource(R.drawable.device_item_background)
                txt_device.setTextColor(Color.WHITE)
                txt_room_name.setTextColor(Color.WHITE)
                txt_loc_name.setTextColor(Color.WHITE)
                text_status.setTextColor(Color.WHITE)
            }

            card_view_switch.setOnClickListener {

                click.clickItem(position, list)
            }

            click.stopProgressBar(position)
        }
    }

    fun updateStatus(){
        if(selectedPosition != -1) {
            if (list[selectedPosition].value == 0)
                list[selectedPosition].value = 100
            else
                list[selectedPosition].value = 0

            //  notifyItemChanged(selectedPosition)
            notifyDataSetChanged()
        }
    }
    fun updateList(listN: ArrayList<GetRoomsResponse.Device>) {
        list = listN
        notifyDataSetChanged()
    }
    interface DevicesListener {

        fun clickItem(pos: Int, list: ArrayList<GetRoomsResponse.Device>)

        fun changeStatus(pos: Int, device: GetRoomsResponse.Device,value: String)
        fun stopProgressBar(position: Int){}
    }
}