package com.softsquared.niceduck.android.sparky.model

import com.softsquared.niceduck.android.sparky.utill.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthRetrofitInterface {

    // 로그인
    @POST("/api/v1/accounts")
    suspend fun postSignIn(@Body signInRequest: SignInRequest): Response<SignResponse>

    // 회원가입
    @POST("/api/v1/accounts/register")
    suspend fun postSignUp(@Body signUnRequest: SignUpRequest): Response<SignResponse>

    // 이메일 중복 확인
    @GET("/api/v1/accounts/register")
    suspend fun getDuplicationEmailCheck(@Query("email") email: String): Response<BaseResponse>

    // 닉네임 중복 확인
    @GET("/api/v1/users")
    suspend fun getDuplicationNameCheck(@Query("name") name: String): Response<BaseResponse>

    // 인증 전송
    @POST("/api/v1/accounts/mails/send")
    suspend fun postCertificationSend(@Body signUpCertificationSendRequest: SignUpCertificationSendRequest): Response<BaseResponse>

    // 인증 확인
    @POST("/api/v1/accounts/mails/confirm")
    suspend fun postCertificationCheck(@Body signUpCertificationCheckRequest: SignUpCertificationCheckRequest): Response<BaseResponse>
}
