package com.softsquared.niceduck.android.sparky.model

import com.softsquared.niceduck.android.sparky.utill.BaseResponse
import okhttp3.MultipartBody
import org.jsoup.Connection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*

interface MyPageRetrofitInterface {
    // 탈퇴
    @DELETE("/api/v1/accounts")
    suspend fun deleteWithdrawal(): Response<BaseResponse>

    // 토큰 갱신
    @POST("/api/v1/token")
    suspend fun postReissueAccessToken(@Header("Authorization") token: String?): Response<TokenResponse>

    // 스크랩 수정
    @Multipart
    @PATCH("/api/v1/users")
    suspend fun patchUser(@Part name: MultipartBody.Part, @Part image: MultipartBody.Part?): Response<UserResponse>

    // 마이페이지 로드
    @GET("/api/v1/users")
    suspend fun getUser(): Response<UserResponse>

    // 문의 사항
    @POST("/api/v1/users/inquiry")
    suspend fun postInquiry(@Body request: InquiryRequest): Response<BaseResponse>


    // 모든 태그 조회
    @GET("/api/v1/tags")
    suspend fun getTagLastLoad(): Response<TagLastLoadResponse>

    // 태그 수정하기
    @PATCH("api/v1/tags")
    suspend fun patchTag(@Body request: TagRequest2): Response<TagResponse>

    // 태그 삭제하기
    @DELETE("api/v1/tags/{id}")
    suspend fun deleteTag(@Path("id") id: Int): Response<BaseResponse>
}
