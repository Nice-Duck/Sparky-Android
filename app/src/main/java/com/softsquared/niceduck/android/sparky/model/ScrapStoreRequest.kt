package com.softsquared.niceduck.android.sparky.model

data class ScrapStoreRequest(
    val imgUrl: String? = null,
    val memo: String? = null,
    val scpUrl: String? = null,
    val tags: ArrayList<Int>? = null,
    val title: String? = null,
    val subTitle: String? = null
)
