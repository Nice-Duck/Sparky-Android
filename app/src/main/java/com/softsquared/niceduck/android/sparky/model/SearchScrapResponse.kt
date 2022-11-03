package com.softsquared.niceduck.android.sparky.model

data class SearchScrapResponse(
    val code: String,
    val message: String,
    val result: List<Scrap>
)