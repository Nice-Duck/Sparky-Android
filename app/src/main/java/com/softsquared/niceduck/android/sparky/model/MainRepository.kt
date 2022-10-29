package com.softsquared.niceduck.android.sparky.model

import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import retrofit2.create

class MainRepository {
    private val mainService = ApplicationClass.sRetrofit.create<MainRetrofitInterface>()

    // 스크랩 조회
    suspend fun getScrap(type: String? = null) =
        mainService.getScrap(type)
}
