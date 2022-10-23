package com.softsquared.niceduck.android.sparky.view.scrap

import android.util.Log.d
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.softsquared.niceduck.android.sparky.databinding.TagItem2Binding
import com.softsquared.niceduck.android.sparky.model.Tag

class TagAddRecyclerviewAdapter(private val viewModel: ItemEvent) :
    ListAdapter<Tag, TagAddViewHolder>(MyDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagAddViewHolder {
        return TagAddViewHolder(
            TagItem2Binding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClick = { position ->
                viewModel.selectItem(position)
                d("선택 테스트", position.toString())
            }
        )
    }

    override fun onBindViewHolder(holder: TagAddViewHolder, position: Int) = holder.bind(getItem(position))

    object MyDiffUtil : DiffUtil.ItemCallback<Tag>() {
        override fun areItemsTheSame(oldItem: Tag, newItem: Tag): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Tag, newItem: Tag): Boolean {
            return oldItem == newItem
        }
    }
}
