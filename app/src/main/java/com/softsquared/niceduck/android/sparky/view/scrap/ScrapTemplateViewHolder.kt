package com.softsquared.niceduck.android.sparky.view.scrap

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.niceduck.android.sparky.databinding.TagItemBinding
import com.softsquared.niceduck.android.sparky.model.TagsResponse

class ScrapTemplateViewHolder(
    private val binding: TagItemBinding,
    private val onItemClick: (position: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TagsResponse) {
        with(binding) {
            try {
                tagItemLL.backgroundTintList = ColorStateList.valueOf(Color.parseColor(item.color))
            } catch (e: Exception) {
                tagItemLL.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#DFDFDF"))
            }
            tagItemTxt.text = item.name
            tagItemImg.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }
    }
}
