package com.softsquared.niceduck.android.sparky.view.web

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.ActivityWebViewBinding
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import com.softsquared.niceduck.android.sparky.view.scrap.ScrapTemplateActivity

class WebViewActivity : BaseActivity<ActivityWebViewBinding>(
    ActivityWebViewBinding::inflate
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = intent.getStringExtra("url")
        if (url != null) {
            binding.webView.loadUrl(url)
            binding.webView.apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                getSettings().setUserAgentString(this.getSettings().getUserAgentString() + " Android_Mobile")
            }

            binding.webViewImgAdd.setOnClickListener {
                val intent = Intent(this, ScrapTemplateActivity::class.java)
                intent.putExtra("add", binding.webView.url)
                startActivity(intent)
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

}