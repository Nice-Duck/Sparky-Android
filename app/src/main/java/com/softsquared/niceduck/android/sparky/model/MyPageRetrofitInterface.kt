package com.softsquared.niceduck.android.sparky.model

import com.softsquared.niceduck.android.sparky.utill.BaseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*

interface MyPageRetrofitInterface {
    // 탈퇴
    @DELETE("/api/v1/accounts")
    suspend fun deleteWithdrawal(): Response<BaseResponse>

    // 토큰 갱신
    @POST("/api/v1/token")
    suspend fun postReissueAccessToken(@Header("Authorization") token: String?): Response<TokenResponse>
}
