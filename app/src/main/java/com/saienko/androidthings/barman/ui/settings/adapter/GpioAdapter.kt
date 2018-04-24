package com.saienko.androidthings.barman.ui.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.Gpio
import com.saienko.androidthings.barman.ui.base.BaseAdapter
import com.saienko.androidthings.barman.ui.settings.adapter.holder.GpioViewHolder

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 15:35
 */

class GpioAdapter(private val onItemListener: OnItemListener) : BaseAdapter<Gpio, GpioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): GpioViewHolder {
        return GpioViewHolder(onItemListener,
                              LayoutInflater.from(parent.context)
                                      .inflate(R.layout.recycler_item_gpio, parent, false))
    }

    override fun onBindViewHolder(holder: GpioViewHolder, position: Int) {
        val gpio = getItem(holder.adapterPosition)
        holder.gpioName.text = gpio.gpioName
        holder.gpioPin.text = gpio.gpioPin.toString()
        holder.gpio = gpio
    }

    interface OnItemListener {
        fun onItemClick(gpio: Gpio)
    }
}
