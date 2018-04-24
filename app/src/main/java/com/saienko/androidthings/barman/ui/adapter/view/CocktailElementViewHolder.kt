package com.saienko.androidthings.barman.ui.adapter.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.CocktailElement

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 11/10/17
 * Time: 00:15
 */
class CocktailElementViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    var name: TextView = v.findViewById(R.id.itemName)
    var volume: TextView = v.findViewById(R.id.itemVolume)
    lateinit var cocktailElement: CocktailElement
}
