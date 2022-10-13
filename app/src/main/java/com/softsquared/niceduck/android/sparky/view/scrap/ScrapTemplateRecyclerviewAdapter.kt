package com.softsquared.niceduck.android.sparky.view.scrap

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat.setBackgroundTintList
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.TagAddItemBinding
import com.softsquared.niceduck.android.sparky.databinding.TagItemBinding
import com.softsquared.niceduck.android.sparky.model.Tag

class ScrapTemplateRecyclerviewAdapter(private val viewModel: ItemEvent)
    : ListAdapter<Tag, RecyclerView.ViewHolder>(MyDiffUtil) {

    companion object {
        const val ITEM = 1
        const val FOOTER = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == (viewModel.getScrapTemplateDataSet()?.size?.minus(1) ?: 0))
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
            })
        } else {
            ScrapTemplateFooterViewHolder(
                TagAddItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onItemClick = {
                    viewModel.addItem()
                })
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ScrapTemplateViewHolder -> holder.bind(getItem(position))
            is ScrapTemplateFooterViewHolder -> holder.bind()
        }
    }


    object MyDiffUtil : DiffUtil.ItemCallback<Tag>(){
        override fun areItemsTheSame(oldItem: Tag, newItem: Tag): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Tag, newItem: Tag): Boolean {
            return oldItem == newItem
        }
    }

}