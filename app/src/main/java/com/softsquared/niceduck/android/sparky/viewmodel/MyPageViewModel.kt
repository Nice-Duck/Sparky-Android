package com.softsquared.niceduck.android.sparky.viewmodel

import androidx.lifecycle.*
import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import com.softsquared.niceduck.android.sparky.model.*
import com.softsquared.niceduck.android.sparky.utill.BaseResponse
import com.softsquared.niceduck.android.sparky.utill.MutableSingleLiveData
import com.softsquared.niceduck.android.sparky.utill.NetworkUtil
import com.softsquared.niceduck.android.sparky.utill.SingleLiveData
import kotlinx.coroutines.*
import okhttp3.internal.wait

class MyPageViewModel : ViewModel() {
    private val repository = MyPageRepository()

    private val _withdrawalResponse = MutableSingleLiveData<BaseResponse>()
    val withdrawalResponse: SingleLiveData<BaseResponse>
        get() = _withdrawalResponse

    private val _withdrawalFailure = MutableSingleLiveData<BaseResponse>()
    val withdrawalFailure: SingleLiveData<BaseResponse>
        get() = _withdrawalFailure

    fun deleteWithdrawal() {
        viewModelScope.launch {
            val response = repository.deleteWithdrawal()

            if (response.isSuccessful) {
                response.body()?.let { _withdrawalResponse.setValue(it) }
            } else {
                    response.errorBody()?.let {
                        val errorBody = NetworkUtil.getErrorResponse(it)
                        errorBody?.let { error -> _withdrawalFailure.setValue(error) }
                    }
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
            val response = repository.postReissueAccessToken()

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
}
