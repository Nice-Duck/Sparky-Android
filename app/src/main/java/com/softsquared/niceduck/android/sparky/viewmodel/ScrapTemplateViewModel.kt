package com.softsquared.niceduck.android.sparky.viewmodel

import android.util.Log.d
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softsquared.niceduck.android.sparky.model.*
import com.softsquared.niceduck.android.sparky.utill.BaseResponse
import com.softsquared.niceduck.android.sparky.utill.MutableSingleLiveData
import com.softsquared.niceduck.android.sparky.utill.SingleLiveData
import com.softsquared.niceduck.android.sparky.view.scrap.ItemEvent
import com.softsquared.niceduck.android.sparky.view.scrap.ScrapTemplateRecyclerviewAdapter
import com.softsquared.niceduck.android.sparky.view.scrap.TagAddRecyclerviewAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class ScrapTemplateViewModel : ViewModel(), ItemEvent {
    private val scrapTemplateRepository = ScrapTemplateRepository()

    // 저장할 스크랩 데이터
    val url = MutableSingleLiveData<String>()
    val title = MutableSingleLiveData<String>()
    val memo = MutableSingleLiveData<String>()
    val img = MutableSingleLiveData<String>()
    val tags = ArrayList<Int>()

    val lastTags = MutableLiveData<ArrayList<Tag>>()
    var updatedList = ArrayList<Tag>()

    private val tagColorList = listOf(
        "#FFDDDA", "#FFE8D3", "#FFFDCC", "#D8F5D6", "#D5E7E0", "#DFF1F5",
        "#DFF1F5", "#E5DDF3", "#F1E0EB", "#FFE6F7", "#E5DBE0", "#DFDFDF"
    )
    val tagColor = MutableSingleLiveData<String>()

    // 템플릿 화면 어댑터
    val scrapTemplateRecyclerviewAdapter: ScrapTemplateRecyclerviewAdapter

    // 템플릿 화면 어댑터 데이터셋
    val scrapTemplateDataSet: MutableLiveData<ArrayList<Tag>> = MutableLiveData()

    // 바텀 시트 화면 어댑터
    val tagAddRecyclerviewAdapter: TagAddRecyclerviewAdapter

    // 바텀 시트 콜
    private val _showBottomSheetCall = MutableSingleLiveData<Boolean>()
    val showBottomSheetCall: SingleLiveData<Boolean>
        get() = _showBottomSheetCall

    private val _hideBottomSheetCall = MutableSingleLiveData<Boolean>()
    val hideBottomSheetCall: SingleLiveData<Boolean>
        get() = _hideBottomSheetCall

    // 바텀 시트 화면 태그 선택 이벤트
    private val _selectItemCall = MutableSingleLiveData<Boolean>()
    val selectItemCall: SingleLiveData<Boolean>
        get() = _selectItemCall

    // 태그 저장 결과
    private val _tagSaveResponse = MutableSingleLiveData<TagResponse>()
    val tagSaveResponse: SingleLiveData<TagResponse>
        get() = _tagSaveResponse
    private val _tagSaveFailure = MutableSingleLiveData<Int>()
    val tagSaveFailure: SingleLiveData<Int>
        get() = _tagSaveFailure

    // 태그 저장
    fun postTagSave(request: TagRequest) {
        viewModelScope.launch {
            val response = scrapTemplateRepository.postTagSave(request)

            if (response.isSuccessful) {
                response.body()?.let { _tagSaveResponse.setValue(it) }
            } else {
                _tagSaveFailure.setValue(response.code())
            }
        }
    }

    // 태그 조회
    private val _tagLastLoadResponse = MutableSingleLiveData<TagLastLoadResponse>()
    val tagLastLoadResponse: SingleLiveData<TagLastLoadResponse>
        get() = _tagLastLoadResponse
    private val _tagLastLoadFailure = MutableSingleLiveData<Int>()
    val tagLastLoadFailure: SingleLiveData<Int>
        get() = _tagLastLoadFailure

    private fun getTagLastLoad() {
        viewModelScope.launch {
            val response = scrapTemplateRepository.getTagLastLoad()

            if (response.isSuccessful) {
                response.body()?.let { _tagLastLoadResponse.setValue(it) }
            } else {
                _tagLastLoadFailure.setValue(response.code())
            }
        }
    }

    // 스크랩 데이터 결과
    private val _setDataViewCall = MutableSingleLiveData<Pair<String, Map<String, String>>>()
    val setDataViewCall: SingleLiveData<Pair<String, Map<String, String>>>
        get() = _setDataViewCall

    // 크롤링
    fun getScrapData(title: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            scrapTemplateRepository.handleSendText(title).apply {
                setDataView(this)
            }
        }
    }

    // 크롤링 결과 처리
    private suspend fun setDataView(result: Pair<String, Map<String, String>>) {
        withContext(Dispatchers.Main) {
            _setDataViewCall.setValue(result)
        }
    }

    // 스크랩 저장
    private val _scrapStoreResponse = MutableSingleLiveData<BaseResponse>()
    val scrapStoreResponse: SingleLiveData<BaseResponse>
        get() = _scrapStoreResponse
    private val _scrapStoreFailure = MutableSingleLiveData<Int>()
    val scrapStoreFailure: SingleLiveData<Int>
        get() = _scrapStoreFailure

    fun postScrapStore() {
        viewModelScope.launch {
            scrapTemplateRepository.postStoreScrap(
                ScrapStoreRequest(
                    img.getValue(),
                    memo.getValue(),
                    url.getValue(),
                    tags,
                    title.getValue()
                )
            )
        }
    }

    init {

        val tags = arrayListOf(Tag("", "", 0))

        scrapTemplateRecyclerviewAdapter = ScrapTemplateRecyclerviewAdapter(this) // 템플릿 화면 어댑터 생성
        scrapTemplateDataSet.value = tags // 탬플릿 화면 어댑터 데이터
        tagAddRecyclerviewAdapter = TagAddRecyclerviewAdapter(this) // 바텀 시트 어댑터 생성
        getTagLastLoad()
    }

    fun randomColor(): String {
        val randomIndex = Random.nextInt(tagColorList.size)

        return tagColorList[randomIndex]
    }

    override fun addItem() {
        _showBottomSheetCall.setValue(true)
    }

    override fun removeItem(position: Int) {
        val newList = scrapTemplateRecyclerviewAdapter.currentList.toMutableList()
        newList.removeAt(position)
        scrapTemplateDataSet.value = newList as ArrayList
    }

    override fun selectItem(position: Int) {
        val newList = tagAddRecyclerviewAdapter.currentList.toMutableList()
        val i = scrapTemplateDataSet.value!!.size - 1

        d(
            "선택 테스트",
            Tag(
                newList[position].color,
                newList[position].name,
                newList[position].tagId
            ).toString()
        )

        scrapTemplateDataSet.value!!.add(
            i,
            Tag(
                newList[position].color,
                newList[position].name,
                newList[position].tagId
            )
        )

        val newTagList = scrapTemplateDataSet.value!!

        scrapTemplateDataSet.value = newTagList

        _hideBottomSheetCall.setValue(true)
    }
}
