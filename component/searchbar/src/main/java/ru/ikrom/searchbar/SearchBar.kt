package ru.ikrom.searchbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.doOnTextChanged
import com.google.android.material.appbar.AppBarLayout

class SearchBar: AppBarLayout {
    private lateinit var btnBack: ImageButton
    private lateinit var btnClean: ImageButton
    private lateinit var searchField: EditText

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

        btnClean.setOnClickListener { searchField.setText("") }
    }

    private fun getStuffFromXML(attrs: AttributeSet?){
        val data = context.obtainStyledAttributes(attrs, R.styleable.SearchBar)
    }

    fun doOnTextChanged(
        action: (
            text: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) -> Unit
    ){
        searchField.doOnTextChanged { text, start, before, count ->
            action(text, start, before, count)
            if(text.isNullOrEmpty()){
                changeCleanButtonVisibility(View.GONE)
            } else {
                changeCleanButtonVisibility(View.VISIBLE)
            }
        }
    }

    private fun changeCleanButtonVisibility(visibility: Int){
        btnClean.visibility = visibility
    }

    fun setOnBackClick(onClick: () -> Unit){
        btnBack.setOnClickListener {
            onClick()
        }
    }

    fun setOnCleanClick(onClick: () -> Unit){
        btnClean.setOnClickListener {
            searchField.setText("")
            onClick()
        }
    }
}