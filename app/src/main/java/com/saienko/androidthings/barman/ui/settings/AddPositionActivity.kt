package com.saienko.androidthings.barman.ui.settings

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.Utils
import com.saienko.androidthings.barman.db.entity.Component
import com.saienko.androidthings.barman.db.entity.Position
import com.saienko.androidthings.barman.db.interfaces.IComponentManager
import com.saienko.androidthings.barman.db.interfaces.IPositionManager
import com.saienko.androidthings.barman.db.manager.ComponentManager
import com.saienko.androidthings.barman.db.manager.PositionManager
import com.saienko.androidthings.barman.ui.base.BaseActivity
import com.saienko.androidthings.barman.ui.settings.adapter.SelectComponentAdapter
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.io.IOException

class AddPositionActivity : BaseActivity() {
    private var currentPosition: Position? = null
    private lateinit var componentList: List<Component>
    private var selectedComponent: Component? = null

    private var isNew: Boolean = false
    private var manager: PeripheralManager? = null
    private var gpio: Gpio? = null

    private lateinit var localAdapter: SelectComponentAdapter

    private lateinit var componentManager: IComponentManager
    private lateinit var positionManager: IPositionManager

    private lateinit var spinnerComponent: Spinner
    private lateinit var tvMotor: TextView
    private lateinit var btnSave: Button
    private lateinit var btnPour: Button

    private var positionId: Long = -1
    private var motorId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_position)
        componentManager = ComponentManager()
        positionManager = PositionManager()
        initUI()
        handleIntent()
        if (Utils.isThingsDevice(this)) {
            manager = PeripheralManager.getInstance()
        }
    }

    private fun handleIntent() {
        positionId = intent.getLongExtra(EXTRA_POSITION_ID, positionId)
        isNew = !intent.hasExtra(EXTRA_EDIT)
        if (isNew) {
            motorId = intent.getLongExtra(EXTRA_MOTOR_ID, motorId)
        }
        getPosition(positionId)
    }

    private fun getPosition(positionId: Long) {
        launch(UI) {
            initPosition(async(CommonPool) { positionManager.get(positionId) }.await())
        }
    }

    private fun initPosition(position: Position) {
        if (position.motor != null) {
            tvMotor.text = position.motor!!.motorName
        } else {
            tvMotor.setText(R.string.unknown)
            btnSave.isEnabled = false
            btnPour.isEnabled = false
        }
        currentPosition = position

        launch(UI) {
            val list: MutableList<Component> = async(CommonPool) {
                if (isNew) {
                    componentManager.allFree()
                } else {
                    val list = componentManager.allFree()
                    position.component?.let { list.add(it) }
                    return@async list
                }
            }.await()
            showPosition(list, position)
        }
    }

    private fun showPosition(
            components: MutableList<Component>,
            position: Position) {
        componentList = components
        initAdapter()
        spinnerComponent.adapter = localAdapter
        if (!isNew) {
            spinnerComponent.setSelection(
                    getPosition(componentList, position.component!!.id))
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initUI() {
        super.initUI()
        tvMotor = findViewById(R.id.tvMotor)
        spinnerComponent = findViewById(R.id.spinnerComponent)
        btnSave = findViewById(R.id.btnSave)
        btnPour = findViewById(R.id.btnPour)

        btnPour.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    btnPour.setBackgroundColor(getColor(R.color.colorAccent))
                    startPour(currentPosition)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    btnPour.setBackgroundColor(getColor(R.color.lighter_gray))
                    endPour()
                }
                else -> {
                }
            }
            false
        }

        spinnerComponent.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                selectedComponent = componentList[i]
                if (selectedComponent != null) {
                    btnSave.isEnabled = true
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
                // IGNORE
            }
        }

        btnSave.setOnClickListener {
            if (isNew) {
                currentPosition = Position(selectedComponent!!.id, motorId)
                savePosition(currentPosition!!)
            } else {
                currentPosition!!.component = selectedComponent
                updatePosition(currentPosition!!)
            }
        }
    }

    private fun savePosition(position: Position) {
        launch(UI) {
            async(CommonPool) { positionManager.insert(position) }.await()
            finish()
        }
    }

    private fun updatePosition(position: Position) {
        launch(UI) {
            async(CommonPool) { positionManager.update(position) }.await()
            finish()
        }
    }

    private fun initAdapter() {
        localAdapter = SelectComponentAdapter(this@AddPositionActivity, componentList)
    }

    private fun endPour() {
        if (manager != null && gpio != null) {
            try {
                gpio!!.close()
            } catch (e: IOException) {
                Log.e(TAG, "Error closing LED GPIO", e)
            } finally {
                gpio = null
            }
            gpio = null
        }
    }

    private fun startPour(position: Position?) {
        if (manager != null && gpio == null) {
            try {
                gpio = manager!!.openGpio(position!!.motor!!.gpio!!.gpioName)
                gpio!!.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
            } catch (e: IOException) {
                Log.e(TAG, "startPour: ", e)
            }
        }
    }

    private fun getPosition(components: List<Component>, componentId: Long): Int {
        return components.indices.firstOrNull { components[it].id == componentId }
               ?: -1
    }

    companion object {

        const val EXTRA_POSITION_ID = "EXTRA_POSITION_ID"
        private const val TAG = "AddPositionActivity"
        private const val EXTRA_EDIT = "EXTRA_EDIT"
        private const val EXTRA_MOTOR_ID = "EXTRA_MOTOR_ID"

        fun add(context: Context, positionId: Long, motorId: Long) {
            val intent = Intent(context, AddPositionActivity::class.java)
            intent.putExtra(EXTRA_POSITION_ID, positionId)
            intent.putExtra(EXTRA_MOTOR_ID, motorId)
            context.startActivity(intent)
        }

        fun edit(context: Context, positionId: Long) {
            val intent = Intent(context, AddPositionActivity::class.java)
            intent.putExtra(EXTRA_POSITION_ID, positionId)
            intent.putExtra(EXTRA_EDIT, true)
            context.startActivity(intent)
        }
    }
}

