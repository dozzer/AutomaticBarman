package com.saienko.androidthings.barman.ui.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.interfaces.IGpioManager
import com.saienko.androidthings.barman.db.manager.GpioManager
import com.saienko.androidthings.barman.ui.base.BaseActivity
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class ViewGpioActivity : BaseActivity() {

    private var gpioId: Long = -1

    private lateinit var gpioManager: IGpioManager
    private lateinit var tvName: TextView
    private lateinit var tvPin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_gpio)
        gpioManager = GpioManager()
        handleIntent()
        initUI()
        loadData()
    }

    private fun loadData() {
        val gpioManager = GpioManager()
        launch(UI) {
            val gpio = async(CommonPool) { gpioManager.get(gpioId) }.await()
            tvName.text = gpio.gpioName
            tvPin.text = gpio.gpioPin.toString()
        }
    }

    private fun handleIntent() {
        gpioId = intent.getLongExtra(EXTRA_GPIO, gpioId)
    }

    override fun initUI() {
        super.initUI()
        tvName = findViewById(R.id.tvName)
        tvPin = findViewById(R.id.tvPin)

    }

    companion object {

        private const val EXTRA_GPIO = "EXTRA_GPIO"

        fun start(activity: Activity, gpioId: Long) {
            val intent = Intent(activity, ViewGpioActivity::class.java)
            intent.putExtra(EXTRA_GPIO, gpioId)
            activity.startActivity(intent)
        }
    }
}
