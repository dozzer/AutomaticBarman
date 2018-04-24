package com.saienko.androidthings.barman.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.ContextThemeWrapper
import android.widget.EditText
import com.saienko.androidthings.barman.R


/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 16:39
 */

object CustomDialog {
//
//    fun showDialog(context: Context, title: String, message: String,
//                   positiveListener: DialogInterface.OnClickListener,
//                   negativeListener: DialogInterface.OnClickListener): AlertDialog {
//
//        val ad = AlertDialog.Builder(context)
//                .setTitle(title)
//                .setMessage(message)
//                .setPositiveButton(android.R.string.ok, positiveListener)
//                .setNegativeButton(android.R.string.cancel, negativeListener)
//                .create()
//        ad.show()
//        ad.setCanceledOnTouchOutside(false)
//        ad.setCancelable(false)
//        return ad
//    }

    fun showDialog(context: Context, title: String, message: String,
                   positiveListener: DialogInterface.OnClickListener): AlertDialog {

        return AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, positiveListener)
                .setNegativeButton(android.R.string.cancel) { _, _ -> }
                .create()
                .apply {
                    show()
                    setCanceledOnTouchOutside(false)
                    setCancelable(false)
                }
    }

    fun selectOneOfTwo(context: Context, title: String,
                       message: String,
                       firstButtonText: String,
                       secondButtonText: String,
                       firstAction: ActionListener,
                       secondAction: ActionListener) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)

        builder.setPositiveButton(firstButtonText) { dialog, _ ->
            firstAction.onAction()
            dialog.dismiss()
        }

        builder.setNegativeButton(secondButtonText) { dialog, _ ->
            secondAction.onAction()
            dialog.dismiss()
        }

        builder.setNeutralButton(context.resources.getString(android.R.string.cancel)) { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun addNewItem(context: Context, title: String, message: String,
                   text: String,
                   callback: OnSaveTextListener): AlertDialog {
        val edittext = EditText(ContextThemeWrapper(context, R.style.EditText_Dialog), null, 0)
        edittext.setText(text)
        val ad = AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Save") { _, _ -> callback.onTextSaved(edittext.text.toString(), 1.0) }
                .setNegativeButton(android.R.string.cancel) { _, _ -> }
                .setView(edittext)
                .create()
        ad.show()
        return ad
    }

//    fun editCocktailElement(activity: Activity, cocktailElement: CocktailElement,
//                            callback: OnAddComponentListener) {
//        val volume = cocktailElement.volume
//        val inflater = activity.layoutInflater
//
//        val builder = AlertDialog.Builder(activity)
//        val view = inflater.inflate(R.layout.dialog_edit_component, null)
//        val ibMinus = view.findViewById<ImageButton>(R.id.ibMinus)
//        val ibPlus = view.findViewById<ImageButton>(R.id.ibPlus)
//        val ibMinusTen = view.findViewById<ImageButton>(R.id.ibMinusTen)
//        val ibPlusTen = view.findViewById<ImageButton>(R.id.ibPlusTen)
//        val etVolume = view.findViewById<EditText>(R.id.etVolume)
//
//        etVolume.setText(volume.toString())
//        ibMinus.setOnClickListener {
//            var value = Integer.valueOf(etVolume.text.toString())
//            if (value > 2) {
//                value--
//                etVolume.setText(value.toString())
//            }
//        }
//        ibMinusTen.setOnClickListener {
//            var value = Integer.valueOf(etVolume.text.toString())
//            if (value > 11) {
//                value -= 10
//                etVolume.setText(value.toString())
//            }
//        }
//
//
//        ibPlus.setOnClickListener {
//            var value = Integer.valueOf(etVolume.text.toString())
//            value++
//            etVolume.setText(value.toString())
//        }
//        ibPlusTen.setOnClickListener {
//            var value = Integer.valueOf(etVolume.text.toString())
//            value += 10
//            etVolume.setText(value.toString())
//        }
//
//        builder.setView(view)
//                .setTitle("Edit cocktail Component")
//                .setPositiveButton("Save"
//                                  ) { _, _ ->
//                    callback
//                            .onComponentEdit(cocktailElement,
//                                             Integer.valueOf(etVolume.text.toString()))
//                }
//                .setNegativeButton(android.R.string.cancel) { _, _ -> }
//                .create()
//        builder.show()
//    }

//    fun addNewCocktailElement(activity: Activity,
//                              list: List<Component>,
//                              callback: OnAddComponentListener) {
//
//        val adapter = ComponentDialogAdapter(activity, list)
//        val inflater = activity.layoutInflater
//
//        val builder = AlertDialog.Builder(activity)
//        val view = inflater.inflate(R.layout.dialog_add_component, null)
//        val componentSpinner = view.findViewById<Spinner>(R.id.componentSpinner)
//        val ibMinus = view.findViewById<ImageButton>(R.id.ibMinus)
//        val ibPlus = view.findViewById<ImageButton>(R.id.ibPlus)
//        val ibMinusTen = view.findViewById<ImageButton>(R.id.ibMinusTen)
//        val ibPlusTen = view.findViewById<ImageButton>(R.id.ibPlusTen)
//
//        val etVolume = view.findViewById<EditText>(R.id.etVolume)
//        var selectedComponent: Component? = null
//        componentSpinner.adapter = adapter
//        componentSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//
//            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
//                selectedComponent = list[i]
//            }
//
//            override fun onNothingSelected(adapterView: AdapterView<*>) {
//                // IGNORE
//            }
//        }
//        ibMinus.setOnClickListener {
//            var value = Integer.valueOf(etVolume.text.toString())
//            if (value > 2) {
//                value--
//                etVolume.setText(value.toString())
//            }
//        }
//        ibMinusTen.setOnClickListener {
//            var value = Integer.valueOf(etVolume.text.toString())
//            if (value > 11) {
//                value -= 10
//                etVolume.setText(value.toString())
//            }
//        }
//        ibPlus.setOnClickListener {
//            var value = Integer.valueOf(etVolume.text.toString())
//            value++
//            etVolume.setText(value.toString())
//        }
//        ibPlusTen.setOnClickListener {
//            var value = Integer.valueOf(etVolume.text.toString())
//            value += 10
//            etVolume.setText(value.toString())
//        }
//
//        builder.setView(view)
//                .setTitle("Add new cocktail Component")
//                .setPositiveButton("Save"
//                                  ) { _, _ ->
//                    callback
//                            .onComponentAdded(selectedComponent,
//                                              Integer.valueOf(etVolume.text.toString()))
//                }
//                .setNegativeButton(android.R.string.cancel) { _, _ -> }
//                .create()
//        builder.show()
//    }
}

interface ActionListener {
    fun onAction()
}
