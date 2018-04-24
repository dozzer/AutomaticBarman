package com.saienko.androidthings.barman.db.interfaces

import com.saienko.androidthings.barman.db.entity.Gpio

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 10/9/17
 * Time: 19:31
 */

interface IGpioManager : IBaseManager<Gpio> {

    fun insertAll(vararg gpios: Gpio)

    fun freeGpio(): MutableList<Gpio>

    fun freeGpio(gpioId: Long): MutableList<Gpio>
}
