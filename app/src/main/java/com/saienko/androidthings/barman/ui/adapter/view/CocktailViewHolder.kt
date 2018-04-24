package com.saienko.androidthings.barman.ui.adapter.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.Cocktail
import com.saienko.androidthings.barman.ui.adapter.OnItemListener

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 11/10/17
 * Time: 00:15
 */
class CocktailViewHolder(v: View, private val onItemListener: OnItemListener) : RecyclerView.ViewHolder(
        v), View.OnClickListener {

    var btn: Button = v.findViewById(R.id.btnCocktail)
    //    var txt: TextView = v.findViewById(R.id.txtCocktail)
    lateinit var cocktail: Cocktail

    init {
        v.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        onItemListener.onStart(cocktail)
    }
}
