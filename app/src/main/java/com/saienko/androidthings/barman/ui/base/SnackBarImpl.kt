package com.saienko.androidthings.barman.ui.base

import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.View
import android.widget.TextView
import org.jetbrains.annotations.NonNls


/**
 * Created
 * User: Vasiliy Saienko
 * Date: 4/24/17
 * Time: 11:52
 */

object SnackBarImpl {

    @NonNls
    private val TAG = "SnackBarImpl"

    fun showSnackBar(view: View?, message: String?) {
        if (view != null && message != null) {
            val snackBar = Snackbar.make(view, fromHtml(message), Snackbar.LENGTH_LONG)
            val snackBarView = snackBar.view
            val textView = snackBarView.findViewById<TextView>(android.support.design.R.id.snackbar_text)
            textView.maxLines = 5
            snackBar.show()
        } else {
            throw NullPointerException("view == $view. message == $message")
        }
        Log.d(TAG, "showDelayedSnackBar: $message")
    }

//    internal fun showDelayedSnackBar(view: View?, message: String?) {
//        if (view != null && message != null) {
//            val snackBar = Snackbar.make(view, fromHtml(message), Snackbar.LENGTH_LONG)
//            snackBar.duration = 10000
//            val snackBarView = snackBar.view
//            val textView = snackBarView.findViewById<TextView>(android.support.design.R.id.snackbar_text)
//            textView.maxLines = 5
//            snackBar.show()
//        } else {
//            throw NullPointerException("view == $view. message == $message")
//        }
//        Log.d(TAG, "showDelayedSnackBar: " + message)
//    }

    internal fun showErrorSnackBar(view: View?, message: String?) {
        if (view != null && message != null) {
            try {
                val snackBar = Snackbar.make(view, fromHtml(message), Snackbar.LENGTH_LONG)
                snackBar.duration = 5000
                val snackBarView = snackBar.view
                snackBarView.setBackgroundColor(
                        ContextCompat.getColor(view.context, android.R.color.holo_red_light))
                val textView = snackBarView.findViewById<TextView>(android.support.design.R.id.snackbar_text)
                textView.maxLines = 5
                textView.setOnClickListener { snackBar.dismiss() }
                snackBar.show()
            } catch (e: IllegalArgumentException) {
                Log.e(TAG, "showSnackBar: ", e)
            }

        } else {
            throw NullPointerException("view == $view. message == $message")
        }
        Log.e(TAG, "showDelayedSnackBar: $message")
    }

//    fun showSnackBar(view: View?, message: String?, btnText: String?,
//                     snackBarOnClickListener: View.OnClickListener?) {
//        if (view != null && message != null) {
//            try {
//                val snackBar = Snackbar.make(view, fromHtml(message), Snackbar.LENGTH_LONG)
//                snackBar.duration = 10000
//                snackBar.setAction(btnText, snackBarOnClickListener)
//                snackBar.setActionTextColor(ContextCompat.getColor(view.context, R.color.colorWhite))
//                val snackBarView = snackBar.view
//                snackBarView.setBackgroundColor(
//                        ContextCompat.getColor(view.context, android.R.color.holo_red_light))
//                val textView = snackBarView.findViewById<TextView>(android.support.design.R.id.snackbar_text)
//                textView.maxLines = 5
//                snackBar.show()
//            } catch (e: IllegalArgumentException) {
//                Log.e(TAG, "showSnackBar: ", e)
//            }
//        } else {
//            throw NullPointerException("view == $view. message == $message")
//        }
//    }

    fun showErrorSnackBar(view: View?, throwable: Throwable?) {
        Log.e(TAG, "onError: ", throwable)
        showErrorSnackBar(view, throwable.toString())
    }

    private fun fromHtml(source: String): Spanned {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//        Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)
//        } else {
//            Html.fromHtml(source)
//        }
        return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)
    }
}
