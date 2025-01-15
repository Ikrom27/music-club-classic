package ru.ikrom.placeholder

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

class PlaceholderView: FrameLayout {
    private lateinit var ivImage: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvDescription: TextView

    var imageSrc: Int = 0
        set(value) {
            field = value
            ivImage.setImageResource(value)
        }
    var title: String = ""
        set(value) {
            field = value
            tvTitle.text = value
        }
    var description: String = ""
        set(value) {
            field = value
            tvDescription.text = value
        }

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

    fun show(){
        visibility = View.VISIBLE
    }

    fun hide(){
        visibility = View.GONE
    }

    fun init(context: Context){
        LayoutInflater.from(context).inflate(R.layout.component_placeholder, this, true)
        ivImage = findViewById(R.id.iv_image)
        tvTitle = findViewById(R.id.tv_title)
        tvDescription = findViewById(R.id.tv_description)
    }

    @SuppressLint("Recycle")
    fun getStuffFromXML(attrs: AttributeSet?){
        val data = context.obtainStyledAttributes(attrs, R.styleable.PlaceHolderView)
        imageSrc = data.getResourceId(R.styleable.PlaceHolderView_imageSrc, 0)
        title = data.getString(R.styleable.PlaceHolderView_title) ?: ""
        description = data.getString(R.styleable.PlaceHolderView_description) ?: ""
    }
}