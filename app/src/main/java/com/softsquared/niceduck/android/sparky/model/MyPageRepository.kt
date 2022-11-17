package com.softsquared.niceduck.android.sparky.model

import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import com.softsquared.niceduck.android.sparky.config.ApplicationClass.Companion.sRetrofit
import com.softsquared.niceduck.android.sparky.utill.BaseResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.create
import retrofit2.http.*

class MyPageRepository {
    private val myPageService = sRetrofit.create<MyPageRetrofitInterface>()

    // 탈퇴
    suspend fun deleteWithdrawal() =
        myPageService.deleteWithdrawal()

    // 토큰 갱신
    suspend fun postReissueAccessToken() =
        myPageService.postReissueAccessToken(
            ApplicationClass.sSharedPreferences.getString(
                ApplicationClass.X_REFRESH_TOKEN, null))

    // 프로필 수정
    suspend fun patchUser(name: MultipartBody.Part, image: MultipartBody.Part? = null) =
        myPageService.patchUser(name, image)

    // 마이페이지 로드
    suspend fun getUser() =
        myPageService.getUser()

    // 문의 사항
    suspend fun postInquiry(request: InquiryRequest) =
        myPageService.postInquiry(request)

    // 스크랩 신고
    suspend fun getDeclaration(scrapId: String) =
        myPageService.getDeclaration(scrapId)

    // 모든 태그 조회
    suspend fun getTagLastLoad() =
        myPageService.getTagLastLoad()

    // 태그 수정하기
    suspend fun patchTag(request: TagRequest2) =
        myPageService.patchTag(request)

    // 태그 삭제하기
    suspend fun deleteTag(id: Int) =
        myPageService.deleteTag(id)



}
