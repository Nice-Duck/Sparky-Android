package com.softsquared.niceduck.android.sparky.view.scrap

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.niceduck.android.sparky.databinding.TagAddItemBinding
import com.softsquared.niceduck.android.sparky.databinding.TagItemBinding
import com.softsquared.niceduck.android.sparky.model.TagsResponse
import com.softsquared.niceduck.android.sparky.viewmodel.ScrapTemplateViewModel

class ScrapTemplateRecyclerviewAdapter(private val viewModel: ScrapTemplateViewModel) :
    ListAdapter<TagsResponse, RecyclerView.ViewHolder>(MyDiffUtil) {

    companion object {
        const val ITEM = 1
        const val FOOTER = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == (viewModel.scrapTemplateDataSet.value?.size?.minus(1) ?: 0))
            FOOTER
        else
            ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM) {
            ScrapTemplateViewHolder(
                TagItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onItemClick = { position ->
                    viewModel.removeItem(position)
                }
            )
        } else {
            ScrapTemplateFooterViewHolder(
                TagAddItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onItemClick = {
                    viewModel.addItem()
                }
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ScrapTemplateViewHolder -> holder.bind(getItem(position))
            is ScrapTemplateFooterViewHolder -> holder.bind()
        }
    }

    object MyDiffUtil : DiffUtil.ItemCallback<TagsResponse>() {
        override fun areItemsTheSame(oldItem: TagsResponse, newItem: TagsResponse): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: TagsResponse, newItem: TagsResponse): Boolean {
            return oldItem == newItem
        }
    }
}
