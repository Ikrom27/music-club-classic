package ru.ikrom.appbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import com.google.android.material.appbar.AppBarLayout

class AppBar: AppBarLayout {
    private lateinit var btnSearch: ImageButton
    private lateinit var btnConnectDevice: ImageButton

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
        getStuffFromXML(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
        getStuffFromXML(attrs)
    }

    fun init(context: Context){
        LayoutInflater.from(context).inflate(R.layout.component_app_bar, this, true)
        btnSearch = findViewById(R.id.btn_search)
        btnConnectDevice = findViewById(R.id.btn_connect)
    }

    fun setOnSearchClick(onClick: () -> Unit){
        btnSearch.setOnClickListener {
            onClick()
        }
    }

    fun setOnConnectClick(onClick: () -> Unit){
        btnConnectDevice.setOnClickListener {
            onClick()
        }
    }

    fun getStuffFromXML(attrs: AttributeSet?){
        val data = context.obtainStyledAttributes(attrs, R.styleable.AppBar)
    }
}