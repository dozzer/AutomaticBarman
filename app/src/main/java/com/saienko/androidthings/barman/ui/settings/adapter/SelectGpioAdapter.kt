package com.saienko.androidthings.barman.ui.settings.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.Gpio
import com.saienko.androidthings.barman.ui.settings.adapter.holder.SettingsGpioViewHolder

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/10/17
 * Time: 17:25
 */

class SelectGpioAdapter(context: Context,
                        gpioList: List<Gpio>) : ArrayAdapter<Gpio>(context, R.layout.spinner_item_gpio, gpioList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: SettingsGpioViewHolder
        val item = getItem(position)

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.spinner_item_gpio, parent, false)
            holder = SettingsGpioViewHolder(view.findViewById(R.id.motorName), view.findViewById(R.id.motorPin))
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as SettingsGpioViewHolder
        }

        holder.name.text = item.gpioName
        holder.pin.text = String.format(view.context.getText(R.string.pin_value).toString(),
                                        item.gpioPin)
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {

        val item = getItem(position)
        val inflater = LayoutInflater.from(context)
        val row = inflater.inflate(R.layout.spinner_item_gpio, parent, false)
        val txtTitle = row.findViewById<TextView>(R.id.motorName)
        val pin = row.findViewById<TextView>(R.id.motorPin)

        if (item != null) {
            txtTitle.text = item.gpioName
            pin.text = String.format(context.getText(R.string.pin_value).toString(),
                                     item.gpioPin)
        }
        return row
    }

}
