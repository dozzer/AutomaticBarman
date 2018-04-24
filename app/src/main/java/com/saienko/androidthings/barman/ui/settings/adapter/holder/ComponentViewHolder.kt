package com.saienko.androidthings.barman.ui.settings.adapter.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.saienko.androidthings.barman.R

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/26/17
 * Time: 22:44
 */
class ComponentViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    var textName: TextView = v.findViewById(R.id.itemName)
    var btnEdit: ImageButton = v.findViewById(R.id.btnEdit)
    var btnDelete: ImageButton = v.findViewById(R.id.btnDelete)

}
