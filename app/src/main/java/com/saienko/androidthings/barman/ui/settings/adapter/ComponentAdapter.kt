package com.saienko.androidthings.barman.ui.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.Component
import com.saienko.androidthings.barman.ui.base.BaseAdapter
import com.saienko.androidthings.barman.ui.settings.adapter.holder.ComponentViewHolder

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 16:10
 */

class ComponentAdapter(private val onItemListener: OnItemListener) : BaseAdapter<Component, ComponentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ComponentViewHolder {
        return ComponentViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false))
    }

    override fun onBindViewHolder(holder: ComponentViewHolder, position: Int) {
//        val component = getItem(position)
        val component = getItem(holder.adapterPosition)
        holder.textName.text = component.componentName
        holder.btnEdit
                .setOnClickListener { onItemListener.onEdit(component) }
        holder.btnDelete
                .setOnClickListener { onItemListener.onDelete(component) }
    }

    interface OnItemListener {
        fun onEdit(component: Component)

        fun onDelete(component: Component)
    }

}
