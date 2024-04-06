package com.ikrom.music_club_classic.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ikrom.music_club_classic.R

@SuppressLint("ViewConstructor")
class MiniPlayerView : FrameLayout {
    private lateinit var container: View
    private lateinit var ivThumbnail: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvSubtitle: TextView
    private lateinit var btnPlayPause: ImageButton
    var title: String = ""
        set(value) {
            field = value
            tvTitle.text = value
        }

    var subTitle: String = ""
        set(value) {
            field = value
            tvSubtitle.text = value
        }

    var thumbnail: Int = 0
        set(value) {
            field = value
            ivThumbnail.setImageResource(value)
        }

    var thumbnailUrl: String = ""
        set(value) {
            field = value
            Glide
                .with(this)
                .load(value)
                .centerCrop()
                .into(ivThumbnail)
        }


    var btnIcon: Int = 0
        set(value) {
            field = value
            btnPlayPause.setImageResource(value)
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
        LayoutInflater.from(context).inflate(R.layout.component_mini_player, this, true)
        container = findViewById(R.id.container)
        ivThumbnail = findViewById(R.id.iv_thumbnail_card)
        tvTitle = findViewById(R.id.tv_title)
        tvSubtitle = findViewById(R.id.tv_subtitle)
        btnPlayPause = findViewById(R.id.btn_player)
    }

    @SuppressLint("CustomViewStyleable")
    private fun getStuffFromXML(attrs: AttributeSet?) {
        val data = context.obtainStyledAttributes(attrs, R.styleable.MiniPlayer)
        title = data.getString(R.styleable.MiniPlayer_title) ?: ""
        subTitle = data.getString(R.styleable.MiniPlayer_subtitle) ?: ""
        thumbnail = data.getResourceId(R.styleable.MiniPlayer_thumbnail, 0)
        btnIcon = data.getResourceId(R.styleable.MiniPlayer_btnIcon, 0)
        layoutBackgroundColor = data.getColor(R.styleable.MiniPlayer_layoutBackgroundColor, 0)
        data.recycle()
    }

    fun setOnButtonClickListener(onClickListener: OnClickListener) = btnPlayPause.setOnClickListener(onClickListener)
    fun setOnLayoutClickListener(onClickListener: OnClickListener) = container.setOnClickListener(onClickListener)

}


