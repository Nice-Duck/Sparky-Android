package com.softsquared.niceduck.android.sparky.viewmodel

import android.content.Intent
import android.util.Log
import android.util.Log.d
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide.init
import com.softsquared.niceduck.android.sparky.model.ScrapTemplateModel
import com.softsquared.niceduck.android.sparky.model.ScrapTemplateRepository
import com.softsquared.niceduck.android.sparky.model.Tag
import com.softsquared.niceduck.android.sparky.utill.Event
import com.softsquared.niceduck.android.sparky.utill.MutableSingleLiveData
import com.softsquared.niceduck.android.sparky.utill.SingleLiveData
import com.softsquared.niceduck.android.sparky.view.scrap.ItemEvent
import com.softsquared.niceduck.android.sparky.view.scrap.ScrapTemplateFooterViewHolder
import com.softsquared.niceduck.android.sparky.view.scrap.ScrapTemplateRecyclerviewAdapter
import com.softsquared.niceduck.android.sparky.view.scrap.TagAddRecyclerviewAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ScrapTemplateViewModel: ViewModel(), ItemEvent {
    private val scrapTemplateRepository = ScrapTemplateRepository()

    private lateinit var scrapTemplateRecyclerviewAdapter: ScrapTemplateRecyclerviewAdapter
    private val _scrapTemplateDataSet: MutableLiveData<ArrayList<Tag>> = MutableLiveData()
    private val scrapTemplateDataSet: LiveData<ArrayList<Tag>>
        get() = _scrapTemplateDataSet

    private lateinit var tagAddRecyclerviewAdapter: TagAddRecyclerviewAdapter
    private val _tagAddDataSet: MutableLiveData<ArrayList<Tag>> = MutableLiveData()
    private val tagAddDataSet: LiveData<ArrayList<Tag>>
        get() = _tagAddDataSet

    private val _showBottomSheetCall = MutableSingleLiveData<Boolean>()
    val showBottomSheetCall: SingleLiveData<Boolean>
        get() = _showBottomSheetCall

    private val _selectItemCall = MutableSingleLiveData<Boolean>()
    val selectItemCall: SingleLiveData<Boolean>
        get() = _selectItemCall

    val url = MutableSingleLiveData<String>()

    val title = MutableSingleLiveData<String>()

    val memo = MutableSingleLiveData<String>()

    val img = MutableSingleLiveData<String>()


    private val _setDataViewCall = MutableSingleLiveData<Pair<String, Map<String, String>>>()
    val setDataViewCall: SingleLiveData<Pair<String, Map<String, String>>>
        get() = _setDataViewCall


    fun getScrapData(title: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            scrapTemplateRepository.handleSendText(title).apply {
                setDataView(this)
            }
        }
    }

    private suspend fun setDataView(result: Pair<String, Map<String, String>>) {
        withContext(Dispatchers.Main) {
            _setDataViewCall.setValue(result)
        }

    }

    init {

        // 모델-레포를 통해 데이터 받아오기
        // getDataSet 함수 구현
        // tags 테스트 데이터
        val tags = arrayListOf<Tag>(Tag("#BEBDBD", "디자인", 0),
            Tag("#BEBDBD", "IT", 1),
            Tag("#BEBDBD", "안드로이드", 2),
            Tag("#BEBDBD", "자료구조", 3),
            Tag("#BEBDBD", "자바", 4))

        setScrapTemplateAdapter(ScrapTemplateRecyclerviewAdapter(this))
        setScrapTemplateDataSet(tags)
    }

    private fun setScrapTemplateDataSet(newList : List<Tag>) {
        this._scrapTemplateDataSet.value = newList as ArrayList<Tag>
        scrapTemplateRecyclerviewAdapter.submitList(newList)
    }

    fun getScrapTemplateData() = scrapTemplateDataSet

    fun getScrapTemplateAdapter() = scrapTemplateRecyclerviewAdapter

    private fun setScrapTemplateAdapter(customAdapter : ScrapTemplateRecyclerviewAdapter) {
        this.scrapTemplateRecyclerviewAdapter = customAdapter
    }

    private fun setTagAddDataSet(newList : List<Tag>) {
        this._tagAddDataSet.value = newList as ArrayList<Tag>
        tagAddRecyclerviewAdapter.submitList(newList)
    }

    fun getTagAddData() = tagAddDataSet

    private fun setTagAddAdapter(customAdapter : TagAddRecyclerviewAdapter) {
        this.tagAddRecyclerviewAdapter = customAdapter
    }

    fun getTagAddAdapter() = tagAddRecyclerviewAdapter

    override fun addItem() {
/*        val newList = customAdapter.currentList.toMutableList()
        newList.add(tag)
        setDataSet(newList)*/

        val tags = arrayListOf<Tag>(Tag("#BEBDBD", "디자인", 0),
            Tag("#BEBDBD", "IT", 1),
            Tag("#BEBDBD", "안드로이드", 2),
            Tag("#BEBDBD", "자료구조", 3),
            Tag("#BEBDBD", "자바", 4))
        setTagAddAdapter(TagAddRecyclerviewAdapter(this))
        setTagAddDataSet(tags)
        // 바텀 네비게이션 콜
        _showBottomSheetCall.setValue(true)

    }


    override fun removeItem(position: Int) {
        val newList = scrapTemplateRecyclerviewAdapter.currentList.toMutableList()
        newList.removeAt(position)
        setScrapTemplateDataSet(newList)
    }

    override fun getScrapTemplateDataSet(): ArrayList<Tag>? {
        return getScrapTemplateData().value
    }

    override fun selectItem(position: Int) {

    }
}