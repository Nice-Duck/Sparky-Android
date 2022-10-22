package com.softsquared.niceduck.android.sparky.view.scrap

import com.softsquared.niceduck.android.sparky.model.Tag


interface ItemEvent {
    fun removeItem(position: Int)

    fun addItem()

    fun selectItem(position: Int)

}