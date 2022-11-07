package com.softsquared.niceduck.android.sparky.view.web

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.webkit.URLUtil
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat.startActivity
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.ActivityWebViewBinding
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import com.softsquared.niceduck.android.sparky.view.scrap.ScrapTemplateActivity
import kotlinx.coroutines.NonCancellable.start
import java.net.URISyntaxException
import java.net.URL

class WebViewActivity : BaseActivity<ActivityWebViewBinding>(
    ActivityWebViewBinding::inflate
) {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = intent.getStringExtra("url")
        Log.d("테스트", url.toString())
        if (url != null) {
            binding.webView.loadUrl(url)
            binding.webView.apply {
                settings.javaScriptEnabled = true
                webViewClient = MyWebViewClient()
                settings.userAgentString = "Mozilla"

            }

            binding.webViewImgAdd.setOnClickListener {
                val intent = Intent(this, ScrapTemplateActivity::class.java)
                intent.putExtra("add", binding.webView.url)
                startActivity(intent)
                finish()
            }
        } else {
            showCustomToast("올바른 URL 형식이 아닙니다.")
            finish()
        }

        binding.webViewImgClose.setOnClickListener {
            finish()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK &&  binding.webView.canGoBack()) {
            binding.webView.goBack()
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    inner class MyWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (URLUtil.isNetworkUrl(url)) {
                // This is my web site, so do not override; let my WebView load the page
                return false
            }

            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                startActivity(this)
            }
            return true
        }
    }


}