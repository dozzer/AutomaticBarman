package com.saienko.androidthings.barman.ui.settings

import android.os.Bundle
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.Gpio
import com.saienko.androidthings.barman.db.interfaces.IBaseManager
import com.saienko.androidthings.barman.db.manager.GpioManager
import com.saienko.androidthings.barman.ui.base.BaseAdapter
import com.saienko.androidthings.barman.ui.base.BaseListActivity
import com.saienko.androidthings.barman.ui.settings.adapter.GpioAdapter
import com.saienko.androidthings.barman.ui.settings.adapter.holder.GpioViewHolder
import kotlinx.android.synthetic.main.activity_settings_list.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class SettingsGpioActivity : BaseListActivity<Gpio, GpioViewHolder>() {

    override val adapter: BaseAdapter<Gpio, GpioViewHolder>
        get() = localAdapter

    override val baseManager: IBaseManager<Gpio>
        get() = GpioManager()

    private lateinit var localAdapter: GpioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_list)
        initUI()
    }

    override fun initUI() {
        super.initUI()
        fabAdd.setOnClickListener { addGpio() }

        initRecyclerView(findViewById(R.id.list))
    }

    override fun onResume() {
        super.onResume()
        fabAdd.post { hideFab() }
    }

    private fun hideFab() {
        if (adapter.getItems().isEmpty()) {
            fabAdd.show()
        } else {
            fabAdd.hide()
        }
    }

    override fun initAdapter() {
        localAdapter = GpioAdapter(object : GpioAdapter.OnItemListener {
            override fun onItemClick(gpio: Gpio) {
                ViewGpioActivity.start(this@SettingsGpioActivity, gpio.id)
            }
        })
    }

    private fun addGpio() {
        launch(UI) {
            async(CommonPool) {
                Gpio.getRealGpios().forEach {
                    baseManager.insert(it)
                }

            }.await()
            hideFab()
            getItemList()
        }
    }
}