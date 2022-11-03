package com.softsquared.niceduck.android.sparky.model

import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import retrofit2.create

class ScrapTemplateRepository {
    private val scrapTemplateModel = ScrapTemplateModel()
    private val scrapTemplateService = ApplicationClass.sRetrofit.create<ScrapTemplateRetrofitInterface>()

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
    suspend fun patchScrap(scrapId: String, request: ScrapStoreRequest) =
        scrapTemplateService.patchScrap(scrapId, request)

}
