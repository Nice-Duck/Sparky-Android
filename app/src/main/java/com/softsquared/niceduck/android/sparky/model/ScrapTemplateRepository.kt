package com.softsquared.niceduck.android.sparky.model

import android.util.Log.d


class ScrapTemplateRepository {
    private val scrapTemplateModel = ScrapTemplateModel()

    suspend fun handleSendText(title: String?): Pair<String, Map<String, String>> {
        d("테스트3", "scrapTemplateModel.handleSendText(title)")
        return scrapTemplateModel.handleSendText(title)
    }
}