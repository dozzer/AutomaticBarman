package com.saienko.androidthings.barman.ui.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.*
import com.saienko.androidthings.barman.db.manager.*
import com.saienko.androidthings.barman.ui.base.BaseActivity
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.util.*

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initUI()
    }

    override fun initUI() {
        super.initUI()
        findViewById<Button>(R.id.btnMotors).setOnClickListener {
            startActivity(Intent(applicationContext, SettingsMotorActivity::class.java))
        }

        findViewById<Button>(R.id.btnGpio).setOnClickListener {
            startActivity(Intent(applicationContext, SettingsGpioActivity::class.java))
        }

        findViewById<Button>(R.id.btnCocktails).setOnClickListener {
            SettingsCocktailsActivity.start(this)
        }

        findViewById<Button>(R.id.btnComponent).setOnClickListener {
            startActivity(Intent(applicationContext, SettingsComponentActivity::class.java))
        }

        findViewById<Button>(R.id.btnPositions).setOnClickListener {
            startActivity(Intent(applicationContext, SettingsPositionsActivity::class.java))
        }

        val gpioManager = GpioManager()
        launch(UI) {
            findViewById<Button>(R.id.btnPreload).visibility = async(
                    CommonPool) { if (gpioManager.list().size > 0) View.GONE else View.VISIBLE }.await()
            findViewById<Button>(R.id.startTest).visibility = async(
                    CommonPool) { if (gpioManager.list().size > 0) View.VISIBLE else View.GONE }.await()
            findViewById<Button>(R.id.massTest).visibility = async(
                    CommonPool) { if (gpioManager.list().size > 0) View.VISIBLE else View.GONE }.await()
        }

        findViewById<Button>(R.id.btnPreload).setOnClickListener {
            btnPreload()
        }
        findViewById<Button>(R.id.startTest).setOnClickListener {
            startTest()
        }
        findViewById<Button>(R.id.massTest).setOnClickListener {
            massTest()
        }
    }

    private fun btnPreload() {
        print("Preload start")
        launch(UI) {
            async(CommonPool) { loadGpios() }.await()
            async(CommonPool) { loadMotors() }.await()
            async(CommonPool) { loadComponents() }.await()
            async(CommonPool) { loadPositions() }.await()
            async(CommonPool) { loadCocktails() }.await()
            print("Preload finish")
            findViewById<Button>(R.id.btnPreload).visibility = View.GONE
            findViewById<Button>(R.id.startTest).visibility = View.VISIBLE
        }
    }

    private fun loadCocktails() {
        val cocktailManager = CocktailManager()
        val componentManager = ComponentManager()
        addCocktail(cocktailManager, componentManager, "Whiskey Cola", listOf(Pair("Cola", 200), Pair("Whiskey", 100)))
        addCocktail(cocktailManager, componentManager, "Whiskey Cola with Juice",
                    listOf(Pair("Cola", 200), Pair("Whiskey", 90), Pair("Lime juice", 10)))
        addCocktail(cocktailManager, componentManager, "Spritz",
                    listOf(Pair("Sparkling wine", 150), Pair("Aperol", 100), Pair("Sparkling water", 50)))
        addCocktail(cocktailManager, componentManager, "test :)",
                    listOf(Pair("Sparkling wine", 120), Pair("Aperol", 100), Pair("Sparkling water", 80),
                           Pair("Whiskey", 60)))
    }

    private fun addCocktail(cocktailManager: CocktailManager, componentManager: ComponentManager, cocktailName: String,
                            listOf: List<Pair<String, Int>>) {
        val componentList = componentManager.list()
        val cocktail = cocktailManager.get(cocktailManager.insert(Cocktail(cocktailName)))
        val cocktailElementList = mutableListOf<CocktailElement>()//MutableList<CocktailElement>(listOf.size)
        listOf.forEach {
            val element = CocktailElement(getComponentId(componentList, it.first), cocktail.id, it.second)
            cocktailElementList.add(element)
        }
        cocktailManager.update(cocktail, cocktailElementList.toList() as ArrayList<CocktailElement>)
    }

    private fun getComponentId(componentList: MutableList<Component>, componentName: String): Long {
        componentList.forEach {
            if (it.componentName == componentName) {
                return it.id
            }
        }
        return -1
    }

    private fun loadPositions() {
        val components = ComponentManager().list()
        val motors = MotorManager().list()
        val positionManger = PositionManager()
        motors.forEachIndexed { index, motor ->
            positionManger.insert(Position(components[index].id, motor.id))
        }
    }

    private fun loadComponents() {
        val componentManager = ComponentManager()
        Component.getRealComponents().forEach {
            componentManager.insert(it)
        }
    }

    private fun loadMotors() {
        val motorManager = MotorManager()
        val gpioManager = GpioManager()
        val list = gpioManager.list()
        getRealMotors(list).forEach {
            motorManager.insert(it)
        }
    }

    private fun getRealMotors(list: MutableList<Gpio>): ArrayList<Motor> {
        val out = ArrayList<Motor>()

//        out.add(getMotor(100, getGpio("GPIO2_IO02", list)))
//        out.add(getMotor(100, getGpio("GPIO2_IO00", list)))
//        out.add(getMotor(100, getGpio("GPIO2_IO07", list)))

//        out.add(getMotor(100, getGpio("GPIO2_IO05", list)))

        out.add(getMotor(100, getGpio("GPIO6_IO13", list)))
        out.add(getMotor(100, getGpio("GPIO6_IO12", list)))

        out.add(getMotor(100, getGpio("GPIO6_IO15", list)))
        out.add(getMotor(100, getGpio("GPIO6_IO14", list)))

        return out
    }

    private fun getMotor(speed: Int, gpio: Gpio?): Motor {
        val motor = Motor(speed, gpio!!.id)
        motor.gpio = gpio
        return motor
    }

    private fun getGpio(gpioName: String,
                        list: MutableList<Gpio>): Gpio? {
        list.forEach {
            if (it.gpioName == gpioName) {
                return it
            }
        }
        return null
    }


    private fun loadGpios() {
        val gpioManager = GpioManager()
        Gpio.getRealGpios().forEach {
            gpioManager.insert(it)
        }
    }

    private fun startTest() {
        val launchIntent = packageManager.getLaunchIntentForPackage("com.saenko.pumppower")
        if (launchIntent != null) {
            startActivity(launchIntent)
        }
    }

    private fun massTest() {
        val launchIntent = packageManager.getLaunchIntentForPackage("com.saenko.testioportsvoltage")
        if (launchIntent != null) {
            startActivity(launchIntent)
        }
    }

    companion object {

        fun start(activity: Activity) {
            val intent = Intent(activity, SettingsActivity::class.java)
            activity.startActivity(intent)
        }
    }
}
