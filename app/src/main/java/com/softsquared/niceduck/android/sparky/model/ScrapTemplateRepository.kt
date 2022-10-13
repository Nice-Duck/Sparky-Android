package com.softsquared.niceduck.android.sparky.model

import android.util.Log.d


class ScrapTemplateRepository {
    private val scrapTemplateModel = ScrapTemplateModel()

    suspend fun handleSendText(title: String?): Pair<String, Map<String, String>> {
        return scrapTemplateModel.handleSendText(title)
    }
}