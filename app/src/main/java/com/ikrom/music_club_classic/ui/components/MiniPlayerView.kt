package com.ikrom.music_club_classic.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ikrom.music_club_classic.R

@SuppressLint("ViewConstructor")
class MiniPlayerView : FrameLayout {
    private lateinit var container: LinearLayout
    private lateinit var thumbnailImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var subTitleTextView: TextView
    private lateinit var button: ImageButton
    var title: String = ""
        set(value) {
            field = value
            titleTextView.text = value
        }

    var subTitle: String = ""
        set(value) {
            field = value
            subTitleTextView.text = value
        }

    var thumbnail: Int = 0
        set(value) {
            field = value
            thumbnailImageView.setImageResource(value)
        }

    var btnIcon: Int = 0
        set(value) {
            field = value
            button.setImageResource(value)
        }

    var layoutBackgroundColor: Int = Color.TRANSPARENT
        set(value) {
            field = value
            container.background = ColorDrawable(value)
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
        LayoutInflater.from(context).inflate(R.layout.layout_mini_player, this, true)
        container = findViewById(R.id.linearLayout_container)
        thumbnailImageView = findViewById(R.id.iv_thumbnail_card)
        titleTextView = findViewById(R.id.tv_title)
        subTitleTextView = findViewById(R.id.tv_subtitle)
        button = findViewById(R.id.btn_player)
    }

    private fun getStuffFromXML(attrs: AttributeSet?) {
        val data = context.obtainStyledAttributes(attrs, R.styleable.MiniPlayer)
        title = data.getString(R.styleable.MiniPlayer_title) ?: ""
        subTitle = data.getString(R.styleable.MiniPlayer_subtitle) ?: ""
        thumbnail = data.getResourceId(R.styleable.MiniPlayer_thumbnail, 0)
        btnIcon = data.getResourceId(R.styleable.MiniPlayer_btnIcon, 0)
        layoutBackgroundColor = data.getColor(R.styleable.MiniPlayer_layoutBackgroundColor, 0)
        data.recycle()
    }

    fun setOnButtonClickListener(onClickListener: OnClickListener) = button.setOnClickListener(onClickListener)
    fun setOnLayoutClickListener(onClickListener: OnClickListener) = container.setOnClickListener(onClickListener)
    fun getThumbnailImageView() : ImageView = thumbnailImageView
}


