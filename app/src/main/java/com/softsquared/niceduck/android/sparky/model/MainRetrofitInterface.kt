package com.softsquared.niceduck.android.sparky.model

import com.softsquared.niceduck.android.sparky.utill.BaseResponse
import retrofit2.Response
import retrofit2.http.*

interface MainRetrofitInterface {
    // 스크랩 조회
    @GET("/api/v1/scraps")
    suspend fun getScrap(@Query("type") type: String? = null): Response<ScrapRoadResponse>

    // 스크랩 검색
    @POST("/api/v1/scraps/search")
    suspend fun postSearchScrap(@Body request: ScrapSearchRequest): Response<SearchScrapResponse>

    // 토큰 갱신
    @POST("/api/v1/token")
    suspend fun postReissueAccessToken(@Header("Authorization") token: String?): Response<TokenResponse>

    // URL 검증
    @GET("/api/v1/scraps/validation")
    suspend fun getScrapValidation(@Query("url") url: String): Response<BaseResponse>
}
