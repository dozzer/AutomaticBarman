package com.saienko.androidthings.barman.ui.settings

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.Cocktail
import com.saienko.androidthings.barman.db.interfaces.IBaseManager
import com.saienko.androidthings.barman.db.manager.CocktailManager
import com.saienko.androidthings.barman.ui.base.BaseAdapter
import com.saienko.androidthings.barman.ui.base.BaseListActivity
import com.saienko.androidthings.barman.ui.dialog.CustomDialog
import com.saienko.androidthings.barman.ui.settings.adapter.SettingsCocktailAdapter
import com.saienko.androidthings.barman.ui.settings.adapter.holder.SettingsCocktailViewHolder

class SettingsCocktailsActivity : BaseListActivity<Cocktail, SettingsCocktailViewHolder>() {

    override val adapter: BaseAdapter<Cocktail, SettingsCocktailViewHolder>
        get() = localAdapter
    override val baseManager: IBaseManager<Cocktail>
        get() = CocktailManager()

    private lateinit var localAdapter: SettingsCocktailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_list)
        initUI()
    }

    override fun initUI() {
        super.initUI()
        val fabAddDrink = findViewById<FloatingActionButton>(R.id.fabAdd)
        fabAddDrink.setOnClickListener { addCocktail() }

        initRecyclerView(findViewById(R.id.list))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            clearList()
            getItemList()
        }
    }

    override fun initAdapter() {
        localAdapter = SettingsCocktailAdapter(object : SettingsCocktailAdapter.OnItemListener {
            override fun onItemClick(cocktail: Cocktail) {
                editCocktail(cocktail)
            }

            override fun onDelete(cocktail: Cocktail) {
                showDeleteDialog(cocktail)
            }
        })
    }

    private fun editCocktail(cocktail: Cocktail) {
        AddCocktailActivity.start(this, cocktail, REQUEST_CODE_EDIT_COCKTAIL)
    }

    private fun addCocktail() {
        AddCocktailActivity.start(this, REQUEST_CODE_ADD_COCKTAIL)
    }

//    override fun getItemList() {
//        addSubscription(baseManager.all.sync().subscribe({ cocktails -> localAdapter.addItems(cocktails) }))
//    }

    private fun showDeleteDialog(cocktail: Cocktail) {

        CustomDialog.showDialog(this, getString(R.string.are_you_sure), getString(
                R.string.do_you_want_to_delete_this_item),
                                DialogInterface.OnClickListener { _, _ -> deleteItem(cocktail) })
    }

//    private fun deleteItem(cocktail: Cocktail) {
//        addSubscription(baseManager.delete(cocktail).sync().subscribe({ getItemList() }))
//    }

    companion object {

        private const val REQUEST_CODE_ADD_COCKTAIL = 23
        private const val REQUEST_CODE_EDIT_COCKTAIL = 24


        fun start(activity: Activity) {
            val intent = Intent(activity, SettingsCocktailsActivity::class.java)
            activity.startActivity(intent)
        }
    }
}