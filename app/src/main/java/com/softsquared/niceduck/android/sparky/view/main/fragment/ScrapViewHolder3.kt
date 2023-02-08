package com.softsquared.niceduck.android.sparky.view.main.fragment

import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.*
import com.softsquared.niceduck.android.sparky.model.Scrap
import com.softsquared.niceduck.android.sparky.view.detail.MyScrapDetailActivity
import com.softsquared.niceduck.android.sparky.view.detail.OthersScrapDetailActivity
import com.softsquared.niceduck.android.sparky.view.web.WebViewActivity
import java.io.Serializable

class ScrapViewHolder3(
    private val binding: ScrapItem3Binding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Scrap) {
        with(binding) {

            scrapItem3TxtTitle.text = item.title
            scrapItem3TxtSummary.text = item.subTitle


            if (item.imgUrl != null && item.imgUrl != "") {
                Glide.with(itemView.context).load(item.imgUrl).error(R.drawable.scrap_default_img1).transform(
                    CenterCrop(), RoundedCorners(8)
                ).into(scrapItem3Img)
            } else {
                Glide.with(itemView.context).load(R.drawable.scrap_default_img1).transform(
                    CenterCrop(), RoundedCorners(8)
                ).into(scrapItem3Img)
            }

            val tags = item.tagsResponse

            if (!tags.isNullOrEmpty()) {
                val tagAdapter = TagRecyclerviewAdapter(tags)
                val layoutManager = FlexboxLayoutManager(itemView.context)

                layoutManager.flexDirection = FlexDirection.ROW
                with(scrapItem3Recyclerview) {
                    this.layoutManager = layoutManager
                    adapter = tagAdapter
                }
            }

            scrapItem3ImgMoreBtn.setOnClickListener {
                val intent: Intent = if (item.type == 1) {
                    Intent(itemView.context, MyScrapDetailActivity::class.java)
                } else {
                    Intent(itemView.context, OthersScrapDetailActivity::class.java)
                }
                intent.putExtra("scrap", item)
                itemView.context.startActivity(intent)
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, WebViewActivity::class.java)
                intent.putExtra("url", item.scpUrl)
                itemView.context.startActivity(intent)
            }
        }
    }
}
