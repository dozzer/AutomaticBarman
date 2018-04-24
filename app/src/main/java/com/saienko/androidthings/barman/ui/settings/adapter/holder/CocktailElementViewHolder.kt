package com.saienko.androidthings.barman.ui.settings.adapter.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.CocktailElement
import com.saienko.androidthings.barman.ui.settings.adapter.CocktailElementAdapter

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 17:15
 */

class CocktailElementViewHolder(private val onItemListener: CocktailElementAdapter.OnItemListener,
                                v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
    var textName: TextView = v.findViewById(R.id.itemName)
    var textVolume: TextView = v.findViewById(R.id.itemVolume)
    var btnDelete: ImageButton = v.findViewById(R.id.btnDelete)
    lateinit var cocktailElement: CocktailElement

    init {
        btnDelete.setOnClickListener { onItemListener.onDelete(cocktailElement) }
        v.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        onItemListener.onEdit(cocktailElement)
    }
}