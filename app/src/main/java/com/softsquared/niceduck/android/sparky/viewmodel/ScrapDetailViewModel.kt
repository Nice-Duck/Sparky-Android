package com.softsquared.niceduck.android.sparky.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import com.softsquared.niceduck.android.sparky.model.ScrapDetailRepository
import com.softsquared.niceduck.android.sparky.model.ScrapTemplateRepository
import com.softsquared.niceduck.android.sparky.model.TokenResponse
import com.softsquared.niceduck.android.sparky.utill.BaseResponse
import com.softsquared.niceduck.android.sparky.utill.MutableSingleLiveData
import com.softsquared.niceduck.android.sparky.utill.NetworkUtil
import com.softsquared.niceduck.android.sparky.utill.SingleLiveData
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ScrapDetailViewModel: ViewModel() {
    private val scrapDetailRepository = ScrapDetailRepository()

    private val _declarationResponse = MutableSingleLiveData<BaseResponse>()
    val declarationResponse: SingleLiveData<BaseResponse>
        get() = _declarationResponse

    private val _declarationFailure = MutableSingleLiveData<BaseResponse>()
    val declarationFailure: SingleLiveData<BaseResponse>
        get() = _declarationFailure

    fun getDeclaration(request: String) {
        viewModelScope.launch {
            try {
                val response = scrapDetailRepository.getDeclaration(request)

                if (response.isSuccessful) {
                    response.body()?.let { _declarationResponse.setValue(it) }
                } else {
                    response.errorBody()?.let {
                        val errorBody = NetworkUtil.getErrorResponse(it)
                        errorBody?.let { error -> _declarationFailure.setValue(error) }
                    }
                }
            } catch (e: Exception) {
                e.message?.let { Log.d("test", it) }
            }
        }
    }


    // 스크랩 저장
    private val _scrapDeleteResponse = MutableSingleLiveData<BaseResponse>()
    val scrapDeleteResponse: SingleLiveData<BaseResponse>
        get() = _scrapDeleteResponse
    private val _scrapDeleteFailure = MutableSingleLiveData<Int>()
    val scrapDeleteFailure: SingleLiveData<Int>
        get() = _scrapDeleteFailure


    fun deleteScrap(scrapId: String) {
        viewModelScope.launch {
            try {
                val response = scrapDetailRepository.deleteScrap(scrapId)

                if (response.isSuccessful) {
                    response.body()?.let { _scrapDeleteResponse.setValue(it) }
                } else {
                    _scrapDeleteFailure.setValue(response.code())
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

    // 토큰 갱신
    suspend fun postReissueAccessToken(): Int {
        val editor = ApplicationClass.sSharedPreferences.edit()
        editor.putString(ApplicationClass.X_ACCESS_TOKEN, null)
        editor.apply()

        val scope = viewModelScope.async {
            try {
                val response = scrapDetailRepository.postReissueAccessToken()

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