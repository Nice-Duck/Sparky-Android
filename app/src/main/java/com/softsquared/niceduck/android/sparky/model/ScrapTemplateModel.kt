package com.softsquared.niceduck.android.sparky.model

import android.util.Log.d
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.util.regex.Matcher
import java.util.regex.Pattern


class ScrapTemplateModel {
    private fun extractUrl(content: String?): String {
        return try {
            val regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
            val p: Pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
            val m: Matcher = p.matcher(content)
            if (m.find()) {
                m.group()
            } else ""
        } catch (e: Exception) {
            ""
        }
    }

    fun handleSendText(title: String?): Pair<String, Map<String, String>> {
        val ogMap = mutableMapOf<String, String>()
        var url = ""

        title?.let {

            url = extractUrl(it)
            d("test_url", url)

            val document = Jsoup.connect(url).userAgent("USER_AGENT_HERE").get()
            d("test_document", document.html())

            val elements = document.select("meta[property^=og:]")

            elements?.forEach { el ->
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
        }

        return Pair(url, ogMap)
    }

}