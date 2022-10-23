package com.softsquared.niceduck.android.sparky.model

import com.softsquared.niceduck.android.sparky.utill.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

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
}
