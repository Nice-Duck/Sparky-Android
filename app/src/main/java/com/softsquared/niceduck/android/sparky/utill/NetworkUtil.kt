package com.softsquared.niceduck.android.sparky.utill

import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import okhttp3.ResponseBody

object NetworkUtil {
    fun getErrorResponse(errorBody: ResponseBody): BaseResponse? {
        return ApplicationClass.sRetrofit.responseBodyConverter<BaseResponse >(
            BaseResponse::class.java,
            BaseResponse::class.java.annotations
        ).convert(errorBody)
    }
}