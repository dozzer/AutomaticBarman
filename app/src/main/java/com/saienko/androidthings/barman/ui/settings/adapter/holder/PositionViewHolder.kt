package com.saienko.androidthings.barman.ui.settings.adapter.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.Position
import com.saienko.androidthings.barman.ui.settings.adapter.PositionAdapter

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/26/17
 * Time: 22:46
 */
class PositionViewHolder(private val onItemListener: PositionAdapter.OnItemListener, v: View) : RecyclerView.ViewHolder(
        v), View.OnClickListener {
    var motorName: TextView = v.findViewById(R.id.motorName)
    var componentName: TextView = v.findViewById(R.id.component)
    var btnDelete: ImageButton = v.findViewById(R.id.btnDelete)
    lateinit var position: Position

    init {
        btnDelete.setOnClickListener { onItemListener.onDelete(position) }
        v.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        onItemListener.onItemClick(position)
    }
}
