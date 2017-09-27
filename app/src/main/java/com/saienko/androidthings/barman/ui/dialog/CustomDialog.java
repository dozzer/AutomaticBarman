package com.saienko.androidthings.barman.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.cocktail.CocktailElement;
import com.saienko.androidthings.barman.db.cocktail.Component;
import com.saienko.androidthings.barman.db.drinkGroup.CocktailGroup;

import java.util.List;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 16:39
 */

public class CustomDialog {

    public static AlertDialog showDialog(Context context, String title, String message,
                                         final DialogInterface.OnClickListener positiveListener,
                                         final DialogInterface.OnClickListener negativeListener) {

        final AlertDialog ad = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, positiveListener)
                .setNegativeButton(android.R.string.cancel, negativeListener)
                .create();
        ad.show();
        ad.setCanceledOnTouchOutside(false);
        ad.setCancelable(false);
        return ad;
    }

    public static AlertDialog showDialog(Context context, String title, String message,
                                         final DialogInterface.OnClickListener positiveListener) {

        final AlertDialog ad = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, positiveListener)
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> {})
                .create();
        ad.show();
        ad.setCanceledOnTouchOutside(false);
        ad.setCancelable(false);
        return ad;
    }

    public static AlertDialog addNewItem(Context context, String title, String message,
                                         String text,
                                         final OnSaveTextListener callback) {
        final EditText edittext = new EditText(new ContextThemeWrapper(context, R.style.EditText_Dialog), null, 0);
        edittext.setText(text);
        final AlertDialog ad = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Save", (dialog, which) -> callback.onTextSaved(edittext.getText().toString()))
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> {})
                .setView(edittext)
                .create();
        ad.show();
        return ad;
    }

    public static AlertDialog addNewDrink(Context context, String title, String message,
                                          List<CocktailGroup> list,
                                          String text,
                                          String positiveText,
                                          final OnSaveTextListener callback) {
        final EditText edittext =
                new EditText(new ContextThemeWrapper(context, R.style.EditText_Dialog), null, 0);
        DrinkGroupDialogAdapter adapter = new DrinkGroupDialogAdapter(context, list);
        edittext.setText(text);
        final AlertDialog ad = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setAdapter(adapter, null)
                .setPositiveButton(positiveText, (dialog, which) -> callback.onTextSaved(edittext.getText().toString()))
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> {})
                .setView(edittext)
                .create();
        ad.show();
        return ad;
    }

    public static void editCocktailElement(Activity activity, CocktailElement cocktailElement,
                                           final OnAddComponentListener callback) {
        int            volume   = cocktailElement.getVolume();
        LayoutInflater inflater = activity.getLayoutInflater();

        AlertDialog.Builder builder    = new AlertDialog.Builder(activity);
        View                view       = inflater.inflate(R.layout.dialog_edit_component, null);
        ImageButton         ibMinus    = view.findViewById(R.id.ibMinus);
        ImageButton         ibPlus     = view.findViewById(R.id.ibPlus);
        ImageButton         ibMinusTen = view.findViewById(R.id.ibMinusTen);
        ImageButton         ibPlusTen  = view.findViewById(R.id.ibPlusTen);
        EditText            etVolume   = view.findViewById(R.id.etVolume);
        etVolume.setText(String.valueOf(volume));
        ibMinus.setOnClickListener(view12 -> {
            int value = Integer.valueOf(etVolume.getText().toString());
            if (value > 2) {
                value--;
                etVolume.setText(String.valueOf(value));
            }
        });
        ibMinusTen.setOnClickListener(view13 -> {
            int value = Integer.valueOf(etVolume.getText().toString());
            if (value > 11) {
                value = volume - 10;
                etVolume.setText(String.valueOf(value));
            }
        });


        ibPlus.setOnClickListener(view1 -> {
            int value = Integer.valueOf(etVolume.getText().toString());
            value++;
            etVolume.setText(String.valueOf(value));
        });
        ibPlusTen.setOnClickListener(view1 -> {
            int value = Integer.valueOf(etVolume.getText().toString());
            value = value + 10;
            etVolume.setText(String.valueOf(value));
        });

        builder.setView(view)
               .setTitle("Edit cocktail Component")
               .setPositiveButton("Save",
                                  (dialog, which) -> callback
                                          .onComponentEdit(cocktailElement,
                                                           Integer.valueOf(etVolume.getText().toString())))
               .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> {})
               .create();
        builder.show();
    }

    public static void addNewCocktailElement(Activity activity,
                                             List<Component> list,
                                             final OnAddComponentListener callback) {

        int                    volume   = 50;
        ComponentDialogAdapter adapter  = new ComponentDialogAdapter(activity, list);
        LayoutInflater         inflater = activity.getLayoutInflater();

        AlertDialog.Builder builder          = new AlertDialog.Builder(activity);
        View                view             = inflater.inflate(R.layout.dialog_add_component, null);
        Spinner             componentSpinner = view.findViewById(R.id.componentSpinner);
        ImageButton         ibMinus          = view.findViewById(R.id.ibMinus);
        ImageButton         ibPlus           = view.findViewById(R.id.ibPlus);
        ImageButton         ibMinusTen       = view.findViewById(R.id.ibMinusTen);
        ImageButton         ibPlusTen        = view.findViewById(R.id.ibPlusTen);

        EditText          etVolume          = view.findViewById(R.id.etVolume);
        final Component[] selectedComponent = new Component[1];
        componentSpinner.setAdapter(adapter);
        componentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedComponent[0] = list.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // IGNORE
            }
        });
        ibMinus.setOnClickListener(view12 -> {
            int value = Integer.valueOf(etVolume.getText().toString());
            if (value > 2) {
                value--;
                etVolume.setText(String.valueOf(value));
            }
        });
        ibMinusTen.setOnClickListener(view13 -> {
            int value = Integer.valueOf(etVolume.getText().toString());
            if (value > 11) {
                value = volume - 10;
                etVolume.setText(String.valueOf(value));
            }
        });
        ibPlus.setOnClickListener(view1 -> {
            int value = Integer.valueOf(etVolume.getText().toString());
            value++;
            etVolume.setText(String.valueOf(value));
        });
        ibPlusTen.setOnClickListener(view1 -> {
            int value = Integer.valueOf(etVolume.getText().toString());
            value = value + 10;
            etVolume.setText(String.valueOf(value));
        });

        builder.setView(view)
               .setTitle("Add new cocktail Component")
               .setPositiveButton("Save",
                                  (dialog, which) -> callback
                                          .onComponentAdded(selectedComponent[0],
                                                            Integer.valueOf(etVolume.getText().toString())))
               .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> {})
               .create();
        builder.show();
    }
}
