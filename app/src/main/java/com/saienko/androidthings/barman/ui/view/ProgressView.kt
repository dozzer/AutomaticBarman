package com.saienko.androidthings.barman.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.saienko.androidthings.barman.R

class ProgressView : LinearLayout {

    private lateinit var tvName: TextView
    private lateinit var progressBar: ProgressBar
    private var name: String = ""

    var progress: Int = 0
        set(progress) {
            field = progress
            progressBar.progress = progress
        }

    fun setName(name: String) {
        this.name = name
        tvName.text = name
    }

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs, defStyle)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyle: Int) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.view_progress_view, this, true)
        tvName = view.findViewById(R.id.tvName)
        progressBar = view.findViewById(R.id.tvProgress)
    }

}
