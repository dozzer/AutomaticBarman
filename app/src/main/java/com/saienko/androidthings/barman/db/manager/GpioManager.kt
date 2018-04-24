package com.saienko.androidthings.barman.db.manager

import com.saienko.androidthings.barman.db.DatabaseCreator
import com.saienko.androidthings.barman.db.entity.Gpio
import com.saienko.androidthings.barman.db.interfaces.IGpioManager

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 10/11/17
 * Time: 10:48
 */

class GpioManager : IGpioManager {

    override fun freeGpio(): MutableList<Gpio> {
        val motorList = DatabaseCreator.getDb().motorDao().all
        val gpioList = list()
        val motorGpioIts = motorList.map {
            it.gpioId
        }
        val out = gpioList.filter {
            val gpio = it
            motorGpioIts.forEach {
                if (it == gpio.id) {
                    return@filter false
                }
            }
            return@filter true
        }
        return out.toMutableList()
    }

    override fun freeGpio(gpioId: Long): MutableList<Gpio> {
        val motorList = DatabaseCreator.getDb().motorDao().all
        val gpioList = list()
        val motorGpioIts = motorList.map {
            it.gpioId
        }
        val out = gpioList.filter {
            val gpio = it
            motorGpioIts.forEach {
                if (it == gpio.id && gpioId != it) {
                    return@filter false
                }
            }
            return@filter true
        }
        return out.toMutableList()
    }

    override fun list(): MutableList<Gpio> = DatabaseCreator.getDb().gpioDao().all
    override suspend fun delete(id: Long) = DatabaseCreator.getDb().gpioDao().delete(id)
    fun getInserted(item: Gpio) = get(insert(item))
    override fun insert(item: Gpio): Long = DatabaseCreator.getDb().gpioDao().insert(item)
    override fun get(itemId: Long) = DatabaseCreator.getDb().gpioDao().get(itemId)
    override fun insertAll(vararg gpios: Gpio) = DatabaseCreator.getDb().gpioDao().insert(*gpios)
    override suspend fun delete(item: Gpio) = DatabaseCreator.getDb().gpioDao().delete(item)
    override suspend fun clear() = DatabaseCreator.getDb().gpioDao().clear()
    override fun update(item: Gpio) = DatabaseCreator.getDb().gpioDao().update(item)
}
