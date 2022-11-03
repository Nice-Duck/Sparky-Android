package com.softsquared.niceduck.android.sparky.view.main.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.softsquared.niceduck.android.sparky.databinding.ScrapItem1Binding
import com.softsquared.niceduck.android.sparky.model.Scrap

class MyScrapRecyclerviewAdapter() :
    ListAdapter<Scrap, ScrapViewHolder1>(MyDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapViewHolder1 {
        return ScrapViewHolder1(
            ScrapItem1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ScrapViewHolder1, position: Int) =
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
