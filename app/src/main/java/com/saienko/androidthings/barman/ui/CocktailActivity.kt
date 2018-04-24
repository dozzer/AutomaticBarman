package com.saienko.androidthings.barman.ui

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.LongSparseArray
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.saienko.androidthings.barman.R
import com.saienko.androidthings.barman.db.entity.Cocktail
import com.saienko.androidthings.barman.db.manager.CocktailManager
import com.saienko.androidthings.barman.listener.OnResultListener
import com.saienko.androidthings.barman.listener.OnUpdateListener
import com.saienko.androidthings.barman.receiver.CocktailBroadcastReceiver
import com.saienko.androidthings.barman.receiver.CocktailUpdateBroadcastReceiver
import com.saienko.androidthings.barman.service.CocktailService
import com.saienko.androidthings.barman.ui.adapter.CocktailElementAdapter
import com.saienko.androidthings.barman.ui.base.BaseActivity
import com.saienko.androidthings.barman.ui.view.ProgressView
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch


class CocktailActivity : BaseActivity() {

    private lateinit var cocktailBroadcastReceiver: CocktailBroadcastReceiver
    private lateinit var cocktailUpdateBroadcastReceiver: CocktailUpdateBroadcastReceiver

    private lateinit var cocktail: Cocktail

    private var holderMap: LongSparseArray<ProgressView>? = null
    private var cocktailEnd: LongSparseArray<Boolean>? = null
    private var progressArray: LongSparseArray<Int>? = null

    private lateinit var btnStart: Button
    private lateinit var btnStop: Button
    private lateinit var cocktailItemList: RecyclerView
    private lateinit var progressContainer: LinearLayout
    private lateinit var errorView: TextView

    private var cocktailId: Long = -1

    private var modelCount: Int = 0

    private val totalProgress: Int
        get() {
            val total = (1..modelCount)
                    .filter { progressArray!!.keyAt(it) != TOTAL_POSITION && progressArray!!.valueAt(it) != null }
                    .sumBy { progressArray!!.valueAt(it) }
            return total / (modelCount - 1)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cocktail)
        holderMap = LongSparseArray()
        progressArray = LongSparseArray()
        cocktailEnd = LongSparseArray()
        handleIntent()
        registerBroadcastReceiver()
        initUI()
    }

    override fun onResume() {
        super.onResume()
        getCocktail(cocktailId)
    }

    private fun handleIntent() {
        cocktailId = intent.getLongExtra(EXTRA_COCKTAIL_ID, -1)
    }

    private fun getCocktail(cocktailId: Long) {
        val cocktailManager = CocktailManager()
        launch(UI) {
            initCocktail(async(CommonPool) { cocktailManager.get(cocktailId) }.await())
        }
    }

    private fun initCocktail(cocktail: Cocktail) {
        this.cocktail = cocktail
        if (!cocktailPositionsCorrect(cocktail)) {
            blockUI("Positions are not correct")
        } else if (!cocktailComponentsCorrect(cocktail)) {
            blockUI("Components are not correct")
        } else {
            updateUi(cocktail)
        }
    }

    private fun blockUI(text: String) {
        btnStart.isEnabled = false
        btnStop.isEnabled = false
        errorView.visibility = View.VISIBLE
        errorView.text = text
        progressContainer.visibility = View.GONE
    }

    private fun cocktailComponentsCorrect(cocktail: Cocktail): Boolean {
        return cocktail.cocktailElements!!.none { it.component == null }
    }

    private fun cocktailPositionsCorrect(cocktail: Cocktail): Boolean {
        return cocktail.cocktailElements!!.none { it.position == null }
    }

    private fun updateUi(cocktail: Cocktail) {
        progressContainer.visibility = View.VISIBLE
        errorView.visibility = View.GONE
        modelCount = cocktail.cocktailElements!!.size + 1
        cocktail.cocktailElements!!.forEachIndexed { _, element ->
            if (element.position == null || element.position!!.motor == null) {
                return@forEachIndexed
            }

            cocktailEnd!!.put(element.position!!.motor!!.gpioId, false)
            progressArray!!.put(element.position!!.motor!!.gpioId, 0)

            val progressView = ProgressView(this)
            progressView.setName(element.component!!.componentName)
            holderMap!!.put(element.position!!.motor!!.gpioId, progressView)
            progressContainer.addView(progressView)
        }
        val totalView = ProgressView(this)
        totalView.setName("Total")
        holderMap!!.put(TOTAL_POSITION, totalView)
        progressArray!!.put(TOTAL_POSITION, 0)
        progressContainer.addView(totalView)
        showCocktailItemList(cocktail)
    }

    private fun showCocktailItemList(cocktail: Cocktail) {
        val list = CocktailElementAdapter()
        val layoutManager = LinearLayoutManager(this)
        cocktailItemList.layoutManager = layoutManager
        cocktail.cocktailElements?.let {
            list.addItems(it)
            cocktailItemList.adapter = list
        }
    }

    override fun initUI() {
        super.initUI()
        btnStart = findViewById(R.id.btnStart)
        btnStop = findViewById(R.id.btnStop)
        cocktailItemList = findViewById(R.id.cocktailItemList)
        progressContainer = findViewById(R.id.progressContainer)
        errorView = findViewById(R.id.errorView)

        btnStart.setOnClickListener {
            CocktailService.startCocktail(this@CocktailActivity, cocktail.id)
            setProgressValue(0)
        }
        btnStop.setOnClickListener { CocktailService.stopCocktail(this@CocktailActivity) }
    }

    private fun registerBroadcastReceiver() {
        cocktailUpdateBroadcastReceiver = CocktailUpdateBroadcastReceiver(object : OnUpdateListener {
            override fun onItemUpdate(gpioId: Long, progress: Int) {
                setValue(gpioId, progress)
            }

            override fun onItemFinish() {
                btnStart.isEnabled = true
                btnStop.isEnabled = false
            }

            override fun onItemStart() {
                btnStart.isEnabled = false
                btnStop.isEnabled = true
            }
        })
        cocktailBroadcastReceiver = CocktailBroadcastReceiver(object : OnResultListener {

            override fun onError() {
                Log.d(TAG, "onError: ")
                btnStart.isEnabled = false
                btnStop.isEnabled = false
            }

            override fun onCancel() {
                Log.d(TAG, "onCancel: ")
                btnStart.isEnabled = true
                btnStop.isEnabled = false
            }
        })
        val intentFilter = IntentFilter(CocktailService.BROADCAST_COCKTAIL_STATUS)
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT)
        registerReceiver(cocktailBroadcastReceiver, intentFilter)

        val intentUpdateFilter = IntentFilter(CocktailService.BROADCAST_COCKTAIL_PROGRESS)
        intentUpdateFilter.addCategory(Intent.CATEGORY_DEFAULT)
        registerReceiver(cocktailUpdateBroadcastReceiver, intentUpdateFilter)
    }

    private fun setProgressValue(progress: Int) {
        this.progressContainer.postInvalidate()
        for (i in 1..modelCount) {
            progressArray!!.put(progressArray!!.keyAt(i), progress)
        }
    }

    private fun setValue(gpioId: Long, progress: Int) {
        val progressView = holderMap!!.get(gpioId)
        if (progressView != null) {
            if (progressView.progress != progress) {
                progressView.progress = progress
            }
            progressArray!!.put(gpioId, progress)

            val totalView = holderMap!!.get(TOTAL_POSITION)
            val totalProgress = totalProgress
            if (totalView.progress != totalProgress) {
                totalView.progress = totalProgress
            }
            this.progressContainer.postInvalidate()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(cocktailBroadcastReceiver)
        unregisterReceiver(cocktailUpdateBroadcastReceiver)
    }

    companion object {

        private const val TAG = "CocktailActivity"
        private const val EXTRA_COCKTAIL_ID = "EXTRA_COCKTAIL_ID"
        private const val TOTAL_POSITION: Long = -1

        fun start(context: Context, cocktailId: Long) {
            val intent = Intent(context, CocktailActivity::class.java)
            intent.putExtra(EXTRA_COCKTAIL_ID, cocktailId)
            context.startActivity(intent)
        }
    }
}
