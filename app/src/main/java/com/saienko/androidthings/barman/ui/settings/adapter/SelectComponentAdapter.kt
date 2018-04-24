package com.saienko.androidthings.barman.ui.settings.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.Component
import com.saienko.androidthings.barman.ui.settings.adapter.holder.SpinnerComponentViewHolder

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/10/17
 * Time: 17:25
 */

class SelectComponentAdapter(context: Context,
                             components: List<Component>) : ArrayAdapter<Component>(context,
                                                                                    R.layout.spinner_item_group,
                                                                                    components) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: SpinnerComponentViewHolder
        val item = getItem(position)

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.spinner_item_component, parent, false)
            holder = SpinnerComponentViewHolder(view.findViewById(R.id.componentName))
            holder.name = view.findViewById(R.id.componentName)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as SpinnerComponentViewHolder
        }

        holder.name.text = item.componentName
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {

        val item = getItem(position)
        val inflater = LayoutInflater.from(context)
        val row = inflater.inflate(R.layout.spinner_item_component, parent, false)
        val txtTitle = row.findViewById<TextView>(R.id.componentName)

        txtTitle.text = item.componentName
        return row
    }

}
