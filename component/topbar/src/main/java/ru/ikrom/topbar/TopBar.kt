package ru.ikrom.topbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import com.google.android.material.appbar.AppBarLayout

class TopBar: AppBarLayout {
    private lateinit var btnBack: ImageButton
    private lateinit var btnSearch: ImageButton
    private lateinit var btnMore: ImageButton

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    fun init(context: Context){
        LayoutInflater.from(context).inflate(R.layout.component_album_bar, this, true)
        btnBack = findViewById(R.id.btn_back)
        btnMore = findViewById(R.id.btn_more)
        btnSearch = findViewById(R.id.btn_search)
    }

    fun setOnBackClick(onClick: () -> Unit){
        btnBack.setOnClickListener {
            onClick()
        }
    }

    fun setOnSearchClick(onClick: () -> Unit){
        btnSearch.setOnClickListener {
            onClick()
        }
    }

    fun setOnMoreClick(onClick: () -> Unit){
        btnMore.setOnClickListener {
            onClick()
        }
    }
}