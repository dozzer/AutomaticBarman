package com.saienko.androidthings.barman.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.saienko.androidthings.barman.listener.OnResultListener
import com.saienko.androidthings.barman.service.CocktailService
import com.saienko.androidthings.barman.service.CocktailStatus

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/8/17
 * Time: 20:05
 */

class CocktailBroadcastReceiver(private var onResultListener: OnResultListener) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val status = intent.getSerializableExtra(CocktailService.BROADCAST_EXTRA_COCKTAIL_STATUS) as CocktailStatus
        when (status) {
            CocktailStatus.ERROR -> onResultListener.onError()
//            CocktailStatus.START -> onResultListener.onStart()
            CocktailStatus.CANCEL -> onResultListener.onCancel()
//            CocktailStatus.FINISH -> onResultListener.onFinish()
        }
    }
}
