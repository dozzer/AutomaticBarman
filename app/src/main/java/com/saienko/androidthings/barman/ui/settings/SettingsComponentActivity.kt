package com.saienko.androidthings.barman.ui.settings

import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.Component
import com.saienko.androidthings.barman.db.interfaces.IBaseManager
import com.saienko.androidthings.barman.db.interfaces.IComponentManager
import com.saienko.androidthings.barman.db.manager.ComponentManager
import com.saienko.androidthings.barman.ui.base.BaseAdapter
import com.saienko.androidthings.barman.ui.base.BaseListActivity
import com.saienko.androidthings.barman.ui.dialog.CustomDialog
import com.saienko.androidthings.barman.ui.dialog.OnSaveTextListener
import com.saienko.androidthings.barman.ui.settings.adapter.ComponentAdapter
import com.saienko.androidthings.barman.ui.settings.adapter.holder.ComponentViewHolder
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class SettingsComponentActivity : BaseListActivity<Component, ComponentViewHolder>() {
    override val adapter: BaseAdapter<Component, ComponentViewHolder>
        get() = localAdapter
    override val baseManager: IBaseManager<Component>
        get() = ComponentManager()

    private lateinit var localAdapter: ComponentAdapter
    private lateinit var componentManager: IComponentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_component)
        componentManager = ComponentManager()
        initUI()
    }

    override fun initUI() {
        super.initUI()
        val fabAddDrink = findViewById<FloatingActionButton>(R.id.fabAdd)
        fabAddDrink.setOnClickListener { showDialogAddComponent() }

        initRecyclerView(findViewById(R.id.list))
    }

    private fun showDialogAddComponent() {
        CustomDialog.addNewItem(this, "New component", "Enter new Component", "",
                                object : OnSaveTextListener {
                                    override fun onTextSaved(text: String, coefficient: Double) {
                                        addComponent(text, coefficient)
                                    }
                                })
    }

    private fun getComponentList() {
//        addSubscription(componentManager.all.sync().subscribe({ components -> updateItemList(components) }))
//        async(CommonPool) {
//            updateItemList(baseManager.list())
//        }
        launch(UI) {
            updateItemList(async(CommonPool) { baseManager.list() }.await())
        }
    }

    private fun updateItemList(components: MutableList<Component>) {
        localAdapter.addItems(components)
        updateMenu(components.toMutableList())
    }

    override fun initAdapter() {
        localAdapter = ComponentAdapter(object : ComponentAdapter.OnItemListener {
            override fun onEdit(component: Component) {
                showDialogUpdateComponent(component)
            }

            override fun onDelete(component: Component) {
                showDeleteDialog(component)
            }
        })
    }

    private fun showDialogUpdateComponent(component: Component) {
        CustomDialog.addNewItem(this, "Update component", "Update Component", component.componentName,
                                object : OnSaveTextListener {
                                    override fun onTextSaved(text: String, coefficient: Double) {
                                        component.componentName = text
                                        component.coefficient = coefficient
                                        updateComponent(component)
                                    }
                                })
    }


    private fun showDeleteDialog(component: Component) {
        CustomDialog.showDialog(this, getString(R.string.are_you_sure),
                                getString(R.string.do_you_want_to_delete_this_item),
                                DialogInterface.OnClickListener { _, _ -> deleteItem(component) })
    }

    private fun addComponent(name: String, coefficient: Double) {
        launch(UI) {
            async(CommonPool) { baseManager.insert(Component(name, coefficient)) }.await()
            getComponentList()
        }
    }

    private fun updateComponent(component: Component) {
        launch(UI) {
            async(CommonPool) { baseManager.update(component) }.await()
            getComponentList()
        }
    }
}
