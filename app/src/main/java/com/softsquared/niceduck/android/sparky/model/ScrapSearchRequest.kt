package com.softsquared.niceduck.android.sparky.model

data class ScrapSearchRequest(
    val tags: List<Int>?,
    val title: String?,
    val type: Int?
)