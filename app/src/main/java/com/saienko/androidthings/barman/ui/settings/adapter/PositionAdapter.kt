package com.saienko.androidthings.barman.ui.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.Component
import com.saienko.androidthings.barman.db.entity.Motor
import com.saienko.androidthings.barman.db.entity.Position
import com.saienko.androidthings.barman.ui.base.BaseAdapter
import com.saienko.androidthings.barman.ui.settings.adapter.holder.PositionViewHolder

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/12/17
 * Time: 12:10
 */

class PositionAdapter(private val onItemListener: OnItemListener) : BaseAdapter<Position, PositionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): PositionViewHolder {
        return PositionViewHolder(onItemListener,
                                  LayoutInflater.from(parent.context)
                                          .inflate(R.layout.recycler_item_position, parent, false))
    }

    override fun onBindViewHolder(holder: PositionViewHolder, position: Int) {
        val item = getItem(position)
        val motor: Motor? = item.motor
        val component: Component? = item.component
        val context = holder.motorName.context
        if (motor != null) {
            holder.motorName.text = motor.motorName
            holder.motorName.setTextColor(holder.motorName.context.getColor(R.color.textColor))
        } else {
            holder.motorName.text = context.getString(R.string.no_motor)
            holder.motorName.setTextColor(holder.motorName.context.getColor(R.color.red_A700))
        }
        if (component != null) {
            holder.componentName.text = component.componentName
            holder.componentName.setTextColor(holder.componentName.context.getColor(R.color.textColor))
        } else {
            holder.componentName.text = context.getString(R.string.no_component)
            holder.componentName.setTextColor(holder.componentName.context.getColor(R.color.red_A700))
        }
        holder.position = item
    }

    interface OnItemListener {
        fun onDelete(position: Position)

        fun onItemClick(position: Position)
    }
}

