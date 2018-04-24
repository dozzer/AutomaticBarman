package com.saienko.androidthings.barman.ui.settings

import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.Menu
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.Motor
import com.saienko.androidthings.barman.db.interfaces.IBaseManager
import com.saienko.androidthings.barman.db.interfaces.IPositionManager
import com.saienko.androidthings.barman.db.manager.MotorManager
import com.saienko.androidthings.barman.db.manager.PositionManager
import com.saienko.androidthings.barman.service.CocktailService
import com.saienko.androidthings.barman.ui.base.BaseAdapter
import com.saienko.androidthings.barman.ui.base.BaseListActivity
import com.saienko.androidthings.barman.ui.dialog.ActionListener
import com.saienko.androidthings.barman.ui.dialog.CustomDialog
import com.saienko.androidthings.barman.ui.settings.adapter.MotorAdapter
import com.saienko.androidthings.barman.ui.settings.adapter.holder.MotorViewHolder

class SettingsMotorActivity : BaseListActivity<Motor, MotorViewHolder>() {

    override val adapter: BaseAdapter<Motor, MotorViewHolder>
        get() = localAdapter
    override val baseManager: IBaseManager<Motor>
        get() = MotorManager()

    private lateinit var positionManager: IPositionManager
    private lateinit var localAdapter: MotorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_list)
        positionManager = PositionManager()
        initUI()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.list_motor, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear -> {
                clearAllMotors()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun clearAllMotors() {

        CustomDialog.selectOneOfTwo(this,
                                    "Clear all motors",
                                    "Do you want clear all motors",
                                    "Short",
                                    "Long",
                                    object : ActionListener {
                                        override fun onAction() {
                                            CocktailService
                                                    .clearShortMotors(this@SettingsMotorActivity, adapter.getItems())
                                        }
                                    },
                                    object : ActionListener {
                                        override fun onAction() {
                                            CocktailService
                                                    .clearLongMotors(this@SettingsMotorActivity, adapter.getItems())
                                        }
                                    })
    }

    override fun initUI() {
        super.initUI()
        val fabAddMotor = findViewById<FloatingActionButton>(R.id.fabAdd)
        fabAddMotor.setOnClickListener { addMotor() }
        initRecyclerView(findViewById(R.id.list))
    }

//    private fun getMotorList() {
//        addSubscription(motorManager.all.sync().subscribe({ motors -> adapter.addItems(motors) }))
//    }

    override fun initAdapter() {
        localAdapter = MotorAdapter(object : MotorAdapter.OnItemListener {

            override fun onDelete(motor: Motor) {
                showDeleteDialog(motor)
            }

            override fun onItemClick(motor: Motor) {
                editMotor(motor)
            }
        })
    }

//    override fun getItemList() {
//        addSubscription(baseManager.all.sync().subscribe({ motors -> adapter.addItems(motors) }))
//    }


    private fun editMotor(motor: Motor) {
        AddMotorActivity.start(this@SettingsMotorActivity, motor.id)
    }

    private fun showDeleteDialog(motor: Motor) {
        CustomDialog.showDialog(this,
                                getString(R.string.are_you_sure),
                                getString(R.string.do_you_want_to_delete_this_item),
                                DialogInterface.OnClickListener { _, _ -> deleteItem(motor) })
    }


    private fun addMotor() {
        AddMotorActivity.start(this@SettingsMotorActivity)
    }
}
