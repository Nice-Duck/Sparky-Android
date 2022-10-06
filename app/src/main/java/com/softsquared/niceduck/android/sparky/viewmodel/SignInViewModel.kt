package com.softsquared.niceduck.android.sparky.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softsquared.niceduck.android.sparky.model.*
import kotlinx.coroutines.*

class SignInViewModel : ViewModel() {
    private val repository = AuthRepository()
    // 로그인
    var email: String = ""
    var pwd: String = ""

    private val _signInResponse = MutableLiveData<SignResponse>()
    val signInResponse: LiveData<SignResponse>
        get() = _signInResponse

    private val _signInFailure = MutableLiveData<Int>()
    val signInFailure: LiveData<Int>
        get() = _signInFailure

    fun postSignIn() {
        viewModelScope.launch {
            val response = repository.postSignIn(SignInRequest(
                email,
                pwd
            ))

            if (response.isSuccessful) {
                _signInResponse.value = response.body()
            } else {
                _signInFailure.value = response.code()
            }
        }
    }
}