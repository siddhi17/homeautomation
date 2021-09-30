package com.homeautomation.Adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.homeautomation.Activities.Responses.GetLocationsResponse
import com.homeautomation.R
import com.homeautomation.capitalizeString
import kotlinx.android.synthetic.main.item_location_layout.view.*

class LocationsAdapter(private val context: Context, private var list: ArrayList<GetLocationsResponse.Location>,
                       val click: LocationListener)
    : RecyclerView.Adapter<LocationsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    var selectedPosition = -1
    var previousSelectedPosition = -1
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

            text_location.text = capitalizeString(list[position]?.locationName!!)

            radioLocation.setOnCheckedChangeListener { buttonView, isChecked ->
                if(buttonView.isPressed) {

                    selectedPosition = position
                    click.clickItem(position, list)
                }
            }

            radioLocation.isChecked = (list[position].isSelected)
        }
    }
    interface LocationListener {

        fun clickItem(pos: Int, list: ArrayList<GetLocationsResponse.Location>)

    }
    /* update item status */
    fun updateStatus(){
        if(previousSelectedPosition != -1)
            list[previousSelectedPosition].isSelected=!list[previousSelectedPosition].isSelected

        previousSelectedPosition = selectedPosition
        list[selectedPosition].isSelected=!list[selectedPosition].isSelected
        notifyDataSetChanged()
    }

}