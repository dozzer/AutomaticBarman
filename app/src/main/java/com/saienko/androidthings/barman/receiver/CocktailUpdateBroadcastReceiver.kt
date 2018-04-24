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

        val map: HashMap<Long, Int> = intent.getSerializableExtra(
                CocktailService.BROADCAST_EXTRA_COCKTAIL_ITEM_MAP) as HashMap<Long, Int>
        var start = true
        var finish = true
        map.forEach { gpioId, progress ->

            when (progress) {
                in 1..99 -> {
                    onUpdateListener.onItemUpdate(gpioId, progress)
                    finish = false
                    start = false
                }
                100 -> {
                    onUpdateListener.onItemUpdate(gpioId, progress)
                    start = finish
                }
                0 -> {
                    onUpdateListener.onItemUpdate(gpioId, progress)
                    start = true
                    finish = false
                }
                -1 -> throw UnsupportedOperationException("Unknown action")
                else -> throw UnsupportedOperationException("Unknown action !!!")
            }
        }


        if (start) {
            onUpdateListener.onItemStart()
        }

        if (finish) {
            onUpdateListener.onItemFinish()
        }
    }
}
