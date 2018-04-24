package com.saienko.androidthings.barman.ui.settings.adapter.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.Motor
import com.saienko.androidthings.barman.ui.settings.adapter.MotorAdapter

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/26/17
 * Time: 22:46
 */
class MotorViewHolder(
        private val onItemListener: MotorAdapter.OnItemListener, v: View) : RecyclerView.ViewHolder(
        v), View.OnClickListener {

    val textName: TextView = v.findViewById(R.id.motorName)
    val textPin: TextView = v.findViewById(R.id.motorPin)
    val motorSpeed: TextView = v.findViewById(R.id.tvMotorSpeed)
    val btnDelete: ImageButton = v.findViewById(R.id.btnDelete)
    lateinit var motor: Motor


    init {
        btnDelete.setOnClickListener { onItemListener.onDelete(motor) }
        v.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        onItemListener.onItemClick(motor)
    }
}
