package com.softsquared.niceduck.android.sparky.model

data class UserResponse(
    val code: String,
    val message: String,
    val result: UserResult
)