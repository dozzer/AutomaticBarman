package com.saienko.androidthings.barman.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import com.saienko.androidthings.barman.R.string.motor
import com.saienko.androidthings.barman.Utils
import com.saienko.androidthings.barman.db.entity.Motor
import com.saienko.androidthings.barman.db.manager.CocktailManager
import com.saienko.androidthings.barman.db.manager.GpioManager
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.cancelAndJoin
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class CocktailService : IntentService(TAG) {

    private var progressMap: HashMap<Long, Int>? = null
    private var threadList: ArrayList<Job> = ArrayList()
    private var gpioList: ArrayList<Gpio> = ArrayList()


    override fun onHandleIntent(intent: Intent?) {
        if (intent != null && intent.action != null) {
            val action = intent.action
            when (action) {
                ACTION_COCKTAIL_CREATE -> {
                    val cocktailId = intent.getLongExtra(EXTRA_COCKTAIL_ID, -1)
                    handleActionCocktail(cocktailId)
                }
                ACTION_COCKTAIL_CANCEL -> {
                    Log.d(TAG, "onHandleIntent: cancel")
                    sendCocktailStatus(CocktailStatus.CANCEL)
                    launch { stopTask() }
                    Thread.currentThread().interrupt()
                    stopSelf()
                }
                ACTION_MOTOR_TEST -> {
                    val motor: Motor = intent.getParcelableExtra(EXTRA_MOTOR)
                    val motorSpeed = intent.getIntExtra(EXTRA_GPIO_SPEED, 1)
                    val gpioManager = GpioManager()
                    pour(motor.gpioId, gpioManager.get(motor.gpioId).gpioName, motorSpeed, 10, 1.0)
                    Log.i(TAG, "onHandleIntent: motorTest")
                }
                ACTION_MOTOR_CLEAR -> {
                    val motorList: List<Motor> = intent.getParcelableArrayListExtra(EXTRA_MOTOR_LIST)
                    val clearType = intent.getSerializableExtra(EXTRA_MOTOR_CLEAR_TYPE) as CleanType
                    val gpioManager = GpioManager()
                    motorList.forEach {
                        if (it.gpio == null) {
                            it.gpio = gpioManager.get(it.gpioId)
                        }
                    }
                    clear(motorList, clearType)
                }
                else -> throw UnsupportedOperationException("Unknown action")
            }
        } else {
            throw UnsupportedOperationException("Unknown action")
        }
    }

    private suspend fun stopTask() {
        if (isTaskRun) {
            taskShouldBeStopped = true
        }

        gpioList.forEach {
            it.close()
        }

        for (job in threadList) {
            job.cancelAndJoin()
        }
        threadList = ArrayList()
    }

    private fun handleActionCocktail(cocktailId: Long) {
        taskShouldBeStopped = false
        isTaskRun = true
        val cocktailManager = CocktailManager()

        val cocktail = cocktailManager.get(cocktailId)
        if (cocktail.cocktailElements != null) {
            for (cocktailElement in cocktail.cocktailElements!!) {
//                if (cocktailElement.position != null && cocktailElement.position!!.motor != null) {
//                    putToMap(cocktailElement.position!!.motor!!.gpioId, 0)
//                    cocktailElement.position!!.motor?.let {
//                        pour(it, cocktailElement.volume, cocktailElement.component.coefficient)
//                    }
//                }
                val component = cocktailElement.component
                cocktailElement.position?.let {
                    //                    if (component == null){
//                        val componentManager = ComponentManager()
//                        component = componentManager.get()
//                    }
                    putToMap(it.motor!!.gpioId, 0)
                    it.motor?.let {
                        pour(it, cocktailElement.volume, component?.coefficient ?: 1.0)
                    }
                }
            }
        }
    }

    private fun putToMap(gpioId: Long, progress: Int) {
        if (progressMap == null) {
            progressMap = HashMap()
        }
        progressMap!![gpioId] = progress
    }

    private fun sendCocktailItemProgress(gpioId: Long, progress: Int) {
        putToMap(gpioId, progress)

        val intentUpdate = Intent()
        intentUpdate.action = BROADCAST_COCKTAIL_PROGRESS
        intentUpdate.addCategory(Intent.CATEGORY_DEFAULT)
        intentUpdate.putExtra(BROADCAST_EXTRA_COCKTAIL_ITEM_MAP, progressMap)
        sendBroadcast(intentUpdate)
    }

    private fun sendCocktailStatus(status: CocktailStatus) {
        val intentUpdate = Intent()
        intentUpdate.action = BROADCAST_COCKTAIL_STATUS
        intentUpdate.addCategory(Intent.CATEGORY_DEFAULT)
        intentUpdate.putExtra(BROADCAST_EXTRA_COCKTAIL_STATUS, status)
        sendBroadcast(intentUpdate)
    }

    private fun clear(motorList: List<Motor>, clearType: CleanType) {
        Log.d(TAG, "clear motors: $motorList as ${clearType.name}")
        val volume: Int = when (clearType) {
            CleanType.LONG -> 500
            CleanType.SHORT -> 10
        }
        for (motor in motorList) {
            pour(motor, volume, 1.0)
        }
    }

    private fun startRealPourThread(millis: Int, gpioName: String) {
        lateinit var gpio: Gpio
        val job = launch {
            val manager: PeripheralManager
            if (Utils.isThingsDevice(applicationContext)) {
                manager = PeripheralManager.getInstance()
                try {
                    gpio = manager.openGpio(gpioName)
                    gpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
                    gpio.setActiveType(Gpio.ACTIVE_HIGH)
                    gpio.value = true
                    gpioList.add(gpio)
                } catch (e: IOException) {
                    Log.e(TAG, "startRealPourThread: ", e)
                }
            }
            Log.d(TAG, "startRealPourThread start")
            delay(millis.toLong(), TimeUnit.MILLISECONDS)
            if (Utils.isThingsDevice(applicationContext)) {
                try {
                    gpio.setActiveType(Gpio.ACTIVE_LOW)
                    gpio.value = true
                    gpio.close()
                } catch (e: IOException) {
                    Log.e(TAG, "Error closing LED GPIO", e)
                }
            }
            Log.d(TAG, "startRealPourThread finish")
        }
        threadList.add(job)
//        val disposable = Completable
//                .fromAction {
//                    val manager: PeripheralManager
//                    if (Utils.isThingsDevice(applicationContext)) {
//                        manager = PeripheralManager.getInstance()
//                        try {
//                            gpio = manager.openGpio(gpioName)
//                            gpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
//                            gpio.setActiveType(Gpio.ACTIVE_HIGH)
//                            gpio.value = true
//                        } catch (e: IOException) {
//                            Log.e(TAG, "startRealPourThread: ", e)
//                        }
//                    }
//                    Log.d(TAG, "startRealPourThread start")
//                }
//                .subscribeOn(Schedulers.io())
//                .delay(millis.toLong(), TimeUnit.MILLISECONDS)
//                .subscribe({
//                               if (Utils.isThingsDevice(applicationContext)) {
//                                   try {
//                                       gpio.setActiveType(Gpio.ACTIVE_LOW)
//                                       gpio.value = true
//                                       gpio.close()
//                                   } catch (e: IOException) {
//                                       Log.e(TAG, "Error closing LED GPIO", e)
//                                   }
//                               }
//                               Log.d(TAG, "startRealPourThread finish")
//                           }, { throwable ->
//                               Log.e(TAG, throwable.localizedMessage)
//                               Thread.currentThread().interrupt()
//                           })
//        threadList.add(disposable)
    }

    // TODO сделть возможность нливть по очереди

    private fun startProgressThread(step: Int, gpioId: Long) {
        val thread = Thread {

            //            val startTime = System.nanoTime()
            for (i in 0..100) {
                try {
                    Thread.sleep(step.toLong())
                    if (taskShouldBeStopped) {
                        Thread.currentThread().interrupt()
                        break
                    }
                } catch (e: InterruptedException) {
                    Log.e(TAG, "pourDemo: ", e)
                    Thread.currentThread().interrupt()
                }

                sendCocktailItemProgress(gpioId, i)
            }
//            val endTime = System.nanoTime()
//            val duration = endTime - startTime
//            Log.d(TAG, "startProgressThread: time $duration GPIO $gpioId")
        }
        thread.start()
    }

    private fun pour(motor: Motor, ml: Int, coefficient: Double) {
        val gpio = motor.gpio
        if (gpio != null) {
            pour(gpio.id, gpio.gpioName, motor.motorSpeed, ml, coefficient)
        } else {
            Log.e(TAG, "pour. gpio == null. motor = $motor")
        }
    }

    private fun pour(gpioId: Long, gpioName: String, motorSpeed: Int, ml: Int, coefficient: Double) {
        if (taskShouldBeStopped) {
            return
        }
        Log.d(TAG, "pour() called with: motor = [" + motor.toString() + "], ml = [" + ml + "]")
        val millis = ml * motorSpeed
        val step: Int = ((millis / 100) * coefficient).toInt()

        Log.d(TAG, "pour: millis = [$millis], step= [$step]")

        Log.d(TAG, "pour: start sleep")
        startProgressThread(step, gpioId)
        startRealPourThread(millis, gpioName)
    }

    companion object {

        const val BROADCAST_COCKTAIL_PROGRESS = "BROADCAST_COCKTAIL_PROGRESS"
        const val BROADCAST_COCKTAIL_STATUS = "BROADCAST_COCKTAIL_STATUS"
        const val BROADCAST_EXTRA_COCKTAIL_STATUS = "BROADCAST_EXTRA_COCKTAIL_STATUS"
        const val BROADCAST_EXTRA_COCKTAIL_ITEM_MAP = "BROADCAST_EXTRA_COCKTAIL_ITEM_MAP"
        private const val TAG = "CocktailService"
        private const val ACTION_COCKTAIL_CREATE = "ACTION_COCKTAIL_CREATE"
        private const val ACTION_COCKTAIL_CANCEL = "ACTION_COCKTAIL_CANCEL"
        private const val ACTION_MOTOR_TEST = "ACTION_MOTOR_TEST"
        private const val ACTION_MOTOR_CLEAR = "ACTION_MOTOR_CLEAR"
        private const val EXTRA_COCKTAIL_ID = "EXTRA_COCKTAIL"
        private const val EXTRA_MOTOR = "EXTRA_MOTOR"
        //        private const val EXTRA_MOTOR_ID = "EXTRA_MOTOR_ID"
//        private const val EXTRA_GPIO_ID = "EXTRA_GPIO_ID"
//        private const val EXTRA_GPIO_NAME = "EXTRA_GPIO_NAME"
        private const val EXTRA_GPIO_SPEED = "EXTRA_GPIO_SPEED"
        private const val EXTRA_MOTOR_LIST = "EXTRA_MOTOR_LIST"
        private const val EXTRA_MOTOR_CLEAR_TYPE = "EXTRA_MOTOR_CLEAR_TYPE"

        private var isTaskRun = false
        private var taskShouldBeStopped = false

        fun startCocktail(context: Context, cocktailId: Long) {
            val intent = Intent(context, CocktailService::class.java)
            intent.action = ACTION_COCKTAIL_CREATE
            intent.putExtra(EXTRA_COCKTAIL_ID, cocktailId)
            context.startService(intent)
        }

        fun stopCocktail(context: Context) {
            val intent = Intent(context, CocktailService::class.java)
            intent.action = ACTION_COCKTAIL_CANCEL
            context.startService(intent)
        }

        fun motorTest(context: Context, motor: Motor) {
            val intent = Intent(context, CocktailService::class.java)
            intent.action = ACTION_MOTOR_TEST
            intent.putExtra(EXTRA_MOTOR, motor)
//            intent.putExtra(EXTRA_GPIO_ID, motoror.gpioId)
//            intent.putExtra(EXTRA_GPIO_NAME, motor.gpio!!.gpioName)
            intent.putExtra(EXTRA_GPIO_SPEED, motor.motorSpeed)
            context.startService(intent)
        }


        fun clearShortMotors(context: Context, motorList: List<Motor>) {
            cleanMotors(context, motorList as java.util.ArrayList<Motor>, CleanType.SHORT)
        }

        fun clearLongMotors(context: Context, motorList: List<Motor>) {
            cleanMotors(context, motorList as java.util.ArrayList<Motor>, CleanType.LONG)
        }

        private fun cleanMotors(context: Context, motorList: ArrayList<Motor>, type: CleanType) {
            val intent = Intent(context, CocktailService::class.java)
            intent.action = ACTION_MOTOR_CLEAR
            intent.putExtra(EXTRA_MOTOR_LIST, motorList)
            intent.putExtra(EXTRA_MOTOR_CLEAR_TYPE, type)
            context.startService(intent)
        }
    }
}
