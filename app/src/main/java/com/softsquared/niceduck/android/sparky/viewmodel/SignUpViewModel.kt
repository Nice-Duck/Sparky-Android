package com.softsquared.niceduck.android.sparky.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {
    val progress = MutableLiveData<Int>()

    fun setProgress(progress: Int) {
        this.progress.value = progress
    }
}
