package com.softsquared.niceduck.android.sparky.view.main.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.softsquared.niceduck.android.sparky.databinding.ScrapItem3Binding
import com.softsquared.niceduck.android.sparky.model.Scrap

class MyScrapRecyclerviewAdapter2() :
    ListAdapter<Scrap, ScrapViewHolder3>(MyDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapViewHolder3 {
        return ScrapViewHolder3(
            ScrapItem3Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ScrapViewHolder3, position: Int) =
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
