package com.homeautomation.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.homeautomation.Activities.Models.StaticRoom
import com.homeautomation.Activities.Responses.AvailableRoomsResponse
import com.homeautomation.Activities.Responses.GetLocationsResponse
import com.homeautomation.Activities.Responses.GetRoomsResponse
import com.homeautomation.R
import com.homeautomation.Utils.WordUtils
import com.homeautomation.capitalizeString
import kotlinx.android.synthetic.main.item_location_layout.view.*
import kotlinx.android.synthetic.main.item_room_layout.view.*
import kotlinx.android.synthetic.main.item_room_layout.view.img_room
import kotlinx.android.synthetic.main.item_room_layout.view.text_room
import kotlinx.android.synthetic.main.item_rooms_layout.view.*

class AvailableRoomsAdapter (private val context: Context, private var list: ArrayList<GetRoomsResponse.Room>,
                             val click: RoomsListener)
    : RecyclerView.Adapter<AvailableRoomsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    var selectedPosition = -1
    var previousSelectedPosition = -1
    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
                LayoutInflater.from(context)
                        .inflate(R.layout.item_room_layout, p0, false)
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

            text_room.text =  WordUtils.capitalize(list[position]?.roomName)


            /*if(WordUtils.capitalize(list[position].roomName) == "Balcony")
                img_room.setImageResource(R.drawable.ic_balcony)
            else if(WordUtils.capitalize(list[position].roomName) == "Backyard")
                img_room.setImageResource(R.drawable.ic_backyard)
            else if(WordUtils.capitalize(list[position].roomName) == "Basement")
                img_room.setImageResource(R.drawable.ic_basement)
            else if(WordUtils.capitalize(list[position].roomName) == "Bed Room")
                img_room.setImageResource(R.drawable.ic_bed_room)
            else if(WordUtils.capitalize(list[position].roomName) == "Common Area")
                img_room.setImageResource(R.drawable.ic_common_area)
            else if(WordUtils.capitalize(list[position].roomName) == "Conference Room")
                img_room.setImageResource(R.drawable.ic_conference_room)
            else if(WordUtils.capitalize(list[position].roomName) == "Corridor")
                img_room.setImageResource(R.drawable.ic_corridor)
            else if(WordUtils.capitalize(list[position].roomName) == "Dinning Room")
                img_room.setImageResource(R.drawable.ic_dinniing_room)
            else if(WordUtils.capitalize(list[position].roomName) == "Gate")
                img_room.setImageResource(R.drawable.ic_gate)
            else if(WordUtils.capitalize(list[position].roomName) == "Hall")
                img_room.setImageResource(R.drawable.ic_hall)
            else if(WordUtils.capitalize(list[position].roomName) == "Kitchen")
                img_room.setImageResource(R.drawable.ic_kitchen)
            else if(WordUtils.capitalize(list[position].roomName) == "Living Room")
                img_room.setImageResource(R.drawable.ic_living_room)
            else if(WordUtils.capitalize(list[position].roomName) == "Office Cabin")
                img_room.setImageResource(R.drawable.ic_office_cabin)
            else if(WordUtils.capitalize(list[position].roomName) == "Out Door")
                img_room.setImageResource(R.drawable.ic_outdoor)
            else if(WordUtils.capitalize(list[position].roomName) == "Stairs")
                img_room.setImageResource(R.drawable.ic_stairs)
            else if(WordUtils.capitalize(list[position].roomName) == "Stair Way")
                img_room.setImageResource(R.drawable.ic_stairway)
            else if(WordUtils.capitalize(list[position].roomName) == "Store Room")
                img_room.setImageResource(R.drawable.ic_store_room)
            else if(WordUtils.capitalize(list[position].roomName) == "Study Room")
                img_room.setImageResource(R.drawable.ic_study_room)
            else if(WordUtils.capitalize(list[position].roomName) == "Wash Room")
                img_room.setImageResource(R.drawable.ic_washroom)*/


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




            radioRoom.setOnCheckedChangeListener { buttonView, isChecked ->
                if(buttonView.isPressed) {

                    selectedPosition = position
                    click.clickItem(position, list)
                }
            }

            radioRoom.isChecked = (list[position].isSelected)
        }
    }
    interface RoomsListener {

        fun clickItem(pos: Int, list: ArrayList<GetRoomsResponse.Room>)

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