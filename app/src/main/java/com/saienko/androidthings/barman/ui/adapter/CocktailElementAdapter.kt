package com.saienko.androidthings.barman.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.CocktailElement
import com.saienko.androidthings.barman.ui.adapter.view.CocktailElementViewHolder
import java.util.*

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 16:10
 */

class CocktailElementAdapter : RecyclerView.Adapter<CocktailElementViewHolder>() {

    private var cocktailList: List<CocktailElement> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): CocktailElementViewHolder {
        return CocktailElementViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_cocktail_ingredient, parent, false))
    }

    override fun onBindViewHolder(holder: CocktailElementViewHolder, position: Int) {
        val item = cocktailList[position]
        holder.volume.text = item.volume.toString()
        holder.name.text = item.component?.componentName

        holder.cocktailElement = item
    }

    override fun getItemCount(): Int {
        return cocktailList.size
    }

    fun addItems(list: List<CocktailElement>) {
        this.cocktailList = list
        notifyDataSetChanged()
    }

    fun clear() {
        this.cocktailList = ArrayList()
        notifyDataSetChanged()
    }
}

