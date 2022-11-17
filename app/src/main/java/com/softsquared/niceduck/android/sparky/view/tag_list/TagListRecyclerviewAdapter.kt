package com.softsquared.niceduck.android.sparky.view.tag_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.softsquared.niceduck.android.sparky.databinding.ScrapItem1Binding
import com.softsquared.niceduck.android.sparky.databinding.TagListItemBinding
import com.softsquared.niceduck.android.sparky.model.Scrap
import com.softsquared.niceduck.android.sparky.model.TagsResponse
import com.softsquared.niceduck.android.sparky.view.main.fragment.ScrapViewHolder1
import com.softsquared.niceduck.android.sparky.view.scrap.ItemEvent
import com.softsquared.niceduck.android.sparky.viewmodel.MainViewModel
import com.softsquared.niceduck.android.sparky.viewmodel.MyPageViewModel

class TagListRecyclerviewAdapter(private val view: ItemEvent) :
    ListAdapter<TagsResponse, TagListViewHolder>(MyDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagListViewHolder {
        return TagListViewHolder(
            TagListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClick = { position ->
                view.selectItem(position)
            },
            onItemClick2 = { position ->
                view.removeItem(position)
            }
        )
    }

    override fun onBindViewHolder(holder: TagListViewHolder, position: Int) =
        holder.bind(getItem(position))

    object MyDiffUtil : DiffUtil.ItemCallback<TagsResponse>() {
        override fun areItemsTheSame(oldItem: TagsResponse, newItem: TagsResponse): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: TagsResponse, newItem: TagsResponse): Boolean {
            return oldItem == newItem
        }
    }
}
