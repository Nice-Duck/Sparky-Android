package com.softsquared.niceduck.android.sparky.model

data class Scrap(
    val imgUrl: String,
    val memo: String,
    val scpUrl: String,
    val scrapId: Int,
    val subTitle: String,
    val tagsResponse: ArrayList<TagsResponse>,
    val title: String
)