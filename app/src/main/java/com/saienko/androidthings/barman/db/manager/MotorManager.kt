package com.saienko.androidthings.barman.db.manager

import com.saienko.androidthings.barman.db.DatabaseCreator
import com.saienko.androidthings.barman.db.entity.Motor
import com.saienko.androidthings.barman.db.interfaces.IMotorManager

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 10/11/17
 * Time: 01:25
 */

class MotorManager : IMotorManager {

    //    override val all: Single<MutableList<Motor>>
//        get() = DatabaseCreator.getDb().motorDao().all
//                .toObservable()
//                .flatMapIterable { items -> items }
//                .compose(fillMotor())
//                .toList()
    override fun list(): MutableList<Motor> {
        val manager = GpioManager()
        val list = DatabaseCreator.getDb().motorDao().all
        list.forEach {
            it.gpio = manager.get(it.gpioId)
        }
        return list

    }


    override fun insert(item: Motor): Long {
        if (item.gpioId == 0L) {
            val gpioManager = GpioManager()
            var gpio = item.gpio
            if (gpio != null) {
                gpio = gpioManager.getInserted(gpio)
                item.gpio = gpio
            }
        }
        return DatabaseCreator.getDb().motorDao().insert(item)
    }

    override fun update(item: Motor) {
        if (item.gpioId == 0L) {
            val gpioManager = GpioManager()
            var gpio = item.gpio
            if (gpio != null) {
                gpio = gpioManager.getInserted(gpio)
                item.gpio = gpio
            }
        }
        return DatabaseCreator.getDb().motorDao().update(item)
    }

    override suspend fun delete(item: Motor) = DatabaseCreator.getDb().motorDao().delete(item)

    override suspend fun delete(id: Long) = DatabaseCreator.getDb().motorDao().delete(id)

    override suspend fun clear() = DatabaseCreator.getDb().motorDao().clear()

//    override fun getById(itemId: Long): Single<Motor> {
//        return DatabaseCreator.getDb().motorDao().getById(itemId)
//                .toObservable()
//                .compose(fillMotor())
//                .firstOrError()
//    }

    override fun get(itemId: Long): Motor {
        val motor = DatabaseCreator.getDb().motorDao().get(itemId)
        val gpioManager = GpioManager()
        motor.gpio = gpioManager.get(motor.gpioId)
        return motor
    }

//    @Contract(pure = true)
//    private fun fillMotor(): ObservableTransformer<Motor, Motor> {
//        return ObservableTransformer { upstream -> upstream.flatMap { addGpio(it) } }
//    }
//
//    private fun addGpio(motor: Motor): ObservableSource<Motor> {
//        val manager = GpioManager()
//        return manager.getById(motor.gpioId)
//                .toObservable()
//                .map { gpio ->
//                    motor.gpio = gpio
//                    motor
//                }
//    }
}
