package com.ikrom.music_club_classic.ui.screens

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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.base_adapter.model.AdapterItem
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.extensions.toDp
import com.ikrom.music_club_classic.ui.adapters.delegates.ButtonsGroupAdapter
import com.ikrom.music_club_classic.ui.adapters.delegates.ButtonsGroupItem
import com.ikrom.music_club_classic.ui.adapters.delegates.AccountHeaderAdapter
import com.ikrom.music_club_classic.ui.adapters.delegates.AccountHeaderItem
import com.ikrom.music_club_classic.ui.components.IconButton
import com.ikrom.music_club_classic.viewmodel.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {
    private val adapter = com.ikrom.base_adapter.CompositeAdapter.Builder()
        .add(AccountHeaderAdapter())
        .add(ButtonsGroupAdapter())
        .build()
    private lateinit var recycleView: RecyclerView
    private lateinit var navController: NavController

    private val viewModel: AccountViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        navController = requireParentFragment().findNavController()
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
        recycleView.addItemDecoration(
            com.ikrom.base_adapter.item_decorations.MarginItemDecoration(
                80,
                80,
                80
            )
        )
    }

    @SuppressLint("ResourceAsColor")
    private fun setupAdapterData(){
        adapter.setItems(
            listOf(
                getHeader(),
                getMainButtons(),
                getCleanButtons(),
                getExitButton()
            )
        )
    }

    fun getHeader(): AdapterItem {
//        viewModel.getAccountInfo().observe(viewLifecycleOwner) {
//            if (it != null){
//                adapter.updateItem(0,
//                    HeaderDelegateItem(
//                        "",
//                        it.name
//                    ))
//            }
//        }
        return AccountHeaderItem(
            "https://img.joinfo.com/i/2017/04/800x0/58df809bd1e8a.jpg",
            "Donald John Trump")
    }

    fun getMainButtons(): AdapterItem {
        return ButtonsGroupItem(
            listOf(
                IconButton(requireContext()).also {
                    it.leadingIcon = R.drawable.ic_language
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
                    it.leadingIcon = R.drawable.ic_storage
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
                    it.leadingIcon = R.drawable.ic_audio_quality
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
        )
    }

    fun getCleanButtons(): AdapterItem {
        return ButtonsGroupItem(
            listOf(
                IconButton(requireContext()).also {
                    it.leadingIcon = R.drawable.ic_clean
                    it.text = "Clean search history"
                },
                IconButton(requireContext()).also {
                    it.leadingIcon = R.drawable.ic_clean
                    it.text = "Clean listened history"
                },
                IconButton(requireContext()).also {
                    it.leadingIcon = com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark
                    it.text = "Sign in"
                    it.setOnClickListener {
                        navController.navigate(R.id.action_profileFragment_to_webAuthorizationFragment)
                    }
                })
            )
    }

    fun getExitButton(): AdapterItem {
        return ButtonsGroupItem(
            listOf(
                IconButton(requireContext()).also {
                    it.text = "Exit"
                    it.tint = Color.RED
                    it.gravity = Gravity.CENTER
                },
            )
        )
    }
}
