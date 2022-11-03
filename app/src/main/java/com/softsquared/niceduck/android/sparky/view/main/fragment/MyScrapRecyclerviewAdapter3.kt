package com.softsquared.niceduck.android.sparky.view.main.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.softsquared.niceduck.android.sparky.databinding.ScrapItem4Binding
import com.softsquared.niceduck.android.sparky.model.Scrap

class MyScrapRecyclerviewAdapter3() :
    ListAdapter<Scrap, ScrapViewHolder4>(MyDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapViewHolder4 {
        return ScrapViewHolder4(
            ScrapItem4Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ScrapViewHolder4, position: Int) =
        holder.bind(getItem(position))

    object MyDiffUtil : DiffUtil.ItemCallback<Scrap>() {
        override fun areItemsTheSame(oldItem: Scrap, newItem: Scrap): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Scrap, newItem: Scrap): Boolean {
            return oldItem == newItem
        }
    }
}
