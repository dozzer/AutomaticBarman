package com.saienko.androidthings.barman.ui.settings

import android.content.DialogInterface
import android.os.Bundle
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.Position
import com.saienko.androidthings.barman.db.interfaces.IBaseManager
import com.saienko.androidthings.barman.db.manager.PositionManager
import com.saienko.androidthings.barman.ui.base.BaseAdapter
import com.saienko.androidthings.barman.ui.base.BaseListActivity
import com.saienko.androidthings.barman.ui.dialog.CustomDialog
import com.saienko.androidthings.barman.ui.settings.adapter.PositionAdapter
import com.saienko.androidthings.barman.ui.settings.adapter.holder.PositionViewHolder

class SettingsPositionsActivity : BaseListActivity<Position, PositionViewHolder>() {
    override val adapter: BaseAdapter<Position, PositionViewHolder>
        get() = localAdapter
    override val baseManager: IBaseManager<Position>
        get() = PositionManager()

    private lateinit var localAdapter: PositionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_positions)
        initUI()
    }

    override fun initUI() {
        super.initUI()
        initRecyclerView(findViewById(R.id.list))
    }

    override fun initAdapter() {
        localAdapter = PositionAdapter(object : PositionAdapter.OnItemListener {
            override fun onDelete(position: Position) {
                showDeleteDialog(position)
            }

            override fun onItemClick(position: Position) {
                editPosition(position)
            }
        })
    }

    private fun editPosition(position: Position) {
        if (position.component != null) {
            AddPositionActivity.edit(this@SettingsPositionsActivity, position.id)
        } else {
            AddPositionActivity.add(this@SettingsPositionsActivity, position.id, position.motorId)
        }
    }

    private fun showDeleteDialog(position: Position) {
        CustomDialog.showDialog(this,
                                getString(R.string.are_you_sure),
                                getString(R.string.do_you_want_to_delete_this_item),
                                DialogInterface.OnClickListener { _, _ -> deleteItem(position) })
    }
}
