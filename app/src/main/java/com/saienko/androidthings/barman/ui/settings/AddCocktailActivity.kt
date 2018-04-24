package com.saienko.androidthings.barman.ui.settings

import android.app.Activity
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.support.design.chip.Chip
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.LongSparseArray
import android.view.View
import androidx.core.view.get
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.Cocktail
import com.saienko.androidthings.barman.db.entity.CocktailElement
import com.saienko.androidthings.barman.db.interfaces.ICocktailManager
import com.saienko.androidthings.barman.db.manager.CocktailManager
import com.saienko.androidthings.barman.db.manager.ComponentManager
import com.saienko.androidthings.barman.ui.base.BaseActivity
import com.saienko.androidthings.barman.ui.settings.adapter.CocktailElementAdapter
import kotlinx.android.synthetic.main.activity_add_cocktail.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.util.*

class AddCocktailActivity : BaseActivity() {
    private var isNewCocktail: Boolean = false
    private var mCocktail: Cocktail? = null
    private var cocktailId: Long = 0
    private var mAdapter: CocktailElementAdapter? = null

    //    lateinit var ibAdd: ImageButton
//    private lateinit var tagGroupNew: ChipGroup
//    private lateinit var tagGroupExist: ChipGroup
//    lateinit var etName: EditText
    private val tags: LongSparseArray<Chip> = LongSparseArray()
//    private var tags: ArrayList<Chip> = ArrayList()

//    private val newColor: Int get() = getColor(R.color.amber_600)
//    private val componentColor: Int get() = getColor(R.color.colorAccent)
//    private val existColor: Int get() = getColor(R.color.green_300)


    private lateinit var cocktailManager: ICocktailManager
    private var editableCocktailElement: CocktailElement? = null

    private val existComponentIds: LongArray
        get() {
            val componentIds = LongArray(getAdapter().itemCount)
            for (i in 0 until getAdapter().itemCount) {
                if (getAdapter().items[i].component != null) {
                    componentIds[i] = getAdapter().items[i].component!!.id
                } else {
                    componentIds[i] = -1
                }
            }
            return componentIds
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_cocktail)
        cocktailManager = CocktailManager()
        handleIntent()
        initUI()
    }

    private fun handleIntent() {
        if (intent.hasExtra(EXTRA_COCKTAIL_ID)) {
            isNewCocktail = false
            cocktailId = intent.getLongExtra(EXTRA_COCKTAIL_ID, -1L)
        } else {
            isNewCocktail = true
        }
    }

    override fun initUI() {
        super.initUI()
//        etName = findViewById(R.id.etName)
//        val btnSave = findViewById<Button>(R.id.btnSave)
//        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
//        tagGroupNew = findViewById(R.id.newTagGroup)
//        tagGroupExist = findViewById(R.id.existTagGroup)
//        ibAdd = findViewById(R.id.ibAdd)

        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = getAdapter()

        btnSave.setOnClickListener { saveCocktail() }

        ibAdd.setOnClickListener { openAddComponentDialog() }


        initListeners()
        if (!isNewCocktail) {
            getCocktail(cocktailId)
        }
//        setAddButtonVisibility()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_COMPONENT && resultCode == RESULT_OK && data != null) {
            val componentId = data.getLongExtra(AddCocktailComponentActivity.EXTRA_COMPONENT_ID, -1)
            val volume = data.getIntExtra(AddCocktailComponentActivity.EXTRA_VOLUME, -1)
            addComponent(componentId, volume)
        } else if (requestCode == REQUEST_CODE_EDIT_COMPONENT && resultCode == RESULT_OK && data != null) {
            setAddButtonVisibility()
            val volume = data.getIntExtra(AddCocktailComponentActivity.EXTRA_VOLUME, -1)
            if (editableCocktailElement != null) {
                editableCocktailElement!!.volume = volume
                editComponent(editableCocktailElement!!)
            }
        }
    }

    private fun getCocktail(cocktailId: Long) {
        launch(UI) {
            val cocktail = async(CommonPool) { cocktailManager.get(cocktailId) }.await()

            mCocktail = cocktail

            etName.setText(cocktail.cocktailName)
            getCocktailElements(cocktail)
//            ibAdd.visibility = if (isExistFreeComponents()) View.VISIBLE else View.GONE
            setAddButtonVisibility()
            setTitle(R.string.title_activity_edit_cocktail)
        }
    }

//    private fun isExistFreeComponents(): Boolean {
//        ODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }

    private fun getCocktailElements(cocktail: Cocktail) {
        launch(UI) {
            val list = async(CommonPool) {
                cocktailManager.getCocktailElement(cocktail.id)
            }.await()
            getAdapter().addAll(list)
            list.forEach {
                addComponentTag(it)
            }
        }
    }

    private fun saveCocktail() {
        if (isNewCocktail) {
            if (!TextUtils.isEmpty(etName.text.toString())) {
                mCocktail = Cocktail(etName.text.toString())
                if (!getAdapter().items.isEmpty()) {
                    mCocktail!!.cocktailElements = getAdapter().items
                    if (mCocktail != null) {
                        saveCocktail(mCocktail!!)
                    }
                } else {
                    showSnackBar("Component list can't be empty")
                }
            } else {
                showSnackBar("Name can't be empty")
            }
        } else {
            if (getAdapter().items.isEmpty()) {
                showSnackBar("Component list can't be empty")
            } else if (!TextUtils.isEmpty(etName.text.toString())) {
                val cocktail = mCocktail
                if (cocktail != null) {
                    cocktail.cocktailName = etName.text.toString()
                    updateCocktail(cocktail, getAdapter().items)
                }

            } else {
                showSnackBar("Speed can't be empty")
            }
        }
    }

    private fun updateCocktail(cocktail: Cocktail, items: ArrayList<CocktailElement>) {
        launch(UI) {
            async(CommonPool) { cocktailManager.update(cocktail, items) }.await()
            cocktailFinished()
        }
    }

    private fun saveCocktail(cocktail: Cocktail) {
        launch(UI) {
            async(CommonPool) { cocktailManager.insert(cocktail) }.await()
            cocktailFinished()
        }
    }

    private fun cocktailFinished() {
        setResult(RESULT_OK, Intent())
        finish()
    }

    private fun initListeners() {
//        tagGroupNew.setOnCheckedChangeListener { group, checkedId ->
//            TODO()
//        }
//        tagGroupNew.setOnTagClickListener { tag, position ->
//            tagGroupNew.remove(position)
//            addExistTag(tag)
//        }
//
//        tagGroupExist.setOnTagDeleteListener { _, tag, position ->
//            tagGroupExist.remove(position)
//            addNewTag(tag)
//        }
//
//        tagGroupExist.setOnTagClickListener { tag, position ->
//            if (tag.isDeletable) {
//                tagGroupExist.remove(position)
//                addNewTag(tag)
//            }
//        }
        existTagGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group[checkedId]
            print(chip)
        }
    }

    private fun addNewTag(tag: Tag) {
//        tag.layoutColor = newColor
//        tag.isDeletable = false
//        tagGroupNew.addTag(tag)
//        tags.remove(tag)
        TODO()
    }

    private fun addExistTag(tag: Tag) {
//        tag.layoutColor = existColor
//        tag.isDeletable = true
//        tagGroupExist.addTag(tag)
//        tags.add(tag)
        TODO()
    }

    private fun setAddButtonVisibility() {
        launch(UI) {
            val manager = ComponentManager()
            val components = async(CommonPool) {
                manager.allFree(existComponentIds.toMutableList())
            }.await()
            ibAdd.visibility = if (components.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun openAddComponentDialog() {
        AddCocktailComponentActivity.start(this, existComponentIds, REQUEST_CODE_ADD_COMPONENT)
    }

    private fun editComponent(cocktailElement: CocktailElement) {
        getAdapter().update(cocktailElement)
    }

    private fun addComponent(componentId: Long, volume: Int) {
        launch(UI) {
            val cocktailElement = CocktailElement(componentId, -1, volume)
            val componentManager = ComponentManager()

            val component = async(CommonPool) { componentManager.get(componentId) }.await()

            cocktailElement.component = component
            getAdapter().add(cocktailElement)
            addComponentTag(cocktailElement)
            setAddButtonVisibility()
        }
    }

    private fun getAdapter(): CocktailElementAdapter {
        if (mAdapter == null) {
            mAdapter = CocktailElementAdapter(object : CocktailElementAdapter.OnItemListener {
                override fun onDelete(cocktailElement: CocktailElement) {
                    getAdapter().delete(cocktailElement)
                    setAddButtonVisibility()
                    deleteComponentTag(cocktailElement)
                }

                override fun onEdit(cocktailElement: CocktailElement) {
                    openEditComponentDialog(cocktailElement)
                }
            })
        }
        return mAdapter as CocktailElementAdapter
    }

    private fun deleteComponentTag(cocktailElement: CocktailElement) {

//        val name = cocktailElement.component!!.componentName
//        val list = tagGroupExist.tags
//        list.indices.filter { TextUtils.equals(name, list[it].text) }.forEach { tagGroupExist.remove(it) }
        existTagGroup.removeView(tags.get(cocktailElement.cocktailId))
    }

    private fun addComponentTag(cocktailElement: CocktailElement) {
        val chip = Chip(this)
        setChipListeners(chip)
        chip.setChipBackgroundColorResource(R.color.colorAccent)
        chip.text = cocktailElement.component?.componentName
        chip.closeIcon = getDrawable(R.drawable.ic_clear)
        chip.chipIcon = getDrawable(R.drawable.ic_close)
        chip.checkedIcon = getDrawable(R.drawable.ic_plus_ten)
        chip.isCheckable = true
        existTagGroup.addView(chip)
        tags.put(cocktailElement.cocktailId, chip)
    }

    private fun setChipListeners(chip: Chip) {
        chip.setOnCloseIconClickListener {
            val tag = it.tag
            print(tag)
        }
        chip.setOnClickListener {
            val tag = it.tag
            print(tag)
        }
    }

    private fun openEditComponentDialog(cocktailElement: CocktailElement) {
        editableCocktailElement = cocktailElement
        AddCocktailComponentActivity.start(this, cocktailElement.id, REQUEST_CODE_EDIT_COMPONENT)
    }

    companion object {

        private const val EXTRA_COCKTAIL_ID = "EXTRA_COCKTAIL_ID"
        private const val REQUEST_CODE_ADD_COMPONENT = 43
        private const val REQUEST_CODE_EDIT_COMPONENT = 44


        fun start(activity: Activity, requestCode: Int) {
            val intent = Intent(activity, AddCocktailActivity::class.java)
            activity.startActivityForResult(intent, requestCode)
        }

        fun start(activity: Activity, cocktail: Cocktail, requestCode: Int) {
            val intent = Intent(activity, AddCocktailActivity::class.java)
            intent.putExtra(EXTRA_COCKTAIL_ID, cocktail.id)
            activity.startActivityForResult(intent, requestCode)
        }
    }
}
