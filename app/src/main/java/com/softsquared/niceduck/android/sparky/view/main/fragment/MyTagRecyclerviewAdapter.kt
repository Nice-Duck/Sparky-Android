package com.softsquared.niceduck.android.sparky.view.main.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.niceduck.android.sparky.databinding.TagAddItem2Binding
import com.softsquared.niceduck.android.sparky.databinding.TagAddItemBinding
import com.softsquared.niceduck.android.sparky.databinding.TagItemBinding
import com.softsquared.niceduck.android.sparky.model.TagsResponse
import com.softsquared.niceduck.android.sparky.view.scrap.ScrapTemplateFooterViewHolder
import com.softsquared.niceduck.android.sparky.view.scrap.ScrapTemplateRecyclerviewAdapter.Companion.FOOTER
import com.softsquared.niceduck.android.sparky.view.scrap.ScrapTemplateViewHolder
import com.softsquared.niceduck.android.sparky.viewmodel.MainViewModel
import com.softsquared.niceduck.android.sparky.viewmodel.ScrapTemplateViewModel

class MyTagRecyclerviewAdapter(private val viewModel: MainViewModel) :
    ListAdapter<TagsResponse, RecyclerView.ViewHolder>(MyDiffUtil) {

    companion object {
        const val ITEM = 1
        const val HEADER = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0)
            HEADER
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
            MyHeaderViewHolder(
                TagAddItem2Binding.inflate(LayoutInflater.from(parent.context), parent, false),
                onItemClick = {
                    viewModel.addItem()
                }
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ScrapTemplateViewHolder -> holder.bind(getItem(position))
            is MyHeaderViewHolder -> holder.bind()
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
