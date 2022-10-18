package com.softsquared.niceduck.android.sparky.view.main.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.softsquared.niceduck.android.sparky.databinding.ScrapItem1Binding
import com.softsquared.niceduck.android.sparky.databinding.ScrapItem3Binding
import com.softsquared.niceduck.android.sparky.databinding.ScrapItem4Binding
import com.softsquared.niceduck.android.sparky.databinding.ScrapItem5Binding
import com.softsquared.niceduck.android.sparky.model.ScrapDataModel
import com.softsquared.niceduck.android.sparky.viewmodel.MainViewModel

class MyScrapRecyclerviewAdapter3(private val viewModel: MainViewModel)
    : ListAdapter<ScrapDataModel, ScrapViewHolder4>(MyDiffUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapViewHolder4 {
        return ScrapViewHolder4(
            ScrapItem4Binding.inflate(LayoutInflater.from(parent.context), parent, false),
            viewModel)
    }

    override fun onBindViewHolder(holder: ScrapViewHolder4, position: Int) =
        holder.bind(getItem(position))


    object MyDiffUtil : DiffUtil.ItemCallback<ScrapDataModel>() {
        override fun areItemsTheSame(oldItem: ScrapDataModel, newItem: ScrapDataModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ScrapDataModel, newItem: ScrapDataModel): Boolean {
            return oldItem == newItem
        }
    }
}