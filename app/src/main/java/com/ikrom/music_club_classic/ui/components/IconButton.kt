package com.ikrom.music_club_classic.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ikrom.music_club_classic.R

class IconButton: FrameLayout {
    lateinit var container: ConstraintLayout
    lateinit var leadingIconView: ImageView
    lateinit var tailContentView: FrameLayout
    lateinit var buttonText: TextView
    lateinit var separator: View

    var gravity = 0
        set(value) {
            field = value
            buttonText.gravity = value
        }

    var leadingIcon = 0
        set(value) {
            field = value
            if (value == 0){
                leadingIconView.visibility = View.GONE
            } else {
                leadingIconView.setImageResource(value)
                leadingIconView.visibility = View.VISIBLE
            }
        }

    var text = ""
        set(value) {
            field = value
            buttonText.text = value
        }

    var tint = 0
        set(value) {
            field = value
            buttonText.setTextColor(value)
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

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.layout_icon_button, this, true)
        container = findViewById((R.id.container))
        leadingIconView = findViewById(R.id.iv_leading_icon)
        tailContentView = findViewById(R.id.iv_tail_content)
        buttonText = findViewById(R.id.tv_text)
        separator = findViewById(R.id.separator)
    }

    private fun getStuffFromXML(attrs: AttributeSet?) {
        val data = context.obtainStyledAttributes(attrs, R.styleable.IconButton)
        text = data.getString(R.styleable.IconButton_text) ?: ""
        leadingIcon = data.getResourceId(R.styleable.IconButton_leadingIcon, 0)
        tint = data.getResourceId(R.styleable.IconButton_tint, buttonText.currentTextColor)
        gravity = data.getResourceId(R.styleable.IconButton_android_gravity, Gravity.NO_GRAVITY)
        data.recycle()
    }

    fun addTailContent(view: View){
        tailContentView.addView(view)
    }
}