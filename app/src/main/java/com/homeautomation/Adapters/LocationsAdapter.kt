package com.homeautomation.Adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.homeautomation.Activities.Responses.GetLocationsResponse
import com.homeautomation.R
import kotlinx.android.synthetic.main.item_location_layout.view.*

class LocationsAdapter(private val context: Context, private var list: ArrayList<GetLocationsResponse.Location>,
                       val click: LocationListener)
    : RecyclerView.Adapter<LocationsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
                LayoutInflater.from(context)
                        .inflate(R.layout.item_location_layout, p0, false)
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

            text_location.text = list[position]?.locationName

            text_location.setOnClickListener {

                click.clickItem(position, list)
            }

        }
    }
    interface LocationListener {

        fun clickItem(pos: Int, list: ArrayList<GetLocationsResponse.Location>)

    }
}