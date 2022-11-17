package com.softsquared.niceduck.android.sparky.config

import android.app.Application
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.softsquared.niceduck.android.sparky.config.ApplicationClass.Companion.sRetrofit
import com.softsquared.niceduck.android.sparky.model.AuthRepository
import com.softsquared.niceduck.android.sparky.utill.BaseResponse
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// 앱이 실행될때 1번만 실행이 됩니다.
class ApplicationClass : Application() {

    companion object {
        lateinit var sSharedPreferences: SharedPreferences
        const val BASE_URL = "https://sparkyapi.tk"
        const val X_ACCESS_TOKEN = "X-ACCESS-TOKEN"
        const val X_REFRESH_TOKEN = "X-REFRESH-TOKEN"

        lateinit var sRetrofit: Retrofit
    }

    // 앱이 처음 생성되는 순간, SP를 새로 만들어주고, 레트로핏 인스턴스를 생성합니다.
    override fun onCreate() {
        super.onCreate()

        sSharedPreferences =
            applicationContext.getSharedPreferences("SPARKY_APP", MODE_PRIVATE)
        // 레트로핏 인스턴스 생성
        initRetrofitInstance()
    }

    // 레트로핏 인스턴스를 생성하고, 레트로핏에 각종 설정값들을 지정해줍니다.
    // 연결 타임아웃시간은 5초로 지정이 되어있고, HttpLoggingInterceptor를 붙여서 어떤 요청이 나가고 들어오는지를 보여줍니다.
    private fun initRetrofitInstance() {
        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(XAccessTokenInterceptor()) // JWT 자동 헤더 전송
            .build()

        // sRetrofit 이라는 전역변수에 API url, 인터셉터, Gson을 넣어주고 빌드해주는 코드
        // 이 전역변수로 http 요청을 서버로 보내면 됩니다.

        var gson = GsonBuilder().setLenient().create()
        sRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}
