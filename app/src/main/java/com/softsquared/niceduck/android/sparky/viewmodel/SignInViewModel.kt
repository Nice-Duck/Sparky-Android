package com.softsquared.niceduck.android.sparky.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softsquared.niceduck.android.sparky.model.*
import com.softsquared.niceduck.android.sparky.utill.MutableSingleLiveData
import com.softsquared.niceduck.android.sparky.utill.SingleLiveData
import kotlinx.coroutines.*

class SignInViewModel : ViewModel() {
    private val repository = AuthRepository()
    // 로그인
    var email: String = ""
    var pwd: String = ""

    private val _signInResponse = MutableSingleLiveData<SignResponse>()
    val signInResponse: SingleLiveData<SignResponse>
        get() = _signInResponse

    private val _signInFailure = MutableSingleLiveData<Int>()
    val signInFailure: SingleLiveData<Int>
        get() = _signInFailure

    fun postSignIn() {
        viewModelScope.launch {
            val response = repository.postSignIn(SignInRequest(
                email,
                pwd
            ))

            if (response.isSuccessful) {
                response.body()?.let { _signInResponse.setValue(it) }
            } else {
                _signInFailure.setValue(response.code())
            }
        }
    }
}