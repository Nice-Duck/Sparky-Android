package com.softsquared.niceduck.android.sparky.viewmodel

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.softsquared.niceduck.android.sparky.model.ScrapTemplateModel
import com.softsquared.niceduck.android.sparky.utill.MutableSingleLiveData
import com.softsquared.niceduck.android.sparky.utill.SingleLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ScrapTemplateViewModel {
    private val scrapTemplateModel = ScrapTemplateModel()

    private val _url = MutableSingleLiveData<String>()
    val url: SingleLiveData<String>
        get() = _url

    private val _title = MutableSingleLiveData<String>()
    val title: SingleLiveData<String>
    get() = _title

    private val _memo = MutableSingleLiveData<String>()
    val memo: SingleLiveData<String>
        get() = _memo

    private val _img = MutableSingleLiveData<String>()
    val img: SingleLiveData<String>
        get() = _img

    private val _tags = MutableSingleLiveData<ArrayList<Int>>()
    val tags: SingleLiveData<ArrayList<Int>>
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
            _url.setValue(url)

            if (ogMap["image"].isNullOrEmpty()) {
                _img.setValue(intent.getStringExtra("ogImage")?:"")
            } else {
                ogMap["image"]?.let { _img.setValue(it) }
            }
            if (ogMap["title"].isNullOrEmpty()) {
                _title.setValue(intent.getStringExtra("title") ?:"")
            } else {
                ogMap["title"]?.let { _title.setValue(it) }
            }
            if (ogMap["description"].isNullOrEmpty()) {
                _memo.setValue(intent.getStringExtra("ogDescription") ?:"")
            } else {
                ogMap["description"]?.let { _memo.setValue(it) }
            }
        }
    }

}