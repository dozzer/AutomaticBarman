package com.saienko.androidthings.barman.ui.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.CocktailElement
import com.saienko.androidthings.barman.db.entity.Component
import com.saienko.androidthings.barman.db.manager.CocktailElementManager
import com.saienko.androidthings.barman.db.manager.ComponentManager
import com.saienko.androidthings.barman.ui.adapter.ComponentDialogAdapter
import com.saienko.androidthings.barman.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_add_cocktail_component.*
import kotlinx.android.synthetic.main.view_enter_value.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class AddCocktailComponentActivity : BaseActivity() {

    companion object {

        private const val EXTRA_EXIST_COMPONENT_IDS = "EXTRA_EXIST_COMPONENT_IDS"
        private const val EXTRA_COCKTAIL_ELEMENT_ID = "EXTRA_COCKTAIL_ELEMENT_ID"
        private const val EXTRA_ADD_COCKTAIL_COMPONENT = "EXTRA_ADD_COCKTAIL_COMPONENT"

        const val EXTRA_COMPONENT_ID = "EXTRA_COMPONENT_ID"
        const val EXTRA_VOLUME = "EXTRA_VOLUME"

        fun start(activity: Activity, existComponentIds: LongArray, requestCode: Int) {
            val intent = Intent(activity, AddCocktailComponentActivity::class.java)
            intent.putExtra(EXTRA_ADD_COCKTAIL_COMPONENT, true)
            intent.putExtra(AddCocktailComponentActivity.EXTRA_EXIST_COMPONENT_IDS, existComponentIds)
            activity.startActivityForResult(intent, requestCode)
        }

        fun start(activity: AddCocktailActivity, cocktailElementId: Long, requestCode: Int) {
            val intent = Intent(activity, AddCocktailComponentActivity::class.java)
            intent.putExtra(EXTRA_ADD_COCKTAIL_COMPONENT, false)
            intent.putExtra(AddCocktailComponentActivity.EXTRA_COCKTAIL_ELEMENT_ID, cocktailElementId)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    private var isNewCocktailComponent: Boolean = true
    private var cocktailElementId: Long = -1

//    lateinit var ibMinus: Button
//    lateinit var ibPlus: Button
//    lateinit var ibMinusTen: Button
//    lateinit var ibPlusTen: Button
//    lateinit var etVolume: TextView
//    lateinit var componentSpinner: Spinner

//    lateinit var btnSave: Button
//    lateinit var btnCancel: Button

    lateinit var selectedComponent: Component
    lateinit var components: MutableList<Component>

    private lateinit var existComponentIds: LongArray
    private lateinit var cocktailElement: CocktailElement


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_cocktail_component)
        handleIntent()
        initUI()
        initListeners()
        if (isNewCocktailComponent) {
            getComponents()
        } else {
            getCocktailElement()
        }
    }

    private fun getCocktailElement() {
        val cocktailElementManager = CocktailElementManager()
        launch(UI) {
            cocktailElement = async(CommonPool) { cocktailElementManager.get(cocktailElementId) }.await()
            val volume = cocktailElement.volume
            etValue.text = volume.toString()
        }
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        val i = item.itemId
        return if (i == android.R.id.home) {
            onComponentCanceled()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun handleIntent() {
        isNewCocktailComponent = intent.getBooleanExtra(EXTRA_ADD_COCKTAIL_COMPONENT, true)
        if (isNewCocktailComponent) {
            existComponentIds = intent.getLongArrayExtra(EXTRA_EXIST_COMPONENT_IDS)
        } else {
            cocktailElementId = intent.getLongExtra(EXTRA_COCKTAIL_ELEMENT_ID, -1)
        }
    }

//    override fun initUI() {
//        super.initUI()
////        componentSpinner = findViewById(R.id.componentSpinner)
////        ibMinus = findViewById(R.id.btnMinus)
////        ibPlus = findViewById(R.id.btnPlus)
////        ibMinusTen = findViewById(R.id.btnMinusTen)
////        ibPlusTen = findViewById(R.id.btnPlusTen)
////        btnSave = findViewById(R.id.btnSave)
////        btnCancel = findViewById(R.id.btnCancel)
////        etVolume = findViewById(R.id.etValue)
//    }

    private fun initListeners() {

        btnMinus.setOnClickListener {
            var value = Integer.valueOf(etValue.text.toString())
            if (value > 2) {
                value--
                etValue.text = value.toString()
            }
        }
        btnMinusTen.setOnClickListener {
            var value = Integer.valueOf(etValue.text.toString())
            if (value > 11) {
                value -= 10
                etValue.text = value.toString()
            }
        }
        btnPlus.setOnClickListener {
            var value = Integer.valueOf(etValue.text.toString())
            value++
            etValue.text = value.toString()
        }
        btnPlusTen.setOnClickListener {
            var value = Integer.valueOf(etValue.text.toString())
            value += 10
            etValue.text = value.toString()
        }
        btnSave.setOnClickListener {
            val volume = Integer.valueOf(etValue.text.toString())
            if (isNewCocktailComponent) {
                onComponentAdded(selectedComponent, volume)
            } else {
                onComponentEdited(cocktailElement, volume)
            }
        }
        btnCancel.setOnClickListener {
            onComponentCanceled()
        }
    }

    private fun onComponentCanceled() {
        finish()
    }

    private fun onComponentAdded(component: Component, volume: Int) {
        val intent = Intent()
        intent.putExtra(EXTRA_COMPONENT_ID, component.id)
        intent.putExtra(EXTRA_VOLUME, volume)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun onComponentEdited(cocktailElement: CocktailElement, volume: Int) {
        val intent = Intent()
        intent.putExtra(EXTRA_VOLUME, volume)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun getComponents() {
        val componentManager = ComponentManager()
        launch(UI) {
            components = async(CommonPool) { componentManager.allFree(existComponentIds.toMutableList()) }.await()
            initAdapter(components)
        }
    }

    private fun initAdapter(components: MutableList<Component>) {
        val adapter = ComponentDialogAdapter(this, components)
        componentSpinner.adapter = adapter
        componentSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(adapterView: AdapterView<*>, view: View,
                                        i: Int, l: Long) {
                selectedComponent = components[i]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
                // IGNORE
            }
        }
    }
}
