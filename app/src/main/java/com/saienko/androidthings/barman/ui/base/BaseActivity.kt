package com.saienko.androidthings.barman.ui.base

import android.support.v7.app.AppCompatActivity
import android.view.View
import com.saienko.androidthings.barman.R

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 15:42
 */

abstract class BaseActivity : AppCompatActivity() {

    //dispatches execution onto the Android main UI thread
//    val uiContext: CoroutineContext = UI

    //represents a common pool of shared threads as the coroutine dispatcher
//    private val bgContext: CoroutineContext = CommonPool


    private var rootView: View? = null

//    private lateinit var subscriptions: CompositeDisposable

//    public override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        subscriptions = CompositeDisposable()
//    }

    open fun initUI() {
        rootView = findViewById(R.id.root)
        if (rootView == null) {
            throw IllegalStateException("root view can't be empty")
        }
    }

//    public override fun onDestroy() {
//        super.onDestroy()
//        closeSubscriptions()
//    }

//    private fun closeSubscriptions() {
//        if (subscriptions.size() > 0 && !subscriptions.isDisposed) {
//            subscriptions.clear()
//        }
//    }

    fun showSnackBar(message: String) {
        SnackBarImpl.showSnackBar(rootView, message)
    }

    fun showErrorSnackBar(message: String) {
        SnackBarImpl.showErrorSnackBar(rootView, message)
    }

    fun showErrorSnackBar(throwable: Throwable) {
        SnackBarImpl.showErrorSnackBar(rootView, throwable)
    }

//    fun addSubscription(subscription: Disposable) {
//        if (!subscription.isDisposed) {
//            subscriptions.add(subscription)
//        }
//    }
}
