package com.softsquared.niceduck.android.sparky.view.scrap

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.niceduck.android.sparky.databinding.TagItem2Binding
import com.softsquared.niceduck.android.sparky.model.TagsResponse

class TagAddViewHolder(
    private val binding: TagItem2Binding,
    private val onItemClick: (position: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TagsResponse) {
        with(binding) {
            tagItem2Txt.text = item.name
            try {
                tagItem2LL.backgroundTintList = ColorStateList.valueOf(Color.parseColor(item.color))
            } catch (e: Exception) {
                tagItem2LL.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#DFDFDF"))
                Log.d("테스트", item.color.toString())
            }
            tagItem2LL.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }
    }
}
