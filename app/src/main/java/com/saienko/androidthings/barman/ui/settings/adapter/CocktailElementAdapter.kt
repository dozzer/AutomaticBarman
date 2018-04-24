package com.saienko.androidthings.barman.ui.settings.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.CocktailElement
import com.saienko.androidthings.barman.ui.settings.adapter.holder.CocktailElementViewHolder
import java.util.*

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 16:10
 */

class CocktailElementAdapter(
        private val onItemListener: OnItemListener) : RecyclerView.Adapter<CocktailElementViewHolder>() {

    var items: ArrayList<CocktailElement> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): CocktailElementViewHolder {
        return CocktailElementViewHolder(onItemListener,
                                         LayoutInflater.from(parent.context)
                                                 .inflate(R.layout.recycler_item_cocktail_element, parent,
                                                          false))
    }

    override fun onBindViewHolder(holder: CocktailElementViewHolder, position: Int) {
        val item = items[holder.adapterPosition]
        if (item.component != null) {
            val component = item.component
            if (component != null) {
                holder.textName.text = component.componentName
            }
        } else {
            holder.textName.text = holder.textName.context.getString(R.string.this_item_was_deleted)
        }
        holder.textVolume.text = item.volume.toString()
        holder.btnDelete.setOnClickListener { onItemListener.onDelete(item) }
        holder.cocktailElement = item
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun add(component: CocktailElement) {
        items.add(component)
        notifyDataSetChanged()
    }

    fun delete(cocktailElement: CocktailElement) {
        items.remove(cocktailElement)
        notifyDataSetChanged()
    }

    fun addAll(cocktailElements: List<CocktailElement>) {
        for (cocktailElement in cocktailElements) {
            add(cocktailElement)
        }
    }

    fun update(cocktailElement: CocktailElement) {
        for (element in items) {
            if (element.id == cocktailElement.id) {
                element.volume = cocktailElement.volume
                break
            }
        }
        notifyDataSetChanged()
    }

    interface OnItemListener {
        fun onDelete(cocktailElement: CocktailElement)

        fun onEdit(cocktailElement: CocktailElement)
    }
}
