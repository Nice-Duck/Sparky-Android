package com.softsquared.niceduck.android.sparky.model

import com.softsquared.niceduck.android.sparky.config.ApplicationClass.Companion.sRetrofit
import retrofit2.create

class AuthRepository {
    private val authService = sRetrofit.create<AuthRetrofitInterface>()

    // 로그인
    suspend fun postSignIn(request: SignInRequest) =
        authService.postSignIn(request)

    // 회원가입
    suspend fun postSignUp(request: SignUpRequest) =
        authService.postSignUp(request)

    // 인증 전송
    suspend fun postCertificationSend(request: SignUpCertificationSendRequest) =
        authService.postCertificationSend(request)

    // 인증 확인
    suspend fun postCertificationCheck(request: SignUpCertificationCheckRequest) =
        authService.postCertificationCheck(request)

    // 이메일 중복 확인
    suspend fun getDuplicationEmailCheck(email: String) =
        authService.getDuplicationEmailCheck(email)

    // 닉네임 중복 확인
    suspend fun getDuplicationNameCheck(name: String) =
        authService.getDuplicationNameCheck(name)
}
