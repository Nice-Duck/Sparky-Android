package com.softsquared.niceduck.android.sparky.view.main.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log.d
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.*
import com.softsquared.niceduck.android.sparky.model.ScrapDataModel
import com.softsquared.niceduck.android.sparky.model.Tag
import com.softsquared.niceduck.android.sparky.view.scrap.TagAddRecyclerviewAdapter
import com.softsquared.niceduck.android.sparky.viewmodel.MainViewModel

class ScrapViewHolder5(
    private val binding: ScrapItem5Binding,
    private val mainViewModel: MainViewModel
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ScrapDataModel) {
        with(binding) {

            scrapItem5TxtTitle.text = item.title
            scrapItem5TxtSummary.text = item.memo

            scrapItem5ImgMoreBtn.setOnClickListener {
                mainViewModel.selectItem(adapterPosition)
            }

            if (item.imgUrl != null) {
                Glide.with(itemView.context).load(item.imgUrl).centerCrop().into(scrapItem5Img)
            } else {
                Glide.with(itemView.context).load(R.drawable.test_img).centerCrop().into(scrapItem5Img)
            }

            val tags = item.tags

            if (tags != null) {
                if (tags.size > 4) {
                    val tagAdapter = TagRecyclerviewAdapter(tags.subList(0, 4))
                    val layoutManager = FlexboxLayoutManager(itemView.context)

                    layoutManager.flexDirection = FlexDirection.ROW
                    with(scrapItem5Recyclerview) {
                        this.layoutManager = layoutManager
                        adapter = tagAdapter
                    }
                } else {
                    val tagAdapter = TagRecyclerviewAdapter(tags)
                    val layoutManager = FlexboxLayoutManager(itemView.context)

                    layoutManager.flexDirection = FlexDirection.ROW
                    with(scrapItem5Recyclerview) {
                        this.layoutManager = layoutManager
                        adapter = tagAdapter
                    }
                }

            }
        }
    }
}