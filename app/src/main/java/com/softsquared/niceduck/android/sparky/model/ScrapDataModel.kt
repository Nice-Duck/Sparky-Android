package com.softsquared.niceduck.android.sparky.model

data class ScrapDataModel(
    val imgUrl: String? = null,
    val memo: String? = null,
    val scpUrl: String? = null,
    val tags: List<Tag>? = null,
    val title: String? = null
)