package com.saienko.androidthings.barman.ui.settings.adapter.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.Cocktail
import com.saienko.androidthings.barman.ui.settings.adapter.SettingsCocktailAdapter

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/26/17
 * Time: 22:47
 */
class SettingsCocktailViewHolder(private val onItemListener: SettingsCocktailAdapter.OnItemListener,
                                 v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
    var textName: TextView = v.findViewById(R.id.itemName)
    var btnDelete: ImageButton = v.findViewById(R.id.btnDelete)
    lateinit var cocktail: Cocktail

    init {
        btnDelete.setOnClickListener { onItemListener.onDelete(cocktail) }
        v.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        onItemListener.onItemClick(cocktail)
    }
}
