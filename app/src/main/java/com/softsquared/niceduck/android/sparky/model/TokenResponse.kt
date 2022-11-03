package com.softsquared.niceduck.android.sparky.model

import android.media.session.MediaSession
import com.softsquared.niceduck.android.sparky.utill.BaseResponse

data class TokenResponse(
    val result: TokenResult? = null
) : BaseResponse()
