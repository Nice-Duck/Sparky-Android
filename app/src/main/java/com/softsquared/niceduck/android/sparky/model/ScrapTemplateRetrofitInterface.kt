package com.softsquared.niceduck.android.sparky.model

import com.softsquared.niceduck.android.sparky.utill.BaseResponse
import retrofit2.Response
import retrofit2.http.*

interface ScrapTemplateRetrofitInterface {

    // 태그 저장
    @POST("/api/v1/tags")
    suspend fun postTagSave(@Body tagRequest: TagRequest): Response<TagResponse>

    // 태그 조회
    @GET("/api/v1/tags")
    suspend fun getTagLastLoad(): Response<TagLastLoadResponse>

    // 스크랩 저장
    @POST("/api/v1/scraps")
    suspend fun postStoreScrap(@Body request: ScrapStoreRequest): Response<BaseResponse>

    // 스크랩 수정
    @PATCH("/api/v1/scraps")
    suspend fun patchScrap(@Query("scrapId") scrapId: String, @Body request: ScrapStoreRequest): Response<BaseResponse>

    // 토큰 갱신
    @POST("/api/v1/token")
    suspend fun postReissueAccessToken(@Header("Authorization") token: String?): Response<TokenResponse>
}
