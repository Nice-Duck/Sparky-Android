package com.softsquared.niceduck.android.sparky.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import com.softsquared.niceduck.android.sparky.config.ApplicationClass.Companion.X_ACCESS_TOKEN
import com.softsquared.niceduck.android.sparky.config.ApplicationClass.Companion.sSharedPreferences
import com.softsquared.niceduck.android.sparky.model.*
import com.softsquared.niceduck.android.sparky.utill.MutableSingleLiveData
import com.softsquared.niceduck.android.sparky.utill.SingleLiveData
import com.softsquared.niceduck.android.sparky.view.main.fragment.MyScrapRecyclerviewAdapter
import com.softsquared.niceduck.android.sparky.view.main.fragment.MyScrapRecyclerviewAdapter2
import com.softsquared.niceduck.android.sparky.view.main.fragment.MyScrapRecyclerviewAdapter3
import com.softsquared.niceduck.android.sparky.view.main.fragment.OthersScrapRecyclerviewAdapter
import com.softsquared.niceduck.android.sparky.view.scrap.ItemEvent
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {
    private val mainRepository = MainRepository()

    // 스크랩 추가 바텀 시트
    val scrapAddBottomSheetShow: MutableSingleLiveData<Boolean> = MutableSingleLiveData()

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
    var searchTags: ArrayList<Int> = ArrayList()
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

    private val _homeScrapLoadFailure = MutableLiveData<Int>()
    val homeScrapLoadFailure: LiveData<Int>
        get() = _homeScrapLoadFailure

    private val _homeScrapSearchResponse: MutableSingleLiveData<ScrapRoadResponse> = MutableSingleLiveData()
    val homeScrapSearchResponse: SingleLiveData<ScrapRoadResponse>
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

        }
        return scope.await()
    }


    // 홈 화면 스크랩 조회
    fun getHomeScrapLoad() {
        viewModelScope.launch {
            val response = mainRepository.getScrap()

            if (response.isSuccessful) {
                response.body()?.let {
                    _homeScrapLoadResponse.value = response.body()
                }
            } else {
                _homeScrapLoadFailure.setValue(response.code())
            }
        }
    }

    // 마이 화면 스크랩 조회
    fun getMyScrapLoad() {
        viewModelScope.launch {
            val response = mainRepository.getScrap("1")

            if (response.isSuccessful) {
                response.body()?.let {
                    _myScrapLoadResponse.value = it
                }
            } else {
                _myScrapLoadFailure.setValue(response.code())
            }
        }
    }

    // 마이 화면 스크랩 검색
    fun postScrapSearch() {
        viewModelScope.launch {
            Log.d("테스트", "$searchTags, $searchTitle, $searchType")
            val response = mainRepository.postScrapSearch(
                ScrapSearchRequest(
                tags = searchTags,
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
        }
    }


}
