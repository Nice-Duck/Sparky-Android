package com.softsquared.niceduck.android.sparky.model

import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import retrofit2.create

class ScrapDetailRepository {
    private val scrapDetailService = ApplicationClass.sRetrofit.create<ScrapDetailRetrofitInterface>()

    // 스크랩 삭제
    suspend fun deleteScrap(scrapId: String) =
       scrapDetailService.deleteScrap(scrapId)

}
