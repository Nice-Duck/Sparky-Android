package com.softsquared.niceduck.android.sparky.view.scrap

import androidx.recyclerview.widget.RecyclerView
import com.softsquared.niceduck.android.sparky.databinding.TagAddItemBinding

class ScrapTemplateFooterViewHolder(
    private val binding: TagAddItemBinding,
    private val onItemClick: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        binding.tagAddItemLL.setOnClickListener {
            onItemClick()
        }
    }
}
