package com.softsquared.niceduck.android.sparky.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softsquared.niceduck.android.sparky.config.ApplicationClass.Companion.X_ACCESS_TOKEN
import com.softsquared.niceduck.android.sparky.config.ApplicationClass.Companion.sSharedPreferences
import com.softsquared.niceduck.android.sparky.model.*
import com.softsquared.niceduck.android.sparky.utill.BaseResponse
import com.softsquared.niceduck.android.sparky.utill.MutableSingleLiveData
import com.softsquared.niceduck.android.sparky.utill.NetworkUtil
import com.softsquared.niceduck.android.sparky.utill.SingleLiveData
import com.softsquared.niceduck.android.sparky.view.main.fragment.MyTagRecyclerviewAdapter
import com.softsquared.niceduck.android.sparky.view.scrap.ItemEvent
import com.softsquared.niceduck.android.sparky.view.scrap.ScrapTemplateRecyclerviewAdapter
import com.softsquared.niceduck.android.sparky.view.scrap.TagAddRecyclerviewAdapter
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel(), ItemEvent {
    private val mainRepository = MainRepository()

    // URL 검증
    private val _scrapValidationResponse: MutableSingleLiveData<BaseResponse> = MutableSingleLiveData()
    val scrapValidationResponse: SingleLiveData<BaseResponse>
        get() = _scrapValidationResponse

    private val _scrapValidationFailure = MutableSingleLiveData<Int>()
    val scrapValidationFailure: SingleLiveData<Int>
        get() = _scrapValidationFailure

    fun getScrapValidation(url: String) {
        viewModelScope.launch {
            try {
                val response =  mainRepository.getScrapValidation(url)

                if (response.isSuccessful) {
                    response.body()?.let {
                        _scrapValidationResponse.setValue(it)
                    }
                } else {
                    _scrapValidationFailure.setValue(response.code())
                }
            } catch (e: Exception) {
                e.message?.let { Log.d("test", it) }
            }
        }
    }


    // 토큰 갱신
    private val _reissueAccessTokenResponse: MutableSingleLiveData<TokenResponse> = MutableSingleLiveData()
    val reissueAccessTokenResponse: SingleLiveData<TokenResponse>
        get() = _reissueAccessTokenResponse

    private val _reissueAccessTokenFailure = MutableSingleLiveData<Int>()
    val reissueAccessTokenFailure: SingleLiveData<Int>
        get() = _reissueAccessTokenFailure

    // 마이 화면 검색 관련 변수
    var searchType = 1
    var searchTitle: String = ""
    var myScrapDataSet: List<Scrap>? = null

    // 마이 화면 네트워크 통신 결과
    private val _myScrapLoadResponse: MutableLiveData<ScrapRoadResponse> = MutableLiveData()
    val myScrapLoadResponse: LiveData<ScrapRoadResponse>
        get() = _myScrapLoadResponse

    private val _myScrapLoadFailure = MutableLiveData<Int>()
    val myScrapLoadFailure: LiveData<Int>
        get() = _myScrapLoadFailure

    private val _myScrapSearchResponse: MutableSingleLiveData<SearchScrapResponse> = MutableSingleLiveData()
    val myScrapSearchResponse: SingleLiveData<SearchScrapResponse>
        get() = _myScrapSearchResponse

    private val _myScrapSearchFailure = MutableSingleLiveData<Int>()
    val myScrapSearchFailure: SingleLiveData<Int>
        get() = _myScrapSearchFailure


    // 홈 화면 네트워크 통신 결과
    private val _homeScrapLoadResponse: MutableLiveData<ScrapRoadResponse> = MutableLiveData()
    val homeScrapLoadResponse: LiveData<ScrapRoadResponse>
        get() = _homeScrapLoadResponse

    private val _homeScrapLoadFailure = MutableSingleLiveData<Int>()
    val homeScrapLoadFailure: SingleLiveData<Int>
        get() = _homeScrapLoadFailure

    private val _homeScrapSearchResponse: MutableLiveData<SearchScrapResponse> = MutableLiveData()
    val homeScrapSearchResponse: LiveData<SearchScrapResponse>
        get() = _homeScrapSearchResponse

    private val _homeScrapSearchFailure = MutableSingleLiveData<Int>()
    val homeScrapSearchFailure: SingleLiveData<Int>
        get() = _homeScrapSearchFailure


    // 토큰 갱신
    suspend fun postReissueAccessToken(): Int {
        val editor = sSharedPreferences.edit()
        editor.putString(X_ACCESS_TOKEN, null)
        editor.apply()

        val scope = viewModelScope.async {
            try {
                val response = mainRepository.postReissueAccessToken()

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


    // 홈 화면 스크랩 조회
    fun getHomeScrapLoad() {
        viewModelScope.launch {
            try {
                val response = mainRepository.getScrap()

                if (response.isSuccessful) {
                    response.body()?.let {
                        _homeScrapLoadResponse.value = response.body()
                    }
                } else {
                    _homeScrapLoadFailure.setValue(response.code())
                }
            } catch (e: Exception) {
                e.message?.let { Log.d("test", it) }
            }
        }
    }

    // 마이 화면 스크랩 조회
    fun getMyScrapLoad() {
        viewModelScope.launch {
            try {
                val response = mainRepository.getScrap("1")

                if (response.isSuccessful) {
                    response.body()?.let {
                        _myScrapLoadResponse.value = it
                    }
                } else {
                    _myScrapLoadFailure.setValue(response.code())
                }
            } catch (e: Exception) {
                e.message?.let { Log.d("test", it) }
            }
        }
    }

    // 마이 화면 스크랩 검색
    fun postScrapSearch() {
        viewModelScope.launch {
            try {
                val response = mainRepository.postScrapSearch(
                    ScrapSearchRequest(
                        tags = tags,
                        title = searchTitle,
                        type = searchType
                    )
                )

                if (response.isSuccessful) {
                    response.body()?.let {
                        _myScrapSearchResponse.setValue(it)
                    }
                } else {
                    _myScrapSearchFailure.setValue(response.code())
                }
            } catch (e: Exception) {
                Log.d("test", e.message.toString())
            }
        }
    }

    // 홈 화면 검색
    var homeSearchType = 0
    var homeSearchTitle: String = ""
    var homeScrapDataSet: List<Scrap>? = null

    fun postHomeScrapSearch() {
        viewModelScope.launch {
            Log.d("테스트", "$homeSearchTitle, $homeSearchType")

            try {
                val response = mainRepository.postScrapSearch(
                    ScrapSearchRequest(
                        tags = null,
                        title = homeSearchTitle,
                        type = homeSearchType
                    )
                )

                if (response.isSuccessful) {
                    response.body()?.let {
                        _homeScrapSearchResponse.setValue(it)
                    }
                } else {
                    _homeScrapSearchFailure.setValue(response.code())
                }

            } catch (e: Exception) {
                e.message?.let { Log.d("test", it) }
            }

        }
    }


    // 필터 검색 기능 관련
    private val scrapTemplateRepository = ScrapTemplateRepository()

    // 저장할 스크랩 데이터
    val tags = ArrayList<Int>()

    val lastTags = MutableLiveData<ArrayList<TagsResponse>>()
    var updatedList = ArrayList<TagsResponse>()

    private val tagColorList = listOf(
        "#FFDDDA", "#FFE8D3", "#FFFDCC", "#D8F5D6", "#D5E7E0", "#DFF1F5",
        "#DFF1F5", "#E5DDF3", "#F1E0EB", "#FFE6F7", "#E5DBE0", "#DFDFDF"
    )
    val tagColor = MutableSingleLiveData<String>()

    // 템플릿 화면 어댑터
    val scrapTemplateRecyclerviewAdapter: MyTagRecyclerviewAdapter

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
    private val _tagLastLoadResponse = MutableSingleLiveData<TagLastLoadResponse>()
    val tagLastLoadResponse: SingleLiveData<TagLastLoadResponse>
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

    init {
        val initTags = ArrayList<TagsResponse>()

        scrapTemplateRecyclerviewAdapter = MyTagRecyclerviewAdapter(this) // 템플릿 화면 어댑터 생성
        scrapTemplateDataSet.value = initTags // 탬플릿 화면 어댑터 데이터
        tagAddRecyclerviewAdapter = TagAddRecyclerviewAdapter(this) // 바텀 시트 어댑터 생성
        getTagLastLoad()
    }

    private val _userResponse = MutableLiveData<UserResponse>()
    val userResponse: LiveData<UserResponse>
        get() = _userResponse

    private val _userFailure = MutableLiveData<BaseResponse>()
    val userFailure: LiveData<BaseResponse>
        get() = _userFailure

    fun getUser() {
        viewModelScope.launch {
            try {
                val response = mainRepository.getUser()

                if (response.isSuccessful) {
                    response.body()?.let { _userResponse.setValue(it) }
                } else {
                    response.errorBody()?.let {
                        val errorBody = NetworkUtil.getErrorResponse(it)
                        errorBody?.let { error -> _userFailure.setValue(error) }
                    }
                }
            } catch (e: Exception) {
                e.message?.let { Log.d("test", it) }
            }
        }
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


        scrapTemplateDataSet.value!!.add(
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

}
