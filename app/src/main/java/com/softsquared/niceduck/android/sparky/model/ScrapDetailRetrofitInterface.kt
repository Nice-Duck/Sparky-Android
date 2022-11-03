package com.softsquared.niceduck.android.sparky.model

import com.softsquared.niceduck.android.sparky.utill.BaseResponse
import retrofit2.Response
import retrofit2.http.*

interface ScrapDetailRetrofitInterface {


    // 스크랩 삭제
    @DELETE("/api/v1/scraps")
    suspend fun deleteScrap(@Query("scrapId") scrapId: String): Response<BaseResponse>
}
