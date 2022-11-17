package com.softsquared.niceduck.android.sparky.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Scrap(
    val type: Int? = 1,
    val imgUrl: String? = "",
    val memo: String? = "",
    val scpUrl: String? = "",
    val scrapId: Int?,
    val subTitle: String? = "",
    val tagsResponse: ArrayList<TagsResponse>?,
    val title: String? = ""
): Parcelable