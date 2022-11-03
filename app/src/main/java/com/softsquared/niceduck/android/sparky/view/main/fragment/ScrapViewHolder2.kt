package com.softsquared.niceduck.android.sparky.view.main.fragment

import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.*
import com.softsquared.niceduck.android.sparky.model.Scrap
import com.softsquared.niceduck.android.sparky.view.detail.MyScrapDetailActivity
import com.softsquared.niceduck.android.sparky.view.detail.OthersScrapDetailActivity
import java.io.Serializable

class ScrapViewHolder2(
    private val binding: ScrapItem2Binding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Scrap) {
        with(binding) {

            scrapItem2TxtTitle.text = item.title
            scrapItem2TxtSummary.text = item.subTitle


            if (item.imgUrl != null && item.imgUrl != "") {
                Glide.with(itemView.context).load(item.imgUrl).transform(
                    CenterCrop(), RoundedCorners(8)
                ).into(scrapItem2Img)
            } else {
                Glide.with(itemView.context).load(R.drawable.scrap_default_img2).transform(
                    CenterCrop(), RoundedCorners(8)
                ).into(scrapItem2Img)
            }

            val tags = item.tagsResponse

            if (tags != null) {
                val tagAdapter = TagRecyclerviewAdapter(tags.subList(0, 1))
                val layoutManager = FlexboxLayoutManager(itemView.context)

                layoutManager.flexDirection = FlexDirection.ROW
                with(scrapItem2Recyclerview) {
                    this.layoutManager = layoutManager
                    adapter = tagAdapter
                }
            }

            scrapItem2ImgMoreBtn.setOnClickListener {
                val intent: Intent = if (item.type == 1) {
                    Intent(itemView.context, MyScrapDetailActivity::class.java)
                } else {
                    Intent(itemView.context, OthersScrapDetailActivity::class.java)
                }
                intent.putExtra("scrap", item)
                itemView.context.startActivity(intent)
            }

            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(item.scpUrl)
                itemView.context.startActivity(intent)
            }
        }
    }
}
