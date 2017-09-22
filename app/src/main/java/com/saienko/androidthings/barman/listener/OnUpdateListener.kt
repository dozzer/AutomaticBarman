package com.saienko.androidthings.barman.listener

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/8/17
 * Time: 17:16
 */

interface OnUpdateListener {

    fun onItemUpdate(gpioId: Long, progress: Int)

    fun onItemFinish(gpioId: Long)

    fun onItemStart(gpioId: Long)

}
