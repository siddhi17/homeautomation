package com.homeautomation.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.*
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.google.gson.Gson
import com.homeautomation.Activities.Models.sendObject
import com.homeautomation.Activities.Responses.GetRoomsResponse
import com.homeautomation.R
import com.homeautomation.Utils.WordUtils
import kotlinx.android.synthetic.main.item_single_switch.view.*
import java.sql.Timestamp


class SwitchDetailsAdapter (private val context: Context, private val list: List<GetRoomsResponse.Device.Switches>,
                            val click: SwitchDetailsAdapter.changeStatusListener)
    : RecyclerView.Adapter<SwitchDetailsAdapter.MyViewHolder>() {
    var isApiHit : Boolean = false
    var isProgressStarted : Boolean = false
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val seekBarSB: SeekBar = view.findViewById(R.id.seekBar)
        var counter: Int = 0

        init {



            seekBarSB?.setOnSeekBarChangeListener(object :
                    SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seek: SeekBar,
                                               progress: Int, fromUser: Boolean) {
                    // write custom code for progress is changed
                    view.percentage.text =  seek.progress.toString() + "%"
                    if(isProgressStarted) {

                      //  if(!isApiHit) {

                            if (((seek.progress) % 5) == 0 && seek.progress < 100) {
                                selectedPosition = -1

                                list[adapterPosition].value = seekBarSB.progress
                                Log.d("seekbar", seek.progress.toString())

                                isApiHit = true
                                click.seekBarUpdate(adapterPosition, list[adapterPosition], seek.progress.toString())
                            }
                       // }
                    }
                }

                override fun onStartTrackingTouch(seek: SeekBar) {
                    // write custom code for progress is started
                    Log.d("seekbar start",seek.progress.toString())

                    isProgressStarted = true

                }

                override fun onStopTrackingTouch(seek: SeekBar) {
                    // write custom code for progress is stopped

                    Log.d("seekbar stop",seek.progress.toString())
                    selectedPosition = -1

                    list[adapterPosition].value = seekBarSB.progress

                    if(seekBarSB.progress != 0)
                        view.img_status.setImageResource(R.drawable.component_33)
                    else
                        view.img_status.setImageResource(R.drawable.component_32)

                    isProgressStarted = false

                    click.seekBarUpdate(adapterPosition, list[adapterPosition],seek.progress.toString())
                }
            })
        }

    }
    var selectedPosition=-1
    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.item_single_switch, p0, false)
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

            text_switch.text = WordUtils.capitalize(list[position].name)

            if(list[position].value != null && list[position].value != 0)
                img_status.setImageResource(R.drawable.component_33)
            else
                img_status.setImageResource(R.drawable.component_32)

            seekBar.progress = (list[position].value?: 0)
            percentage.text =  seekBar.progress.toString() + "%"

            img_status.setOnClickListener {

                selectedPosition = position

                if(list[position].value == 0)
                    click.clickItem(position, list[position],"100")
                else
                    click.clickItem(position, list[position],"0")

            }
            seekBar.isEnabled = list[position].type == "Dimmable"
        }
    }
    fun updateStatus(){
        if(selectedPosition != -1) {
            if (list[selectedPosition].value == 0)
                list[selectedPosition].value = 100
            else
                list[selectedPosition].value = 0

            notifyDataSetChanged()
        }
    }

    interface changeStatusListener {

        fun clickItem(pos: Int, list: GetRoomsResponse.Device.Switches,value: String)
        fun seekBarUpdate(pos: Int,list: GetRoomsResponse.Device.Switches,value: String)
        {
        }
    }
}