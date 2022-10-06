package com.softsquared.niceduck.android.sparky.viewmodel

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.softsquared.niceduck.android.sparky.model.ScrapTemplateModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ScrapTemplateViewModel {
    private val scrapTemplateModel = ScrapTemplateModel()

    private val _url = MutableLiveData<String>()
    val url: LiveData<String>
        get() = _url

    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
    get() = _title

    private val _memo = MutableLiveData<String>()
    val memo: LiveData<String>
        get() = _memo

    private val _img = MutableLiveData<String>()
    val img: LiveData<String>
        get() = _img

    private val _tags = MutableLiveData<ArrayList<Int>>()
    val tags: LiveData<ArrayList<Int>>
        get() = _tags

    fun getScrapData(intent: Intent?) {
        CoroutineScope(Dispatchers.IO).launch {
            if (intent?.action == Intent.ACTION_SEND) {
                if ("text/plain" == intent.type) {
                    scrapTemplateModel.handleSendText(intent).apply {
                        setDataView(intent, first, second)
                    }
                }
            }
        }
    }

    private suspend fun setDataView(intent: Intent, url: String, ogMap: Map<String, String>) {
        withContext(Dispatchers.Main) {
            _url.value = url

            if (ogMap["image"].isNullOrEmpty()) {
                _img.value = intent.getStringExtra("ogImage")?:""
            } else {
                _img.value = ogMap["image"]
            }
            if (ogMap["title"].isNullOrEmpty()) {
                _title.value = intent.getStringExtra("title") ?:""
            } else {
                _title.value = ogMap["title"]
            }
            if (ogMap["description"].isNullOrEmpty()) {
                _memo.value = intent.getStringExtra("ogDescription") ?:""
            } else {
                _memo.value = ogMap["description"]
            }
        }
    }

}