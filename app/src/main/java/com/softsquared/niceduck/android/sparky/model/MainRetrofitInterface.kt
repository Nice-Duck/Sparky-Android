package com.softsquared.niceduck.android.sparky.model

import com.softsquared.niceduck.android.sparky.utill.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MainRetrofitInterface {
    // 스크랩 조회
    @GET("/api/v1/scraps")
    suspend fun getScrap(@Query("type") type: String? = null): Response<ScrapRoadResponse>
}
