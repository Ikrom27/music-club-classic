package com.ikrom.music_club_classic.ui.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.ikrom.music_club_classic.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebAuthorizationFragment : Fragment() {
    private lateinit var webView: WebView
    private lateinit var navController: NavController
    private lateinit var btnBack: ImageButton
    private lateinit var urlTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_web_authorization, container, false)
        navController = requireParentFragment().findNavController()
        bindViews(view)
        setContent()
        openPage()
        return view
    }

    fun bindViews(view: View){
        btnBack = view.findViewById(R.id.btn_back)
        urlTextView = view.findViewById(R.id.tv_url)
        webView = view.findViewById(R.id.web_view_authorization)
    }

    fun setContent(){
        urlTextView.text = URL
        btnBack.setOnClickListener{
            navController.navigateUp()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun openPage(){
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                view.loadUrl("javascript:window.Android.onUrlChange(window.location.href);")
                if (url.startsWith("https://music.youtube.com")) {
                    val cookies = CookieManager.getInstance().getCookie(url)

                    navController.navigateUp()
                }
            }
        }
        webView.loadUrl(AUTH_URL)
    }

    companion object {
        private const val AUTH_URL = "https://accounts.google.com/ServiceLogin?ltmpl=music&service=youtube&passive=true&continue=https%3A%2F%2Fwww.youtube.com%2Fsignin%3Faction_handle_signin%3Dtrue%26next%3Dhttps%253A%252F%252Fmusic.youtube.com%252F"
        private const val URL = "music.youtube.com"
    }
}