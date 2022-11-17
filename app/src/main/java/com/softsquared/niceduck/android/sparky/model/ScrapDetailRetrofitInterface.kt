package com.softsquared.niceduck.android.sparky.model

import com.softsquared.niceduck.android.sparky.utill.BaseResponse
import retrofit2.Response
import retrofit2.http.*

interface ScrapDetailRetrofitInterface {
    // 스크랩 신고
    @GET("/api/v1/scraps/declaration")
    suspend fun getDeclaration(@Query("scrapId") scrapId: String): Response<BaseResponse>

    // 스크랩 삭제
    @DELETE("/api/v1/scraps")
    suspend fun deleteScrap(@Query("scrapId") scrapId: String): Response<BaseResponse>

    // 토큰 갱신
    @POST("/api/v1/token")
    suspend fun postReissueAccessToken(@Header("Authorization") token: String?): Response<TokenResponse>
}
