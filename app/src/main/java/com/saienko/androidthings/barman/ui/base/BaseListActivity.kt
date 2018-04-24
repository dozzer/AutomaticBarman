package com.saienko.androidthings.barman.ui.base

import android.content.DialogInterface
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.Item
import com.saienko.androidthings.barman.db.interfaces.IBaseManager
import com.saienko.androidthings.barman.ui.dialog.CustomDialog
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 15:42
 */

abstract class BaseListActivity<T : Item, Holder : RecyclerView.ViewHolder> : BaseActivity() {

    protected abstract val adapter: BaseAdapter<T, Holder>
    protected abstract val baseManager: IBaseManager<T>

    override fun initUI() {
        super.initUI()
        initAdapter()
    }

    private var showMenu: Boolean = true

    open fun getItemList() {
        launch(UI) {
            adapter.clear()
            val list = async(CommonPool) { baseManager.list() }.await()
            adapter.addItems(list)
        }
    }

    fun deleteItem(item: T) {
        launch(UI) {
            async(CommonPool) { baseManager.delete(item) }.await()
            getItemList()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (showMenu) {
            menuInflater.inflate(R.menu.lists, menu)
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        clearList()
        getItemList()
    }

    protected fun clearList() {
        adapter.clear()
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        val i = item.itemId
        return if (i == R.id.action_remove_all) {
            removeAll()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

//    private fun showClearDialog() {
//        CustomDialog.showDialog(this, getString(R.string.are_you_sure), "Do you want to clear all records ?",
//                                DialogInterface.OnClickListener { _, _ -> removeAll() })
//    }

    private fun removeAll() {
        CustomDialog.showDialog(this,
                                getString(R.string.are_you_sure),
                                getString(R.string.do_you_want_to_delete_all_items),
                                DialogInterface.OnClickListener { _, _ ->
                                    launch(UI) {
                                        async(CommonPool) { baseManager.clear() }.await()
                                        getItemList()
                                    }
                                })
    }

    protected fun initRecyclerView(recyclerView: RecyclerView) {
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context,
                                                          layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.adapter = adapter
    }

    protected abstract fun initAdapter()

    fun updateMenu(components: MutableList<Any>) {
        showMenu = !components.isEmpty()
        invalidateOptionsMenu()
    }
}
