package com.saienko.androidthings.barman.ui

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import com.cunoraz.tagview.Tag
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.Utils
import com.saienko.androidthings.barman.db.entity.Cocktail
import com.saienko.androidthings.barman.db.interfaces.ICocktailManager
import com.saienko.androidthings.barman.db.manager.CocktailManager
import com.saienko.androidthings.barman.ui.adapter.CocktailAdapter
import com.saienko.androidthings.barman.ui.adapter.OnItemListener
import com.saienko.androidthings.barman.ui.base.BaseActivity
import com.saienko.androidthings.barman.ui.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


open class MainActivity : BaseActivity() {

    //    lateinit var tagGroup: TagView
//    lateinit var tagSelectedGroup: TagView
    lateinit var manager: ICocktailManager
    private var localAdapter: CocktailAdapter? = null

//TODO    Killing Android View boilerplate with anko / kotlin-android-extensions
//TODO    https://proandroiddev.com/code-clean-up-with-kotlin-19ee1c8c0719

    private var tags: MutableList<Tag> = ArrayList()

    private val selectedColor: Int
        get() = getColor(R.color.colorAccent)


    private val allColor: Int
        get() = getColor(R.color.green_700)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        manager = CocktailManager()
        initUI()
        tags
        getCocktailList()
        if (Utils.isThingsDevice(this)) {
            getDeviceList()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.list_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        val i = item.itemId
        return if (i == R.id.action_settings) {
            SettingsActivity.start(this)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun showCocktailTags(cocktails: MutableList<Cocktail>?) {
        val out = ArrayList<Tag>()
        for (component in getComponents(cocktails)) {
            out.add(Tag(component))
        }
        showTags(out)
    }

    private fun getComponents(cocktails: MutableList<Cocktail>?): List<String> {
        val out = HashSet<String>()
        cocktails?.forEach {
            it.cocktailElements?.forEach {
                val component = it.component
                if (component != null) {
                    out.add(component.componentName)
                }
            }
        }
        return out.toList()
    }

    private fun showTags(tags: List<Tag>) {
        tagGroup.tags.clear()
        for (tag in tags) {
            tag.layoutColor = allColor
            tagGroup.addTag(tag)
        }
    }

    private fun getCocktailList() {
        if (tagSelectedGroup.tags.isEmpty()) {
            launch(UI) {
                val cocktails = async(CommonPool) { manager.list() }.await()
                cocktails.map {
                    it.cocktailElements = null
                }
                showCocktails(cocktails)
                showCocktailTags(cocktails)
            }

        } else {
            launch(UI) {
                TODO("додедлть")
//                val cocktails = async(CommonPool) { manager.getCocktails(Mapper.map(tagSelectedGroup.tags)) }.await()
//                showCocktails(cocktails)
            }
        }
    }

    private fun showCocktails(cocktailList: List<Cocktail>) {
        getAdapter().clear()
        getAdapter().addItems(cocktailList)
    }

    private fun getAdapter(): CocktailAdapter {
        if (localAdapter == null) {
            localAdapter = CocktailAdapter(object : OnItemListener {
                override fun onStart(cocktail: Cocktail) {
                    CocktailActivity.start(this@MainActivity, cocktail.id)
                }
            })
        }
        return localAdapter as CocktailAdapter
    }

    override fun initUI() {
        super.initUI()
//        tagGroup = findViewById(R.id.tagGroup)
//        tagSelectedGroup = findViewById(R.id.tagSelectedGroup)
        initRecyclerView(findViewById(R.id.rvCocktails))
        initListeners()
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        recyclerView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(this, 3)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = getAdapter()
    }

    private fun initListeners() {
        tagGroup.setOnTagClickListener { tag, position ->
            tagGroup.remove(position)
            addSelectedTag(tag)
            getCocktailList()
        }

        tagSelectedGroup.setOnTagDeleteListener { _, tag, position ->
            tagSelectedGroup.remove(position)
            addAllTag(tag)
            getCocktailList()
        }

        tagSelectedGroup.setOnTagClickListener { tag, position ->
            if (tag.isDeletable) {
                tagSelectedGroup.remove(position)
                addAllTag(tag)
                getCocktailList()
            }
        }
    }

    private fun addAllTag(tag: Tag) {
        tag.layoutColor = allColor
        tag.isDeletable = false
        tagGroup.addTag(tag)
        tags.remove(tag)
    }

    private fun addSelectedTag(tag: Tag) {
        tag.layoutColor = selectedColor
        tag.isDeletable = true
        tagSelectedGroup.addTag(tag)
        tags.add(tag)
    }

    private fun getDeviceList() {
        Log.d(TAG, "getDeviceList() called")
        val manager = PeripheralManager.getInstance()
        val portList = manager.gpioList
        for (port in portList) {
            turnOffPort(port, manager)
        }
        if (portList.isEmpty()) {
            Log.i(TAG, "No GPIO port available on this device.")
        } else {
            Log.i(TAG, "List of available ports: $portList")
        }
    }

    private fun turnOffPort(port: String, manager: PeripheralManager) {
        var mLedGpio: Gpio? = null
        try {
            mLedGpio = manager.openGpio(port)
            if (mLedGpio != null) {
                mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
            }

        } catch (e: IOException) {
            Log.e(TAG, "Error configuring GPIO pins", e)
        }

        if (mLedGpio != null) {
            try {
                mLedGpio.close()
            } catch (e: IOException) {
                Log.e(TAG, "Error closing LED GPIO", e)
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
