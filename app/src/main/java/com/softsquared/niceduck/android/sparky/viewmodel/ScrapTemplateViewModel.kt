package com.softsquared.niceduck.android.sparky.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import com.softsquared.niceduck.android.sparky.config.ApplicationClass.Companion.sSharedPreferences
import com.softsquared.niceduck.android.sparky.model.*
import com.softsquared.niceduck.android.sparky.utill.*
import com.softsquared.niceduck.android.sparky.view.scrap.ItemEvent
import com.softsquared.niceduck.android.sparky.view.scrap.ScrapTemplateRecyclerviewAdapter
import com.softsquared.niceduck.android.sparky.view.scrap.TagAddRecyclerviewAdapter
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.BufferedSink
import org.jsoup.Connection
import java.io.File
import kotlin.random.Random

class ScrapTemplateViewModel : ViewModel(), ItemEvent {
    private val scrapTemplateRepository = ScrapTemplateRepository()

    // 저장할 스크랩 데이터
    var url: String = ""
    var memo: String = ""
    val title = MutableSingleLiveData<String>()
    val subTitle = MutableSingleLiveData<String>()
    val img = MutableSingleLiveData<String>()
    val tags = ArrayList<Int>()

    val lastTags = MutableLiveData<ArrayList<TagsResponse>>()
    var updatedList = ArrayList<TagsResponse>()

    private val tagColorList = listOf(
        "#FFDDDA", "#FFE8D3", "#FFFDCC", "#D8F5D6", "#D5E7E0", "#DFF1F5",
        "#DFF1F5", "#E5DDF3", "#F1E0EB", "#FFE6F7", "#E5DBE0", "#DFDFDF"
    )
    val tagColor = MutableSingleLiveData<String>()

    // 템플릿 화면 어댑터
    val scrapTemplateRecyclerviewAdapter: ScrapTemplateRecyclerviewAdapter

    // 템플릿 화면 어댑터 데이터셋
    val scrapTemplateDataSet: MutableLiveData<ArrayList<TagsResponse>> = MutableLiveData()

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
    private val _tagSaveFailure = MutableSingleLiveData<BaseResponse>()
    val tagSaveFailure: SingleLiveData<BaseResponse>
        get() = _tagSaveFailure

    // 태그 저장
    fun postTagSave(request: TagRequest) {
        viewModelScope.launch {
            try {
                val response = scrapTemplateRepository.postTagSave(request)

                if (response.isSuccessful) {
                    response.body()?.let { _tagSaveResponse.setValue(it) }
                } else {
                    response.errorBody()?.let {
                        val errorBody = NetworkUtil.getErrorResponse(it)
                        errorBody?.let { error -> _tagSaveFailure.setValue(error) }
                    }
                }
            } catch (e: Exception) {
                e.message?.let { Log.d("test", it) }
            }
        }
    }



    // 태그 조회
    private val _tagLastLoadResponse = MutableLiveData<TagLastLoadResponse>()
    val tagLastLoadResponse: LiveData<TagLastLoadResponse>
        get() = _tagLastLoadResponse
    private val _tagLastLoadFailure = MutableSingleLiveData<BaseResponse>()
    val tagLastLoadFailure: SingleLiveData<BaseResponse>
        get() = _tagLastLoadFailure

    fun getTagLastLoad() {
        viewModelScope.launch {
            try {
                val response = scrapTemplateRepository.getTagLastLoad()

                if (response.isSuccessful) {
                    response.body()?.let { _tagLastLoadResponse.setValue(it) }
                } else {
                    response.errorBody()?.let {
                        val errorBody = NetworkUtil.getErrorResponse(it)
                        errorBody?.let { error -> _tagLastLoadFailure.setValue(error) }
                    }
                }
            } catch (e: Exception) {
                e.message?.let { Log.d("test", it) }
            }
        }
    }

    // 스크랩 데이터 결과
    private val _setDataViewCall = MutableSingleLiveData<Pair<String, Map<String, String>>>()
    val setDataViewCall: SingleLiveData<Pair<String, Map<String, String>>>
        get() = _setDataViewCall
    private val _setDataViewCallFailure = MutableSingleLiveData<String>()
    val setDataViewCallFailure: SingleLiveData<String>
        get() = _setDataViewCallFailure

    // 크롤링
    fun getScrapData(title: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                scrapTemplateRepository.handleSendText(title).apply {
                    setDataView(this)
                }
            } catch (e: Exception) {
                _setDataViewCallFailure.postValue(e.message?: "crawling error")
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
    private val _scrapStoreFailure = MutableSingleLiveData<BaseResponse>()
    val scrapStoreFailure: SingleLiveData<BaseResponse>
        get() = _scrapStoreFailure

    fun postScrapStore() {
        viewModelScope.launch {
            try {
                val response = scrapTemplateRepository.postStoreScrap(
                    ScrapStoreRequest(
                        img.getValue(),
                        memo,
                        url,
                        tags,
                        title.getValue(),
                        subTitle.getValue()
                    )
                )

                if (response.isSuccessful) {
                    response.body()?.let { _scrapStoreResponse.setValue(it) }
                } else {
                    response.errorBody()?.let {
                        val errorBody = NetworkUtil.getErrorResponse(it)
                        errorBody?.let { error -> _scrapStoreFailure.setValue(error) }
                    }
                }
            } catch (e: Exception) {
                e.message?.let { Log.d("test", it) }
            }


        }
    }


    // 스크랩 수정
    private val _scrapUpdateResponse = MutableSingleLiveData<BaseResponse>()
    val scrapUpdateResponse: SingleLiveData<BaseResponse>
        get() = _scrapUpdateResponse
    private val _scrapUpdateFailure = MutableSingleLiveData<BaseResponse>()
    val scrapUpdateFailure: SingleLiveData<BaseResponse>
        get() = _scrapUpdateFailure


    inner class BitmapRequestBody(private val bitmap: Bitmap) : RequestBody() {
        override fun contentType(): MediaType = "image/jpeg".toMediaType()
        override fun writeTo(sink: BufferedSink) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 99, sink.outputStream())
        }
    }
    var image: Bitmap? = null

    fun patchScrap(scrapId: String) {
        Log.d("수정 테스트", "$title, $subTitle, $memo, $url, $tags, $image")
        viewModelScope.launch {
            try {
                val formTitle = title.getValue()?.let { FormDataUtil.getBody("title", it) }
                val formSubTitle = subTitle.getValue()?.let { FormDataUtil.getBody("subTitle", it) }
                val formMemo = FormDataUtil.getBody("memo", memo)
                val formUrl = FormDataUtil.getBody("scpUrl", url)
                val stringTags = tags.toString().replace("[", "").replace("]", "")
                val formTags = FormDataUtil.getBody("tags", stringTags)

                val bitmapRequestBody = image?.let { BitmapRequestBody(it) }
                val bitmapMultipartBody: MultipartBody.Part? =
                    if (bitmapRequestBody == null) null
                    else MultipartBody.Part.createFormData("image", "sparky", bitmapRequestBody)


                val response = scrapTemplateRepository.patchScrap(
                    scrapId,
                    formTitle,
                    formSubTitle,
                    formMemo,
                    formUrl,
                    formTags,
                    bitmapMultipartBody
                )

                if (response.isSuccessful) {
                    response.body()?.let { _scrapUpdateResponse.setValue(it) }
                } else {
                    response.errorBody()?.let {
                        val errorBody = NetworkUtil.getErrorResponse(it)
                        errorBody?.let { error -> _scrapUpdateFailure.setValue(error) }
                    }
                }
            } catch (e: Exception) {
                e.message?.let { Log.d("test", it) }
            }
        }
    }

    init {
        val tags = arrayListOf(TagsResponse("", "", 0))

        scrapTemplateRecyclerviewAdapter = ScrapTemplateRecyclerviewAdapter(this) // 템플릿 화면 어댑터 생성
        scrapTemplateDataSet.value = tags // 탬플릿 화면 어댑터 데이터
        tagAddRecyclerviewAdapter = TagAddRecyclerviewAdapter(this) // 바텀 시트 어댑터 생성
        getTagLastLoad()
    }

    fun randomColor(): String {
        return tagColorList.shuffled()[0]
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


        scrapTemplateDataSet.value!!.add(
            i,
            TagsResponse(
                newList[position].color,
                newList[position].name,
                newList[position].tagId
            )
        )

        val newTagList = scrapTemplateDataSet.value!!

        scrapTemplateDataSet.value = newTagList

        _hideBottomSheetCall.setValue(true)
    }

    // 토큰 갱신
    private val _reissueAccessTokenResponse: MutableSingleLiveData<TokenResponse> = MutableSingleLiveData()
    val reissueAccessTokenResponse: SingleLiveData<TokenResponse>
        get() = _reissueAccessTokenResponse

    private val _reissueAccessTokenFailure = MutableSingleLiveData<Int>()
    val reissueAccessTokenFailure: SingleLiveData<Int>
        get() = _reissueAccessTokenFailure

    // 토큰 갱신
    suspend fun postReissueAccessToken(): Int {
        val editor = sSharedPreferences.edit()
        editor.putString(ApplicationClass.X_ACCESS_TOKEN, null)
        editor.apply()

        val scope = viewModelScope.async {
            try {
                val response = scrapTemplateRepository.postReissueAccessToken()

                if (response.isSuccessful) {
                    response.body()?.let {
                        _reissueAccessTokenResponse.setValue(it)

                    }
                    1
                } else {
                    _reissueAccessTokenFailure.setValue(response.code())
                    0
                }
            } catch (e: Exception) {
                e.message?.let { Log.d("test", it) }
                0
            }
        }
        return scope.await()
    }
}
