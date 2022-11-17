package com.softsquared.niceduck.android.sparky.view.tag_list

import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Selection.setSelection
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.TagListItemBinding
import com.softsquared.niceduck.android.sparky.model.TagsResponse

class TagListViewHolder(
    private val binding: TagListItemBinding,
    private val onItemClick: (position: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TagsResponse) {
        with(binding) {
            try {
                tagListItemTag.backgroundTintList = ColorStateList.valueOf(Color.parseColor(item.color))
            } catch (e: Exception) {
                tagListItemTag.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#DFDFDF"))
            }

            val spinnerAdapter = CustomSpinnerAdapter(itemView.context,
                mutableListOf(*itemView.resources.getStringArray(R.array.스피너))
            )
            spinner.adapter = spinnerAdapter
            spinner.setSelection(2, false)

            tagListItemTag.text = item.name
            tagListItemBtn.setOnClickListener {
                spinner.performClick()
            }

        }
    }
}
