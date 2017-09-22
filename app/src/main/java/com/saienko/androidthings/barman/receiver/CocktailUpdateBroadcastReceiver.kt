package com.saienko.androidthings.barman.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.saienko.androidthings.barman.listener.OnUpdateListener
import com.saienko.androidthings.barman.service.CocktailService

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/8/17
 * Time: 20:05
 */

class CocktailUpdateBroadcastReceiver(private val onUpdateListener: OnUpdateListener) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val gpioId = intent.getLongExtra(CocktailService.BROADCAST_EXTRA_COCKTAIL_ITEM_GPIO_ID, UNKNOWN_VALUE_LONG)
        val progress = intent.getIntExtra(CocktailService.BROADCAST_EXTRA_COCKTAIL_ITEM_PROGRESS, UNKNOWN_VALUE)

//        if (order == -1) {
//            throw UnsupportedOperationException("Unknown action")
//        }

        when (progress) {
            in 1..99 -> onUpdateListener.onItemUpdate(gpioId, progress)
            100 -> {
                onUpdateListener.onItemFinish(gpioId)
                onUpdateListener.onItemUpdate(gpioId, progress)
            }
            0 -> {
                onUpdateListener.onItemStart(gpioId)
                onUpdateListener.onItemUpdate(gpioId, progress)
            }
            -1 -> throw UnsupportedOperationException("Unknown action")
            else -> throw UnsupportedOperationException("Unknown action !!!")
        }
    }

    companion object {

        private val UNKNOWN_VALUE_LONG = -1L
        private val UNKNOWN_VALUE = -1
    }
}
