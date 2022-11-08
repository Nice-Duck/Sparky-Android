package com.softsquared.niceduck.android.sparky.viewmodel

import androidx.lifecycle.*
import com.softsquared.niceduck.android.sparky.model.*
import com.softsquared.niceduck.android.sparky.utill.BaseResponse
import com.softsquared.niceduck.android.sparky.utill.MutableSingleLiveData
import com.softsquared.niceduck.android.sparky.utill.NetworkUtil
import com.softsquared.niceduck.android.sparky.utill.SingleLiveData
import kotlinx.coroutines.*
import okhttp3.internal.wait

class SignInViewModel : ViewModel() {
    private val repository = AuthRepository()
    // 로그인
    var email: String = ""
    var pwd: String = ""

    private val _signInResponse = MutableSingleLiveData<SignResponse>()
    val signInResponse: SingleLiveData<SignResponse>
        get() = _signInResponse

    private val _signInFailure = MutableSingleLiveData<BaseResponse>()
    val signInFailure: SingleLiveData<BaseResponse>
        get() = _signInFailure

    fun postSignIn() {
        viewModelScope.launch {
            val response = repository.postSignIn(
                SignInRequest(
                    email,
                    pwd
                )
            )

            if (response.isSuccessful) {
                response.body()?.let { _signInResponse.setValue(it) }
            } else {
                    response.errorBody()?.let {
                        val errorBody = NetworkUtil.getErrorResponse(it)
                        errorBody?.let { error -> _signInFailure.setValue(error) }
                    }


            }
        }
    }
}
