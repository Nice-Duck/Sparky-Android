package com.softsquared.niceduck.android.sparky.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softsquared.niceduck.android.sparky.model.*
import com.softsquared.niceduck.android.sparky.utill.BaseResponse
import kotlinx.coroutines.*

class SignUpViewModel : ViewModel() {
    private val repository = AuthRepository()
    // 프로그래스바
    val progress = MutableLiveData<Int>()

    // 회원가입 데이터
    var email: String = ""
    var name: String = ""
    var pwd: String = ""

    // 이메일 중복 확인
    private val _duplicationEmailCheckResponse = MutableLiveData<BaseResponse>()
    val duplicationEmailCheckResponse: LiveData<BaseResponse>
        get() = _duplicationEmailCheckResponse

    private val _duplicationEmailCheckFailure = MutableLiveData<Int>()
    val duplicationEmailCheckFailure: LiveData<Int>
        get() = _duplicationEmailCheckFailure

    fun getDuplicationEmailCheck() {
        viewModelScope.launch {
            val response = repository.getDuplicationEmailCheck(email)

            if (response.isSuccessful) {
                _duplicationEmailCheckResponse.value = response.body()
            } else {
                _duplicationEmailCheckFailure.value = response.code()
            }
        }
    }

    // 닉네임 중복 확인
    private val _duplicationNameCheckResponse = MutableLiveData<BaseResponse>()
    val duplicationNameCheckResponse: LiveData<BaseResponse>
        get() = _duplicationNameCheckResponse

    private val _duplicationNameCheckFailure = MutableLiveData<Int>()
    val duplicationNameCheckFailure: LiveData<Int>
        get() = _duplicationNameCheckFailure

    fun getDuplicationNameCheck() {
        viewModelScope.launch {
            val response = repository.getDuplicationNameCheck(name)

            if (response.isSuccessful) {
                _duplicationNameCheckResponse.value = response.body()
            } else {
                _duplicationNameCheckFailure.value = response.code()
            }
        }
    }

    // 인증 전송
    private val _certificationSendResponse = MutableLiveData<BaseResponse>()
    val certificationSendResponse: LiveData<BaseResponse>
        get() = _certificationSendResponse

    private val _certificationSendFailure = MutableLiveData<Int>()
    val certificationSendFailure: LiveData<Int>
        get() = _certificationSendFailure

    fun postCertificationSend() {
        viewModelScope.launch {
            val response = repository.postCertificationSend(SignUpCertificationSendRequest(email))

            if (response.isSuccessful) {
                _certificationSendResponse.value = response.body()
            } else {
                _certificationSendFailure.value = response.code()
            }
        }
    }

    // 인증 확인
    private val _certificationCheckResponse = MutableLiveData<BaseResponse>()
    val certificationCheckResponse: LiveData<BaseResponse>
        get() = _certificationCheckResponse

    private val _certificationCheckFailure = MutableLiveData<Int>()
    val certificationCheckFailure: LiveData<Int>
        get() = _certificationCheckFailure

    fun postCertificationCheck(number: String) {
        viewModelScope.launch {
            val response = repository.postCertificationCheck(SignUpCertificationCheckRequest(email, number))

            if (response.isSuccessful) {
                _certificationCheckResponse.value = response.body()
            } else {
                _certificationCheckFailure.value = response.code()
            }
        }

    }

    // 회원 가입
    private val _signUpResponse = MutableLiveData<SignResponse>()
    val signUpResponse: LiveData<SignResponse>
        get() = _signUpResponse

    private val _signUpFailure = MutableLiveData<Int>()
    val signUpFailure: LiveData<Int>
        get() = _signUpFailure

    fun postSignUp() {
        viewModelScope.launch {
            val response = repository.postSignUp(SignUpRequest(
                email,
                name,
                pwd
            ))

            if (response.isSuccessful) {
                _signUpResponse.value = response.body()
            } else {
                _signUpFailure.value = response.code()
            }
        }
    }


}
