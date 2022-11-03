package com.softsquared.niceduck.android.sparky.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softsquared.niceduck.android.sparky.model.ScrapDetailRepository
import com.softsquared.niceduck.android.sparky.model.ScrapStoreRequest
import com.softsquared.niceduck.android.sparky.model.ScrapTemplateRepository
import com.softsquared.niceduck.android.sparky.utill.BaseResponse
import com.softsquared.niceduck.android.sparky.utill.MutableSingleLiveData
import com.softsquared.niceduck.android.sparky.utill.SingleLiveData
import kotlinx.coroutines.launch

class ScrapDetailViewModel: ViewModel() {
    private val scrapDetailRepository = ScrapDetailRepository()

    // 스크랩 저장
    private val _scrapDeleteResponse = MutableSingleLiveData<BaseResponse>()
    val scrapDeleteResponse: SingleLiveData<BaseResponse>
        get() = _scrapDeleteResponse
    private val _scrapDeleteFailure = MutableSingleLiveData<Int>()
    val scrapDeleteFailure: SingleLiveData<Int>
        get() = _scrapDeleteFailure


    fun deleteScrap(scrapId: String) {
        viewModelScope.launch {
            val response = scrapDetailRepository.deleteScrap(scrapId)

            if (response.isSuccessful) {
                response.body()?.let { _scrapDeleteResponse.setValue(it) }
            } else {
                _scrapDeleteFailure.setValue(response.code())
            }


        }
    }
}