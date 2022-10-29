package com.softsquared.niceduck.android.sparky.view.main.fragment

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.*
import com.softsquared.niceduck.android.sparky.model.Scrap
import com.softsquared.niceduck.android.sparky.model.ScrapDataModel
import com.softsquared.niceduck.android.sparky.viewmodel.MainViewModel

class ScrapViewHolder3(
    private val binding: ScrapItem3Binding,
    private val mainViewModel: MainViewModel
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Scrap) {
        with(binding) {

            scrapItem3TxtTitle.text = item.title
            scrapItem3TxtSummary.text = item.memo

            scrapItem3ImgMoreBtn.setOnClickListener {
                mainViewModel.selectItem(adapterPosition)
            }

            if (item.imgUrl != null) {
                Glide.with(itemView.context).load(item.imgUrl).transform(
                    CenterCrop(), RoundedCorners(8)
                ).into(scrapItem3Img)
            } else {
                Glide.with(itemView.context).load(R.drawable.scrap_default_img3).transform(
                    CenterCrop(), RoundedCorners(8)
                ).into(scrapItem3Img)
            }

            val tags = item.tagsResponse

            if (tags != null) {
                if (tags.size > 4) {
                    val tagAdapter = TagRecyclerviewAdapter(tags.subList(0, 4))
                    val layoutManager = FlexboxLayoutManager(itemView.context)

                    layoutManager.flexDirection = FlexDirection.ROW
                    with(scrapItem3Recyclerview) {
                        this.layoutManager = layoutManager
                        adapter = tagAdapter
                    }
                } else {
                    val tagAdapter = TagRecyclerviewAdapter(tags)
                    val layoutManager = FlexboxLayoutManager(itemView.context)

                    layoutManager.flexDirection = FlexDirection.ROW
                    with(scrapItem3Recyclerview) {
                        this.layoutManager = layoutManager
                        adapter = tagAdapter
                    }
                }
            }
        }
    }
}
