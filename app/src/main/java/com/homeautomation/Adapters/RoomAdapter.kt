package com.homeautomation.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.homeautomation.Activities.Models.Switch
import com.homeautomation.R

class RoomAdapter (private val context: Context, private val list: ArrayList<Switch>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private inner class View1ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) {
            val recyclerViewModel = list[position]

        }
    }

    private inner class View2ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) {
            val recyclerViewModel = list[position]



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ONE) {
            return View1ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.single_switch_item_layout, parent, false)
            )
        }
        return View2ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.room_item_layout, parent, false)
        )
    }

    //Return size
    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].switchType
    }

    //Bind View Holder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (list[position].switchType == VIEW_TYPE_ONE) {
            (holder as View1ViewHolder).bind(position)
        } else {
            (holder as View2ViewHolder).bind(position)
        }
    }

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }

}