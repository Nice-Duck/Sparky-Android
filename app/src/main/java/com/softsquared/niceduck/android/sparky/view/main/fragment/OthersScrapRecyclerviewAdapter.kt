package com.softsquared.niceduck.android.sparky.view.main.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.niceduck.android.sparky.databinding.ScrapItem2Binding
import com.softsquared.niceduck.android.sparky.databinding.ScrapItem3Binding
import com.softsquared.niceduck.android.sparky.databinding.ScrapItem4Binding
import com.softsquared.niceduck.android.sparky.model.ScrapDataModel
import com.softsquared.niceduck.android.sparky.viewmodel.MainViewModel

class OthersScrapRecyclerviewAdapter(private val viewModel: MainViewModel) :
    ListAdapter<ScrapDataModel, RecyclerView.ViewHolder>(MyDiffUtil) {

    companion object {
        const val ITEM1 = 1
        const val ITEM2 = 2
        const val ITEM3 = 3
    }

    override fun getItemViewType(position: Int): Int {
        val i = (position + 1) % 6
        return when (i) {
            1, 2 -> ITEM1
            3, 4 -> ITEM2
            else -> ITEM3
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM1 -> {
                ScrapViewHolder2(
                    ScrapItem2Binding.inflate(LayoutInflater.from(parent.context), parent, false),
                    viewModel
                )
            }
            ITEM2 -> {
                ScrapViewHolder3(
                    ScrapItem3Binding.inflate(LayoutInflater.from(parent.context), parent, false),
                    viewModel
                )
            }
            else -> {
                ScrapViewHolder4(
                    ScrapItem4Binding.inflate(LayoutInflater.from(parent.context), parent, false),
                    viewModel
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (holder) {
            is ScrapViewHolder1 -> holder.bind(getItem(position))
            is ScrapViewHolder2 -> holder.bind(getItem(position))
            is ScrapViewHolder3 -> holder.bind(getItem(position))
            is ScrapViewHolder4 -> holder.bind(getItem(position))
            else -> {
            }
        }

    object MyDiffUtil : DiffUtil.ItemCallback<ScrapDataModel>() {
        override fun areItemsTheSame(oldItem: ScrapDataModel, newItem: ScrapDataModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ScrapDataModel, newItem: ScrapDataModel): Boolean {
            return oldItem == newItem
        }
    }
}
