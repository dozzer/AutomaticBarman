package com.saienko.androidthings.barman.ui.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.Motor
import com.saienko.androidthings.barman.ui.base.BaseAdapter
import com.saienko.androidthings.barman.ui.settings.adapter.holder.MotorViewHolder

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 16:10
 */

class MotorAdapter(private val onItemListener: OnItemListener) : BaseAdapter<Motor, MotorViewHolder>() {
    override fun onBindViewHolder(holder: MotorViewHolder, position: Int) {
        val motor = getItem(holder.adapterPosition)
        holder.textName.text = motor.motorName

        val gpio = motor.gpio
        if (gpio != null) {
            holder.textPin.text = gpio.gpioPin.toString()
        }
        holder.motorSpeed.text = motor.motorSpeed.toString()
        holder.btnDelete
                .setOnClickListener { onItemListener.onDelete(motor) }
        holder.motor = motor
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MotorViewHolder {
        return MotorViewHolder(onItemListener,
                               LayoutInflater.from(parent.context)
                                       .inflate(R.layout.recycler_item_motor, parent, false))
    }

    interface OnItemListener {
        fun onDelete(motor: Motor)
        fun onItemClick(motor: Motor)
    }
}
