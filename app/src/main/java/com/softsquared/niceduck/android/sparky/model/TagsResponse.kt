package com.softsquared.niceduck.android.sparky.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TagsResponse(
    val color: String?,
    val name: String,
    val tagId: Int
): Parcelable