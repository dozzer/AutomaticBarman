package com.saienko.androidthings.barman.ui.base;

import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.saienko.androidthings.barman.R;
import org.jetbrains.annotations.NonNls;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 4/24/17
 * Time: 11:52
 */

public class SnackBarImpl {

    @NonNls private static final String TAG = "SnackBarImpl";

    public static void showSnackBar(View view, String message) {
        Snackbar snackbar     = Snackbar.make(view, Html.fromHtml(message), Snackbar.LENGTH_LONG);
        View     snackbarView = snackbar.getView();
        TextView textView     = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(5);
        snackbar.show();
        Log.d(TAG, "showDelayedSnackBar: " + message);
    }

    static void showDelayedSnackBar(@Nullable View view, String message) {
        if (view != null) {
            Snackbar snackbar = Snackbar.make(view, Html.fromHtml(message), Snackbar.LENGTH_LONG);
            snackbar.setDuration(10000);
            View     snackbarView = snackbar.getView();
            TextView textView     = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setMaxLines(5);
            snackbar.show();
        }
        Log.d(TAG, "showDelayedSnackBar: " + message);
    }

    static void showErrorSnackBar(@Nullable View view, String message) {
        if (view != null) {
            try {
                Snackbar snackbar = Snackbar.make(view, Html.fromHtml(message), Snackbar.LENGTH_LONG);
                snackbar.setDuration(5000);
                View snackbarView = snackbar.getView();
                snackbarView.setBackgroundColor(
                        ContextCompat.getColor(view.getContext(), android.R.color.holo_red_light));
                TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setMaxLines(5);
                textView.setOnClickListener(v -> snackbar.dismiss());
                snackbar.show();
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "showSnackBar: ", e);
            }

        }
        Log.e(TAG, "showDelayedSnackBar: " + message);
    }

    public static void showSnackBar(@Nullable View view, String message, String btnText,
                                    View.OnClickListener snackbarOnClickListener) {
        if (view != null) {
            try {
                Snackbar snackbar = Snackbar.make(view, Html.fromHtml(message), Snackbar.LENGTH_LONG);
                snackbar.setDuration(10000);
                snackbar.setAction(btnText, snackbarOnClickListener);
                snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), R.color.colorWhite));
                View snackbarView = snackbar.getView();
                snackbarView.setBackgroundColor(
                        ContextCompat.getColor(view.getContext(), android.R.color.holo_red_light));
                TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setMaxLines(5);
                snackbar.show();
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "showSnackBar: ", e);
            }

        }
    }

    public static void showErrorSnackBar(View view, Throwable throwable) {
        Log.e(TAG, "onError: ", throwable);
        showErrorSnackBar(view, throwable.toString());
    }
}
