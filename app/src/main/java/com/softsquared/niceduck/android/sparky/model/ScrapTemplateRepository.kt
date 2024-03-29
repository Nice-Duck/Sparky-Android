package com.softsquared.niceduck.android.sparky.model

import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import com.softsquared.niceduck.android.sparky.utill.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.create
import retrofit2.http.Part

class ScrapTemplateRepository {
    private val scrapTemplateModel = ScrapTemplateModel()
    private val scrapTemplateService =
        ApplicationClass.sRetrofit.create<ScrapTemplateRetrofitInterface>()

    fun handleSendText(title: String?): Pair<String, Map<String, String>> {
        return scrapTemplateModel.handleSendText(title)
    }

    // 태그 저장
    suspend fun postTagSave(request: TagRequest) =
        scrapTemplateService.postTagSave(request)

    // 태그 조회
    suspend fun getTagLastLoad() =
        scrapTemplateService.getTagLastLoad()

    // 스크랩 저장
    suspend fun postStoreScrap(request: ScrapStoreRequest) =
        scrapTemplateService.postStoreScrap(request)

    // 스크랩 수정
    suspend fun patchScrap(
        scrapId: String,
        title: MultipartBody.Part? = null,
        subTitle: MultipartBody.Part? = null,
        memo: MultipartBody.Part? = null,
        scpUrl: MultipartBody.Part? = null,
        tags: MultipartBody.Part? = null,
        image: MultipartBody.Part? = null
    ) = scrapTemplateService.patchScrap(scrapId, title, subTitle, memo, scpUrl, tags, image)

    // 토큰 갱신
    suspend fun postReissueAccessToken() =
        scrapTemplateService.postReissueAccessToken(
            ApplicationClass.sSharedPreferences.getString(
                ApplicationClass.X_REFRESH_TOKEN, null
            )
        )

}
