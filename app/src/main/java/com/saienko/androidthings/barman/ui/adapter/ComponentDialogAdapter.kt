package com.saienko.androidthings.barman.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.Component
import com.saienko.androidthings.barman.ui.adapter.view.ComponentDialogViewHolder

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/10/17
 * Time: 01:34
 */

class ComponentDialogAdapter(context: Context, list: List<Component>) : ArrayAdapter<Component>(context,
                                                                                                R.layout.dialog_item_group,
                                                                                                list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ComponentDialogViewHolder
        val item = getItem(position)

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.dialog_item_group, parent, false)
            holder = ComponentDialogViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ComponentDialogViewHolder
        }

        item?.let {
            holder.name.text = item.componentName
        }

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {

        val item = getItem(position)
        val inflater = LayoutInflater.from(context)
        val row = inflater.inflate(R.layout.dialog_item_group, parent, false)
        val txtTitle = row.findViewById<TextView>(R.id.name)

        txtTitle.text = item.componentName
        return row
    }
}
