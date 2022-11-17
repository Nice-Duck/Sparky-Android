package com.softsquared.niceduck.android.sparky.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.*
import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import com.softsquared.niceduck.android.sparky.model.*
import com.softsquared.niceduck.android.sparky.utill.*
import com.softsquared.niceduck.android.sparky.view.scrap.ItemEvent
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.internal.wait
import okio.BufferedSink

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

    private val _userResponse = MutableSingleLiveData<UserResponse>()
    val userResponse: SingleLiveData<UserResponse>
        get() = _userResponse

    private val _userFailure = MutableSingleLiveData<BaseResponse>()
    val userFailure: SingleLiveData<BaseResponse>
        get() = _userFailure

    fun getUser() {
        viewModelScope.launch {
            val response = repository.getUser()

            if (response.isSuccessful) {
                response.body()?.let { _userResponse.setValue(it) }
            } else {
                response.errorBody()?.let {
                    val errorBody = NetworkUtil.getErrorResponse(it)
                    errorBody?.let { error -> _userFailure.setValue(error) }
                }
            }
        }
    }


    private val _inquiryResponse = MutableSingleLiveData<BaseResponse>()
    val inquiryResponse: SingleLiveData<BaseResponse>
        get() = _inquiryResponse

    private val _inquiryFailure = MutableSingleLiveData<BaseResponse>()
    val inquiryFailure: SingleLiveData<BaseResponse>
        get() = _inquiryFailure

    fun postInquiry(request: InquiryRequest) {
        viewModelScope.launch {
            val response = repository.postInquiry(request)

            if (response.isSuccessful) {
                response.body()?.let { _inquiryResponse.setValue(it) }
            } else {
                response.errorBody()?.let {
                    val errorBody = NetworkUtil.getErrorResponse(it)
                    errorBody?.let { error -> _inquiryFailure.setValue(error) }
                }
            }
        }
    }

    private val _declarationResponse = MutableSingleLiveData<BaseResponse>()
    val declarationResponse: SingleLiveData<BaseResponse>
        get() = _declarationResponse

    private val _declarationFailure = MutableSingleLiveData<BaseResponse>()
    val declarationFailure: SingleLiveData<BaseResponse>
        get() = _declarationFailure

    fun getDeclaration(request: String) {
        viewModelScope.launch {
            val response = repository.getDeclaration(request)

            if (response.isSuccessful) {
                response.body()?.let { _declarationResponse.setValue(it) }
            } else {
                response.errorBody()?.let {
                    val errorBody = NetworkUtil.getErrorResponse(it)
                    errorBody?.let { error -> _declarationFailure.setValue(error) }
                }
            }
        }
    }


    private val _tagLastLoadResponse = MutableSingleLiveData<TagLastLoadResponse>()
    val tagLastLoadResponse: SingleLiveData<TagLastLoadResponse>
        get() = _tagLastLoadResponse
    private val _tagLastLoadFailure = MutableSingleLiveData<BaseResponse>()
    val tagLastLoadFailure: SingleLiveData<BaseResponse>
        get() = _tagLastLoadFailure

    fun getTagLastLoad() {
        viewModelScope.launch {
            val response = repository.getTagLastLoad()

            if (response.isSuccessful) {
                response.body()?.let { _tagLastLoadResponse.setValue(it) }
            } else {
                response.errorBody()?.let {
                    val errorBody = NetworkUtil.getErrorResponse(it)
                    errorBody?.let { error -> _tagLastLoadFailure.setValue(error) }
                }
            }
        }
    }



    private val _tagPatchResponse = MutableSingleLiveData<TagResponse>()
    val tagPatchResponse: SingleLiveData<TagResponse>
        get() = _tagPatchResponse
    private val _tagPatchFailure = MutableSingleLiveData<BaseResponse>()
    val tagPatchFailure: SingleLiveData<BaseResponse>
        get() = _tagPatchFailure

    var patchTag: TagsResponse? = null
    var patchPosition: Int? = null

    fun patchTag() {
        val request = patchTag?.let {
            TagRequest2(it.tagId, it.name)
        }
        viewModelScope.launch {
            request?.let {
                val response = repository.patchTag(request)

                if (response.isSuccessful) {
                    response.body()?.let { _tagPatchResponse.setValue(it) }
                } else {
                    response.errorBody()?.let {
                        val errorBody = NetworkUtil.getErrorResponse(it)
                        errorBody?.let { error -> _tagPatchFailure.setValue(error) }
                    }
                }
            }
        }
    }

    private val _tagDeleteResponse = MutableSingleLiveData<BaseResponse>()
    val tagDeleteResponse: SingleLiveData<BaseResponse>
        get() = _tagDeleteResponse
    private val _tagDeleteFailure = MutableSingleLiveData<BaseResponse>()
    val tagDeleteFailure: SingleLiveData<BaseResponse>
        get() = _tagDeleteFailure

    var deletePosition: Int? = null
    var deleteTagId: Int? = null

    fun deleteTag() {
       deleteTagId?.let {
            viewModelScope.launch {
                val response = repository.deleteTag(it)

                if (response.isSuccessful) {
                    response.body()?.let { _tagDeleteResponse.setValue(it) }
                } else {
                    response.errorBody()?.let {
                        val errorBody = NetworkUtil.getErrorResponse(it)
                        errorBody?.let { error -> _tagDeleteFailure.setValue(error) }
                    }
                }
            }
        }

    }

    inner class BitmapRequestBody(private val bitmap: Bitmap) : RequestBody() {
        override fun contentType(): MediaType = "image/jpeg".toMediaType()
        override fun writeTo(sink: BufferedSink) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 99, sink.outputStream())
        }
    }
    var image: Bitmap? = null
    var name: String = ""

    // 프로필 수정
    private val _userUpdateResponse = MutableSingleLiveData<UserResponse>()
    val userUpdateResponse: SingleLiveData<UserResponse>
        get() = _userUpdateResponse
    private val _userUpdateFailure = MutableSingleLiveData<BaseResponse>()
    val userUpdateFailure: SingleLiveData<BaseResponse>
        get() = _userUpdateFailure

    fun patchUser() {
        viewModelScope.launch {
            val formName= FormDataUtil.getBody("name", name)

            val bitmapRequestBody = image?.let { BitmapRequestBody(it) }
            val bitmapMultipartBody: MultipartBody.Part? =
                if (bitmapRequestBody == null) null
                else MultipartBody.Part.createFormData("image", "sparky", bitmapRequestBody)


            val response = repository.patchUser(formName,
                bitmapMultipartBody
            )

            if (response.isSuccessful) {
                response.body()?.let { _userUpdateResponse.setValue(it) }
            } else {
                response.errorBody()?.let {
                    val errorBody = NetworkUtil.getErrorResponse(it)
                    errorBody?.let { error -> _userUpdateFailure.setValue(error) }
                }
            }
        }
    }


}
