package com.softsquared.niceduck.android.sparky.view.main.fragment

import androidx.recyclerview.widget.RecyclerView
import com.softsquared.niceduck.android.sparky.databinding.TagAddItem2Binding
import com.softsquared.niceduck.android.sparky.databinding.TagAddItemBinding

class MyHeaderViewHolder(
    private val binding: TagAddItem2Binding,
    private val onItemClick: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        binding.tagAddItem2LL.setOnClickListener {
            onItemClick()
        }
    }
}
