package com.softsquared.niceduck.android.sparky.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softsquared.niceduck.android.sparky.model.*
import com.softsquared.niceduck.android.sparky.utill.BaseResponse
import com.softsquared.niceduck.android.sparky.utill.MutableSingleLiveData
import com.softsquared.niceduck.android.sparky.utill.SingleLiveData
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
    private val _duplicationEmailCheckResponse = MutableSingleLiveData<BaseResponse>()
    val duplicationEmailCheckResponse: SingleLiveData<BaseResponse>
        get() = _duplicationEmailCheckResponse

    private val _duplicationEmailCheckFailure = MutableSingleLiveData<Int>()
    val duplicationEmailCheckFailure: SingleLiveData<Int>
        get() = _duplicationEmailCheckFailure

    fun getDuplicationEmailCheck() {
        viewModelScope.launch {
            val response = repository.getDuplicationEmailCheck(email)

            if (response.isSuccessful) {
                response.body()?.let { _duplicationEmailCheckResponse.setValue(it) }
            } else {
                _duplicationEmailCheckFailure.setValue(response.code())
            }
        }
    }

    // 닉네임 중복 확인
    private val _duplicationNameCheckResponse = MutableSingleLiveData<BaseResponse>()
    val duplicationNameCheckResponse: SingleLiveData<BaseResponse>
        get() = _duplicationNameCheckResponse

    private val _duplicationNameCheckFailure = MutableSingleLiveData<Int>()
    val duplicationNameCheckFailure: SingleLiveData<Int>
        get() = _duplicationNameCheckFailure

    fun getDuplicationNameCheck() {
        viewModelScope.launch {
            val response = repository.getDuplicationNameCheck(name)

            if (response.isSuccessful) {
                response.body()?.let { _duplicationNameCheckResponse.setValue(it) }
            } else {
                _duplicationNameCheckFailure.setValue(response.code())
            }
        }
    }

    // 인증 전송
    private val _certificationSendResponse = MutableSingleLiveData<BaseResponse>()
    val certificationSendResponse: SingleLiveData<BaseResponse>
        get() = _certificationSendResponse

    private val _certificationSendFailure = MutableSingleLiveData<Int>()
    val certificationSendFailure: SingleLiveData<Int>
        get() = _certificationSendFailure

    fun postCertificationSend() {
        viewModelScope.launch {
            val response = repository.postCertificationSend(SignUpCertificationSendRequest(email))

            if (response.isSuccessful) {
                response.body()?.let { _certificationSendResponse.setValue(it) }
            } else {
                _certificationSendFailure.setValue(response.code())
            }
        }
    }

    // 인증 확인
    private val _certificationCheckResponse = MutableSingleLiveData<BaseResponse>()
    val certificationCheckResponse: SingleLiveData<BaseResponse>
        get() = _certificationCheckResponse

    private val _certificationCheckFailure = MutableSingleLiveData<Int>()
    val certificationCheckFailure: SingleLiveData<Int>
        get() = _certificationCheckFailure

    fun postCertificationCheck(number: String) {
        viewModelScope.launch {
            val response = repository.postCertificationCheck(SignUpCertificationCheckRequest(email, number))

            if (response.isSuccessful) {
                response.body()?.let { _certificationCheckResponse.setValue(it) }
            } else {
                _certificationCheckFailure.setValue(response.code())
            }
        }

    }

    // 회원 가입
    private val _signUpResponse = MutableSingleLiveData<SignResponse>()
    val signUpResponse: SingleLiveData<SignResponse>
        get() = _signUpResponse

    private val _signUpFailure = MutableSingleLiveData<Int>()
    val signUpFailure: SingleLiveData<Int>
        get() = _signUpFailure

    fun postSignUp() {
        viewModelScope.launch {
            val response = repository.postSignUp(SignUpRequest(
                email,
                name,
                pwd
            ))

            if (response.isSuccessful) {
                response.body()?.let { _signUpResponse.setValue(it) }
            } else {
                _signUpFailure.setValue(response.code())
            }
        }
    }


}
