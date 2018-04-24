package com.saienko.androidthings.barman.ui.settings.adapter.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.Gpio
import com.saienko.androidthings.barman.ui.settings.adapter.GpioAdapter

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/26/17
 * Time: 22:45
 */
class GpioViewHolder(private val onItemListener: GpioAdapter.OnItemListener, v: View) : RecyclerView.ViewHolder(
        v), View.OnClickListener {
    var gpioName: TextView = v.findViewById(R.id.motorName)
    var gpioPin: TextView = v.findViewById(R.id.component)
    lateinit var gpio: Gpio

    init {
        v.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        onItemListener.onItemClick(gpio)
    }
}
