package com.ikrom.music_club_classic.ui.screens.account

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.extensions.toDp
import com.ikrom.music_club_classic.ui.base_adapters.CompositeAdapter
import com.ikrom.music_club_classic.ui.base_adapters.item_decorations.MarginItemDecoration
import com.ikrom.music_club_classic.ui.components.IconButton

class AccountFragment : Fragment() {
    private val adapter = CompositeAdapter.Builder()
        .add(HeaderAdapter())
        .add(ButtonsAdapter())
        .build()
    private lateinit var recycleView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        bindViews(view)
        setupAdapterData()
        setupAdapter(view)
        return view
    }

    private fun bindViews(view: View){
        recycleView = view.findViewById(R.id.rv_content)
    }

    private fun setupAdapter(view: View){
        recycleView.layoutManager = LinearLayoutManager(view.context)
        recycleView.adapter = adapter
        recycleView.addItemDecoration(MarginItemDecoration(80, 80, 80))
    }

    @SuppressLint("ResourceAsColor")
    private fun setupAdapterData(){
        adapter.setItems(
            listOf(
                HeaderDelegateItem(
                    "https://yandex-images.clstorage.net/Ki9Wp6383/bcd401IeL/xGSVeKXLfRxi8kjcCY51eqrp23L-tf5jSwqrUTM00-39NNUeTLzDtPBDhrY2ovgomwT-90bLliO1NhSAeEjer9F9zHmUxnFBM7dQoSC-aV66lPcH3rTiEgMm9zxqUc_dzR2dC_0NHzRx7mcz13ZUmZphk6tD7qM6hd75iXTxnmuY7FUQLaNQaD5CCbS1ntFSLkxD-rbE0qIbxgqgdqHm9Ug4XAPdFaeUNYKf_ydm0AliPfPf32vfvgFmsMkMDe534KAtkH2n-GRyXomwTeL1WjJc02JO2NJqV4ayrOptnvGoYISbFW03jL0CB7P_9qCJvvWbaz9b3yJZdsg99HFSb8QkARA934QMEmpt2OFKJVIWRK9qstBych8OHpj6hV6hTDiMk5EV14yt0pczN0Z4YRohb9_Lg4fWARYMEYgdsi_88DHoOd8YQIZKvTzZwsVy8lznokpY8ma_goZEilUCOdgM8PdRuYv0mTK_829-FGGe_SOP3ytDMu0SlOFw4QZnhGhZPHkHPJzqgmXUCarZAn7QW8bSfBZ6dxI-OM7RxqH4DLzjWd3D9P3Wd1fXSuTlJnH3swvPs5rFdiBFCNkKv1wA8WQ9ZwCQ4iqZQOXGhQqmuAsS3niGZoNu9qyiaa55lPyEGx05FxBlrgtzx_6Ycfa9H-NH61PaXcrkXewBCiuAJMU0YTe0eEZ23UDx7mlWNtT_htqAKgrfoi5wyr1O4WCARBsZGROYkV5Pn6vyvJ122fcj2z8X4unuvKEUCZ5TCHSleHWv7LR2vk10ObqdSq7sN5pmdB4mqxaWmLa13nWUoHBrCamjAF0qhx_PiuiJplUX6xs_0zq5tiQF3IkO84hkjay9h-i04kLRgDE64SKKgEPuSuDy6qeGpuxagQbhHEjU-03dp1hVuufL98LYvcLpz5uDt89-FeqAPbCNpv_kgNkcvbscGAI2icgN3hHWklgn4l5Eln5z7n48Qi1E",
                    "Anne Usova"
                ),
                ButtonsDelegateItem(
                    listOf(
                        IconButton(requireContext()).also {
                            it.leadingIcon = R.drawable.ic_profile
                            it.text = "Language"
                            it.addTailContent(
                                TextView(requireContext()).also { textView ->
                                    textView.textSize = 16f
                                    textView.text = "English"
                                    textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_50))
                                }
                            )
                        },
                        IconButton(requireContext()).also {
                            it.leadingIcon = R.drawable.ic_profile
                            it.text = "Storage"
                            it.addTailContent(
                                ImageView(requireContext()).also { imageView ->
                                    imageView.setImageResource(R.drawable.ic_array_more)
                                    imageView.maxWidth = 32.toDp(requireContext())
                                    imageView.setColorFilter(R.color.grey_50)
                                }
                            )
                        },
                        IconButton(requireContext()).also {
                            it.leadingIcon = R.drawable.ic_profile
                            it.text = "Audio quality"
                            it.addTailContent(
                                TextView(requireContext()).also { textView ->
                                    textView.textSize = 16f
                                    textView.text = "Auto"
                                    textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_50))
                                }
                            )
                        }
                    )
                ),
                ButtonsDelegateItem(
                    listOf(
                        IconButton(requireContext()).also {
                            it.leadingIcon = R.drawable.ic_profile
                            it.text = "Clean search history"
                        },
                        IconButton(requireContext()).also {
                            it.leadingIcon = R.drawable.ic_profile
                            it.text = "Clean listened history"
                        },
                    )
                ),
                ButtonsDelegateItem(
                    listOf(
                        IconButton(requireContext()).also {
                            it.text = "Exit"
                            it.tint = Color.RED
                            it.gravity = Gravity.CENTER
                        },
                    )
                )
            )
        )
    }
}
