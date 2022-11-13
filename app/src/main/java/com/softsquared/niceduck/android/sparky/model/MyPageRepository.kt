package com.softsquared.niceduck.android.sparky.model

import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import com.softsquared.niceduck.android.sparky.config.ApplicationClass.Companion.sRetrofit
import com.softsquared.niceduck.android.sparky.utill.BaseResponse
import retrofit2.Response
import retrofit2.create
import retrofit2.http.DELETE

class MyPageRepository {
    private val myPageService = sRetrofit.create<MyPageRetrofitInterface>()

    // 탈퇴
    suspend fun deleteWithdrawal() =
        myPageService.deleteWithdrawal()

    // 토큰 갱신
    suspend fun postReissueAccessToken() =
        myPageService.postReissueAccessToken(
            ApplicationClass.sSharedPreferences.getString(
                ApplicationClass.X_REFRESH_TOKEN, null))

}
