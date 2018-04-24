package com.saienko.androidthings.barman.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.Cocktail
import com.saienko.androidthings.barman.ui.adapter.view.CocktailViewHolder
import java.util.*

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 16:10
 */

class CocktailAdapter(val onItemListener: OnItemListener) : RecyclerView.Adapter<CocktailViewHolder>() {

    private var cocktailList: List<Cocktail> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): CocktailViewHolder {
        return CocktailViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_main_cocktail, parent, false),
                onItemListener)
    }

    override fun onBindViewHolder(holder: CocktailViewHolder, position: Int) {
        val item = cocktailList[position]
        holder.btn.text = item.cocktailName
        holder.cocktail = item
    }

    override fun getItemCount(): Int {
        return cocktailList.size
    }

    fun addItems(cocktailList: List<Cocktail>) {
        this.cocktailList = cocktailList
        notifyDataSetChanged()
    }

    fun clear() {
        this.cocktailList = ArrayList()
        notifyDataSetChanged()
    }
}
