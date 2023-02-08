package com.softsquared.niceduck.android.sparky.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softsquared.niceduck.android.sparky.model.*
import com.softsquared.niceduck.android.sparky.utill.BaseResponse
import com.softsquared.niceduck.android.sparky.utill.MutableSingleLiveData
import com.softsquared.niceduck.android.sparky.utill.NetworkUtil
import com.softsquared.niceduck.android.sparky.utill.SingleLiveData
import kotlinx.coroutines.*

class SignUpViewModel : ViewModel() {
    private val repository = AuthRepository()
    // 이메일 인증 여부
    var isChecked = false

    // 프로그래스바
    val progress = MutableLiveData<Int>()

    // 회원가입 데이터
    var email: String = ""
    var name: String = ""
    var pwd: String = ""

    // 이메일 중복 확인
    private val _duplicationEmailCheckResponse = MutableSingleLiveData<BaseResponse>()
    val duplicationEmailCheckResponse: SingleLiveData<BaseResponse>
        get() = _duplicationEmailCheckResponse

    private val _duplicationEmailCheckFailure = MutableSingleLiveData<BaseResponse>()
    val duplicationEmailCheckFailure: SingleLiveData<BaseResponse>
        get() = _duplicationEmailCheckFailure

    fun getDuplicationEmailCheck() {
        viewModelScope.launch {
            try {
                val response = repository.getDuplicationEmailCheck(email)

                if (response.isSuccessful) {
                    response.body()?.let { _duplicationEmailCheckResponse.setValue(it) }
                } else {
                    response.errorBody()?.let {
                        val errorBody = NetworkUtil.getErrorResponse(it)
                        errorBody?.let { error -> _duplicationEmailCheckFailure.setValue(error) }
                    }
                }
            } catch (e: Exception) {
                e.message?.let { Log.d("test", it) }
            }
        }
    }

    // 닉네임 중복 확인
    private val _duplicationNameCheckResponse = MutableSingleLiveData<BaseResponse>()
    val duplicationNameCheckResponse: SingleLiveData<BaseResponse>
        get() = _duplicationNameCheckResponse

    private val _duplicationNameCheckFailure = MutableSingleLiveData<BaseResponse>()
    val duplicationNameCheckFailure: SingleLiveData<BaseResponse>
        get() = _duplicationNameCheckFailure

    fun getDuplicationNameCheck() {
        viewModelScope.launch {
            try {
                val response = repository.getDuplicationNameCheck(name)

                if (response.isSuccessful) {
                    response.body()?.let { _duplicationNameCheckResponse.setValue(it) }
                } else {
                    response.errorBody()?.let {
                        val errorBody = NetworkUtil.getErrorResponse(it)
                        errorBody?.let { error -> _duplicationNameCheckFailure.setValue(error) }
                    }
                }
            } catch (e: Exception) {
                e.message?.let { Log.d("test", it) }
            }
        }
    }

    // 인증 전송
    private val _certificationSendResponse = MutableSingleLiveData<BaseResponse>()
    val certificationSendResponse: SingleLiveData<BaseResponse>
        get() = _certificationSendResponse

    private val _certificationSendFailure = MutableSingleLiveData<BaseResponse>()
    val certificationSendFailure: SingleLiveData<BaseResponse>
        get() = _certificationSendFailure

    fun postCertificationSend() {
        viewModelScope.launch {
            try {
                val response = repository.postCertificationSend(SignUpCertificationSendRequest(email))

                if (response.isSuccessful) {
                    response.body()?.let { _certificationSendResponse.setValue(it) }
                } else {
                    response.errorBody()?.let {
                        val errorBody = NetworkUtil.getErrorResponse(it)
                        errorBody?.let { error -> _certificationSendFailure.setValue(error) }
                    }
                }
            } catch (e: Exception) {
                e.message?.let { Log.d("test", it) }
            }
        }
    }

    // 인증 확인
    private val _certificationCheckResponse = MutableSingleLiveData<BaseResponse>()
    val certificationCheckResponse: SingleLiveData<BaseResponse>
        get() = _certificationCheckResponse

    private val _certificationCheckFailure = MutableSingleLiveData<BaseResponse>()
    val certificationCheckFailure: SingleLiveData<BaseResponse>
        get() = _certificationCheckFailure

    fun postCertificationCheck(number: String) {
        viewModelScope.launch {
            try {
                val response = repository.postCertificationCheck(SignUpCertificationCheckRequest(email, number))

                if (response.isSuccessful) {
                    response.body()?.let { _certificationCheckResponse.setValue(it) }
                } else {
                    response.errorBody()?.let {
                        val errorBody = NetworkUtil.getErrorResponse(it)
                        errorBody?.let { error -> _certificationCheckFailure.setValue(error) }
                    }
                }
            } catch (e: Exception) {
                e.message?.let { Log.d("test", it) }
            }
        }
    }

    // 회원 가입
    private val _signUpResponse = MutableSingleLiveData<SignResponse>()
    val signUpResponse: SingleLiveData<SignResponse>
        get() = _signUpResponse

    private val _signUpFailure = MutableSingleLiveData<BaseResponse>()
    val signUpFailure: SingleLiveData<BaseResponse>
        get() = _signUpFailure

    fun postSignUp() {
        viewModelScope.launch {
            try {
                val response = repository.postSignUp(
                    SignUpRequest(
                        email,
                        name,
                        pwd
                    )
                )

                if (response.isSuccessful) {
                    response.body()?.let { _signUpResponse.setValue(it) }
                } else {
                    response.errorBody()?.let {
                        val errorBody = NetworkUtil.getErrorResponse(it)
                        errorBody?.let { error -> _signUpFailure.setValue(error) }
                    }
                }
            } catch (e: Exception) {
                e.message?.let { Log.d("test", it) }
            }
        }
    }
}
