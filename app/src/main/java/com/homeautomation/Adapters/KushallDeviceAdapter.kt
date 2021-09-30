package com.homeautomation.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.homeautomation.R
import kotlinx.android.synthetic.main.item_wifi_layout.view.*

class KushallDeviceAdapter (private val context: Context, private val list: ArrayList<String>,
                            val click: ClickListener)
    : RecyclerView.Adapter<KushallDeviceAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.item_wifi_layout, p0, false)
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

            tv_device_name.text = list[position]


            tv_device_name.setOnClickListener {

                    click.clickItem(list[position])
            }
        }
    }

    interface ClickListener {

        fun clickItem(pos: String)

    }
}