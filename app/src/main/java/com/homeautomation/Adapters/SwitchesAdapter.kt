package com.homeautomation.Adapters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.homeautomation.Activities.Models.Switch
import com.homeautomation.R


class SwitchesAdapter(val context: Context, val arrayListOptions: ArrayList<Switch>,
                      val listener: SwitchListener) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /*
        Inflate view for recycler*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwitchViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(
                        R.layout.switch_item_layout,
                        parent,
                        false
                )

        return SwitchViewHolder(view, arrayListOptions, listener)
    }

    /*    Return size*/
    override fun getItemCount(): Int {
        return arrayListOptions.size
    }

    /* Set up data */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val holder = holder as SwitchViewHolder

        holder.apply {

            if (!arrayListOptions[adapterPosition].nickName.isNullOrEmpty())
                holder.etName.setText(arrayListOptions[adapterPosition].nickName)
            else
                setHint(holder)

            holder.textSwitchName.text = arrayListOptions[position].name

            holder.switch.setOnClickListener{

                if(arrayListOptions[position].isDimmable)
                {
                    listener.clickItem(position,false)
                }
                else{
                    listener.clickItem(position,true)
                }

            }

            if(arrayListOptions[position].isDimmable) {
                holder.switch.isChecked = true
                holder.textDimmable.text = "Dimmable"
            }
            else {
                holder.switch.isChecked = false
                holder.textDimmable.text = "Non-Dimmable"
            }

        }
    }
    /* AddOn View holder */
    class SwitchViewHolder(
            itemView: View,
            list: ArrayList<Switch>,
            listener: SwitchListener
    ) :
            RecyclerView.ViewHolder(itemView) {

        var etName: EditText = itemView.findViewById(R.id.et_name)
        var switch: SwitchCompat = itemView.findViewById(R.id.switch_order)
        var textSwitchName: TextView = itemView.findViewById(R.id.text_switch_name)
        var textDimmable: TextView = itemView.findViewById(R.id.text_dimmable)

        init {

            etName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {

                    list[adapterPosition].nickName = s.toString()
                }

            })
        }

            fun setHint(holder: RecyclerView.ViewHolder) {
                holder.itemView.apply {
                    etName.setText("")
                    etName.hint = "Enter your friendly name"
                }
            }


            fun EditText.clickableFalse() {
                this.isClickable = false
                this.isCursorVisible = false
            }
    }
    interface SwitchListener {

        fun clickItem(pos: Int, isSelected: Boolean)

    }
}