package com.saienko.androidthings.barman.ui.base

import android.support.v7.widget.RecyclerView

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 3/22/18
 * Time: 23:57
 */
abstract class BaseAdapter<T, V : RecyclerView.ViewHolder> : RecyclerView.Adapter<V>() {

    private var items: MutableList<T>

    init {
        items = ArrayList()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getItems(): MutableList<T> {
        return items
    }

    fun clear() {
        val count = getItems().size
        getItems().clear()
        notifyItemRangeRemoved(0, count)
    }

    fun addItems(list: MutableList<T>) {
        this.items = list
        notifyDataSetChanged()
        notifyItemInserted(list.size - 1)
    }

    fun getItem(position: Int): T {
        return items[position]
    }
}
