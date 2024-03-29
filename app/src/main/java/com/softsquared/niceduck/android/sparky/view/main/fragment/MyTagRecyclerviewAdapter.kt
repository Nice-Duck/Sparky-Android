package com.softsquared.niceduck.android.sparky.view.main.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.softsquared.niceduck.android.sparky.databinding.TagItemBinding
import com.softsquared.niceduck.android.sparky.model.TagsResponse
import com.softsquared.niceduck.android.sparky.view.scrap.ScrapTemplateFooterViewHolder
import com.softsquared.niceduck.android.sparky.view.scrap.ScrapTemplateRecyclerviewAdapter.Companion.FOOTER
import com.softsquared.niceduck.android.sparky.view.scrap.ScrapTemplateRecyclerviewAdapter.Companion.ITEM
import com.softsquared.niceduck.android.sparky.view.scrap.ScrapTemplateViewHolder
import com.softsquared.niceduck.android.sparky.viewmodel.MainViewModel
import com.softsquared.niceduck.android.sparky.viewmodel.ScrapTemplateViewModel

class MyTagRecyclerviewAdapter(private val viewModel: MainViewModel) :
    ListAdapter<TagsResponse,  ScrapTemplateViewHolder>(MyDiffUtil) {

    override fun onBindViewHolder(holder: ScrapTemplateViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  ScrapTemplateViewHolder {
        return ScrapTemplateViewHolder(
                TagItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onItemClick = { position ->
                    viewModel.removeItem(position)
                }
            )
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
