package com.saienko.androidthings.barman.ui.settings

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.Gpio
import com.saienko.androidthings.barman.db.entity.Motor
import com.saienko.androidthings.barman.db.entity.Position
import com.saienko.androidthings.barman.db.interfaces.IPositionManager
import com.saienko.androidthings.barman.db.manager.GpioManager
import com.saienko.androidthings.barman.db.manager.MotorManager
import com.saienko.androidthings.barman.db.manager.PositionManager
import com.saienko.androidthings.barman.extension.hideKeyboard
import com.saienko.androidthings.barman.listener.OnResultListener
import com.saienko.androidthings.barman.listener.OnUpdateListener
import com.saienko.androidthings.barman.receiver.CocktailBroadcastReceiver
import com.saienko.androidthings.barman.receiver.CocktailUpdateBroadcastReceiver
import com.saienko.androidthings.barman.service.CocktailService
import com.saienko.androidthings.barman.ui.InfoActivity
import com.saienko.androidthings.barman.ui.base.BaseActivity
import com.saienko.androidthings.barman.ui.settings.adapter.SelectGpioAdapter
import kotlinx.android.synthetic.main.activity_add_motor.*
import kotlinx.android.synthetic.main.view_enter_value.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.util.*

open class AddMotorActivity : BaseActivity() {

    private var isNewMotor: Boolean = false
    private lateinit var currentMotor: Motor

    private var gpioList: MutableList<Gpio> = ArrayList()
    private var selectedGpio: Gpio? = null

//    private lateinit var spinnerGpio: Spinner
//    private lateinit var tvName: TextView
//    private lateinit var tvComponent: TextView
//    private lateinit var etSpeed: TextView
//    private lateinit var btnSave: Button
//    private lateinit var btnTest: Button

    private var motorId: Long = 0

    private lateinit var motorManager: MotorManager
    private lateinit var positionManager: IPositionManager

    private lateinit var cocktailBroadcastReceiver: CocktailBroadcastReceiver
    private lateinit var cocktailUpdateBroadcastReceiver: CocktailUpdateBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_motor)
        motorManager = MotorManager()
        positionManager = PositionManager()
        handleIntent()
        registerBroadcastReceiver()
        initUI()
    }

    override fun onResume() {
        super.onResume()
        this.hideKeyboard()
    }

    private fun getFreeGpio(motor: Motor?) {
        val manager = GpioManager()
//        async(CommonPool) {
//            val list = if (motor != null) {
//                manager.freeGpio(motor.gpioId)
//            } else {
//                manager.freeGpio()
//            }
//            gpioList = list
//            initData()
//        }
        launch(UI) {
            val list = async(CommonPool) {
                if (motor != null) {
                    manager.freeGpio(motor.gpioId)
                } else {
                    manager.freeGpio()
                }
            }.await()
            gpioList = list
            initData()
        }
    }

    private fun handleIntent() {
        if (intent.hasExtra(EXTRA_MOTOR)) {
            isNewMotor = false
            motorId = intent.getLongExtra(EXTRA_MOTOR, -1L)
            getMotor(motorId)
        } else {
            isNewMotor = true
            getFreeGpio(null)
        }
    }

    private fun getMotor(motorId: Long) {
//        addSubscription(motorManager.getById(motorId)
//                                .sync()
//                                .subscribe({ motor ->
//                                               currentMotor = motor
//                                               getFreeGpio(motor)
//                                           }))
//        async(CommonPool) {
//            currentMotor = motorManager.get(motorId)
//            getFreeGpio(currentMotor)
//        }
        launch(UI) {
            currentMotor = async(CommonPool) { motorManager.get(motorId) }.await()
            getFreeGpio(currentMotor)
        }
    }

    override fun initUI() {
        super.initUI()

//        spinnerGpio = findViewById(R.id.spinnerGpio)
//        tvName = findViewById(R.id.tvName)
//        tvComponent = findViewById(R.id.tvComponent)
//        etSpeed = findViewById(R.id.etValue)
//        btnSave = findViewById(R.id.btnSave)
//        val minus = findViewById<Button>(R.id.btnMinus)
//        val plus = findViewById<Button>(R.id.btnPlus)
//        val minusTen = findViewById<Button>(R.id.btnMinusTen)
//        val plusTen = findViewById<Button>(R.id.btnMinusTen)
//        btnTest = findViewById(R.id.btnTest)
//        val imgInfo = findViewById<ImageButton>(R.id.imgInfo)

        spinnerGpio.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                selectedGpio = gpioList[i]
                showButtons(true)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
                // IGNORE
            }
        }

        btnSave.setOnClickListener { saveMotor() }


        btnMinus.setOnClickListener {
            var value = Integer.parseInt(etValue.text.toString())
            if (value > 2) {
                value--
                etValue.text = value.toString()
            }
        }

        btnPlus.setOnClickListener {
            var value = Integer.parseInt(etValue.text.toString())
            value++
            etValue.text = value.toString()
        }
        btnMinusTen.setOnClickListener {
            var value = Integer.valueOf(etValue.text.toString())
            if (value > 11) {
                value -= 10
                etValue.text = value.toString()
            }
        }

        btnPlusTen.setOnClickListener {
            var value = Integer.valueOf(etValue.text.toString())
            value += 10
            etValue.text = value.toString()
        }


        btnTest.setOnClickListener {
            if (isNewMotor) {
                val gpio = selectedGpio
                if (!TextUtils.isEmpty(etValue.text.toString()) && gpio != null) {
                    val newMotor = Motor(Integer.parseInt(etValue.text.toString()), gpio.id)
                    newMotor.gpio = selectedGpio
                    CocktailService.motorTest(this@AddMotorActivity, newMotor)
                } else {
                    showSnackBar("Speed can't be empty")
                }
            } else {
                CocktailService.motorTest(this@AddMotorActivity, currentMotor)
            }
        }
        imgInfo.setOnClickListener { view -> InfoActivity.start(view.context) }
        showButtons(false)
    }

    private fun saveMotor() {
        if (isNewMotor) {
            val gpio = selectedGpio
            if (!TextUtils.isEmpty(etValue.text.toString()) && gpio != null) {
                val motor = Motor(Integer.parseInt(etValue.text.toString()), gpio.id)
                saveMotor(motor)
            } else {
                showSnackBar("Speed can't be empty")
            }
        } else {
            if (!TextUtils.isEmpty(etValue.text.toString())) {
                currentMotor.gpio = selectedGpio
                currentMotor.motorSpeed = Integer.parseInt(etValue.text.toString())
                updateMotor(currentMotor)
            } else {
                showSnackBar("Speed can't be empty")
            }
        }
    }

    private fun saveMotor(motor: Motor) {
//        addSubscription(motorManager.insert(motor).sync().subscribe({ _ -> finish() }))
//        async(CommonPool) {
//            motorManager.insert(motor)
//            finish()
//        }
        launch(UI) {
            async(CommonPool) { motorManager.insert(motor) }.await()
            finish()
        }
    }

    private fun updateMotor(motor: Motor) {
        launch(UI) {
            async(CommonPool) { motorManager.update(motor) }.await()
            updatePosition(motor)
        }
    }

    private fun updatePosition(motor: Motor) {
        launch(UI) {
            val position = async(CommonPool) { positionManager.getByMotorId(motor.id) }.await()
            position.motorId = motor.id
            savePosition(position)
        }
    }

    private fun savePosition(position: Position) {
//        addSubscription(positionManager.update(position).sync().subscribe({ finish() }))
//        async(CommonPool) {
//            positionManager.update(position)
//            finish()
//        }
        launch(UI) {
            async(CommonPool) { positionManager.update(position) }.await()
            finish()
        }
    }


    private fun showButtons(show: Boolean) {
        btnTest.isEnabled = show
        btnSave.isEnabled = show
    }

    private fun initData() {
        spinnerGpio.adapter = SelectGpioAdapter(this, gpioList)
        if (!isNewMotor) {
            etValue.text = currentMotor.motorSpeed.toString()
            val gpio = currentMotor.gpio
            if (gpio != null) {
                spinnerGpio.setSelection(getPosition(gpioList, gpio.gpioPin))
            }
            tvName.text = currentMotor.motorName
            showComponent()
        } else {
            tvName.text = ""
        }
    }

    private fun showComponent() {

        val positionManager = PositionManager()
        launch(UI) {
            val position = async(CommonPool) { positionManager.getByMotorId(motorId) }.await()
            position.component?.let {
                tvComponent.text = it.componentName
            }
//            updatePosition(motor)
        }
//        val componentManager = ComponentManager()
//        componentManager.g
    }

    private fun getPosition(gpioList: List<Gpio>, gpioPin: Long): Int {
        return gpioList.indices.firstOrNull { gpioList[it].gpioPin == gpioPin } ?: 0
    }

    private fun registerBroadcastReceiver() {
        cocktailUpdateBroadcastReceiver = CocktailUpdateBroadcastReceiver(object : OnUpdateListener {
            override fun onItemUpdate(gpioId: Long, progress: Int) {
                // IGNORE
            }

            override fun onItemFinish() {
                blockTestBnt(true)

            }

            override fun onItemStart() {
                blockTestBnt(false)
            }
        })
        cocktailBroadcastReceiver = CocktailBroadcastReceiver(object : OnResultListener {

            override fun onError() {
                blockTestBnt(true)
            }

            override fun onCancel() {
                blockTestBnt(true)
            }
        })
        val intentFilter = IntentFilter(CocktailService.BROADCAST_COCKTAIL_STATUS)
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT)
        registerReceiver(cocktailBroadcastReceiver, intentFilter)

        val intentUpdateFilter = IntentFilter(CocktailService.BROADCAST_COCKTAIL_PROGRESS)
        intentUpdateFilter.addCategory(Intent.CATEGORY_DEFAULT)
        registerReceiver(cocktailUpdateBroadcastReceiver, intentUpdateFilter)
    }

    private fun blockTestBnt(block: Boolean) {
        btnTest.isEnabled = block
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(cocktailBroadcastReceiver)
        unregisterReceiver(cocktailUpdateBroadcastReceiver)
    }

    companion object {

        protected const val EXTRA_MOTOR = "EXTRA_MOTOR"

        fun start(context: Context) {
            val intent = Intent(context, AddMotorActivity::class.java)
            context.startActivity(intent)
        }

        fun start(context: Context, motorId: Long) {
            val intent = Intent(context, AddMotorActivity::class.java)
            intent.putExtra(EXTRA_MOTOR, motorId)
            context.startActivity(intent)
        }
    }

}


