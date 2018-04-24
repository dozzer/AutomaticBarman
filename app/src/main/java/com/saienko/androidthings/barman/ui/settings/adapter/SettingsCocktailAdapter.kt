package com.saienko.androidthings.barman.ui.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.Cocktail
import com.saienko.androidthings.barman.ui.base.BaseAdapter
import com.saienko.androidthings.barman.ui.settings.adapter.holder.SettingsCocktailViewHolder

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 16:10
 */

class SettingsCocktailAdapter(
        private val onItemListener: OnItemListener) : BaseAdapter<Cocktail, SettingsCocktailViewHolder>() {

    override fun onBindViewHolder(holder: SettingsCocktailViewHolder, position: Int) {
        val cocktail = getItem(holder.adapterPosition)
        holder.textName.text = cocktail.cocktailName
        holder.btnDelete.setOnClickListener { onItemListener.onDelete(cocktail) }
        holder.cocktail = cocktail
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): SettingsCocktailViewHolder {
        return SettingsCocktailViewHolder(onItemListener,
                                          LayoutInflater.from(parent.context)
                                                  .inflate(R.layout.recycler_item_cocktail, parent, false))
    }

    interface OnItemListener {
        fun onItemClick(cocktail: Cocktail)

        fun onDelete(cocktail: Cocktail)
    }

}
