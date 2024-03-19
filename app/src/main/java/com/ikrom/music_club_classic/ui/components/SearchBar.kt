package com.ikrom.music_club_classic.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import androidx.core.widget.doOnTextChanged
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.textfield.TextInputEditText
import com.ikrom.music_club_classic.R

class SearchBar: AppBarLayout {
    private lateinit var btnBack: ImageButton
    private lateinit var btnClean: ImageButton

    lateinit var searchField: TextInputEditText

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

    private fun init(context: Context){
        LayoutInflater.from(context).inflate(R.layout.component_search_bar, this, true)
        btnBack = findViewById(R.id.btn_back)
        btnClean = findViewById(R.id.btn_clear)
        searchField = findViewById(R.id.et_input_field)
    }

    private fun getStuffFromXML(attrs: AttributeSet?){
        val data = context.obtainStyledAttributes(attrs, R.styleable.SearchBar)
    }

    inline fun doOnTextChanged(
        crossinline action: (
            text: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) -> Unit
    ){
        searchField.doOnTextChanged { text, start, before, count ->
            action(text, start, before, count)
        }
    }

    fun setOnBackClick(onClick: () -> Unit){
        btnBack.setOnClickListener {
            onClick()
        }
    }

    fun setOnCleanClick(onClick: () -> Unit){
        btnClean.setOnClickListener {
            onClick()
        }
    }
}