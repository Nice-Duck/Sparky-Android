package com.softsquared.niceduck.android.sparky.view.scrap

interface ItemEvent {
    fun removeItem(position: Int)

    fun addItem()

    fun selectItem(position: Int)
}
