package com.softsquared.niceduck.android.sparky.model

import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import com.softsquared.niceduck.android.sparky.config.ApplicationClass.Companion.X_REFRESH_TOKEN
import com.softsquared.niceduck.android.sparky.config.ApplicationClass.Companion.sSharedPreferences
import retrofit2.create

class MainRepository {
    private val mainService = ApplicationClass.sRetrofit.create<MainRetrofitInterface>()

    // 스크랩 조회
    suspend fun getScrap(type: String? = null) =
        mainService.getScrap(type)

    // 스크랩 검색
    suspend fun postScrapSearch(request: ScrapSearchRequest) =
        mainService.postSearchScrap(request)

    // 토큰 갱신
    suspend fun postReissueAccessToken() =
         mainService.postReissueAccessToken(
            sSharedPreferences.getString(
                X_REFRESH_TOKEN, null))
}
