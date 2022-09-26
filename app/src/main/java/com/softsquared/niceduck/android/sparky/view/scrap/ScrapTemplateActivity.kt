package com.softsquared.niceduck.android.sparky.view.scrap

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log.d
import com.bumptech.glide.Glide
import com.softsquared.niceduck.android.sparky.databinding.ActivityScrapTemplateBinding
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.util.regex.Matcher
import java.util.regex.Pattern

class ScrapTemplateActivity : BaseActivity<ActivityScrapTemplateBinding>(ActivityScrapTemplateBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: 아키텍처 패턴 적용 및 코루틴 코드 개선 예정
        CoroutineScope(Dispatchers.Main).launch {
            getScrapData()
        }
    }

    override fun onResume() {
        super.onResume()


    }

    private suspend fun getScrapData() {
        CoroutineScope(Dispatchers.IO).launch {
            if (intent?.action == Intent.ACTION_SEND) {
                if ("text/plain" == intent.type) {
                    handleSendText(intent) // Handle text being sent
                } else if (intent.type?.startsWith("image/") == true) {
                    handleSendImage(intent) // Handle single image being sent
                }
            } else {
                // Handle other intents, such as being started from the home screen
            }
        }
    }

    private suspend fun handleSendText(intent: Intent) {
        val ogMap = mutableMapOf<String, String>()
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            // Update UI to reflect text being shared

            val url = extractUrl(it)

            d("test_url", url?:" ")
            val document = Jsoup.connect(url).get()
            val elements = document.select("meta[property^=og:]")
            elements?.let { elements ->
                elements.forEach { el ->
                    when (el.attr("property")) {
                        "og:url" -> {
                            el.attr("content")?.let { content ->
                                ogMap.put("url", content)
                            }
                        }
                        "og:site_name" -> {
                            el.attr("content")?.let { content ->
                                ogMap.put("siteName", content)
                            }
                        }
                        "og:title" -> {
                            el.attr("content")?.let { content ->
                                ogMap.put("title", content)
                            }
                        }
                        "og:description" -> {
                            el.attr("content")?.let { content ->
                                ogMap.put("description", content)
                            }
                        }
                        "og:image" -> {
                            ogMap.put("image", el.attr("content"))
                        }
                    }
                }

                d("test_ogMap", "${ogMap.values}")
                d("test_intent_extras", "${intent.extras}")
            }
            if (url != null) {
                setDataView(it ,url, ogMap)
            }
        }
    }

    suspend fun setDataView(text: String, url: String, ogMap: Map<String, String>) {
        withContext(Dispatchers.Main) {
            Glide.with(this@ScrapTemplateActivity).load(ogMap.get("image")).centerCrop().into(binding.scrapTemplateImgThumbnail)
            if (ogMap.get("title").isNullOrEmpty()) {
                binding.scrapTemplateTxtTitle.setText(text)
            } else {
                binding.scrapTemplateTxtTitle.setText(ogMap.get("title"))
            }
        }
    }

    private fun handleSendImage(intent: Intent) {
        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
            // Update UI to reflect image being shared
        }
    }


    private fun extractUrl(content: String?): String? {
        return try {
            val REGEX = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
            val p: Pattern = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE)
            val m: Matcher = p.matcher(content)
            if (m.find()) {
                m.group()
            } else ""
        } catch (e: Exception) {
            ""
        }
    }
}