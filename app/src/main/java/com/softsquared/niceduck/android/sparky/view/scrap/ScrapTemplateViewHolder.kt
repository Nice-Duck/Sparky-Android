package com.softsquared.niceduck.android.sparky.view.scrap

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log.d
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.TagAddItemBinding
import com.softsquared.niceduck.android.sparky.databinding.TagItemBinding
import com.softsquared.niceduck.android.sparky.model.Tag

class ScrapTemplateViewHolder(
    private val binding: TagItemBinding,
    private val onItemClick: (position: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Tag) {
        with(binding) {
            tagItemLL.backgroundTintList = ColorStateList.valueOf(Color.parseColor(item.color))
            tagItemTxt.text = item.name
            tagItemImg.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }
    }
}