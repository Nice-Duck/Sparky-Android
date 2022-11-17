package com.softsquared.niceduck.android.sparky.view.detail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.ActivityMyScrapDetailBinding
import com.softsquared.niceduck.android.sparky.databinding.ActivityOthersScrapDetailBinding
import com.softsquared.niceduck.android.sparky.model.Scrap
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import com.softsquared.niceduck.android.sparky.view.main.fragment.TagRecyclerviewAdapter
import com.softsquared.niceduck.android.sparky.view.main.fragment.TagRecyclerviewAdapter2
import com.softsquared.niceduck.android.sparky.view.scrap.ScrapTemplateActivity

class OthersScrapDetailActivity: BaseActivity<ActivityOthersScrapDetailBinding>(
    ActivityOthersScrapDetailBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.othersScrapDetailImgBack.setOnClickListener {
            finish()
        }

        val scrap: Scrap? = intent.getParcelableExtra("scrap")
        if (scrap != null) {
            binding.othersScrapDetailTxtShare.setOnClickListener {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, scrap.scpUrl)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }

            binding.othersScrapDetailTxtScrapAdd.setOnClickListener {
                val intent = Intent(this, ScrapTemplateActivity::class.java)
                intent.putExtra("add", scrap.scpUrl)
                startActivity(intent)
                finish()
            }

            binding.othersScrapDetailTxtUrlCopy.setOnClickListener {
                val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip : ClipData = ClipData.newPlainText("url", scrap.scpUrl)
                showCustomToast("URL이 복사되었습니다.")
                clipboardManager.setPrimaryClip(clip)
            }

            binding.othersScrapDetailEditTxtTitle.text = scrap.title
            binding.othersScrapDetailTxtSummary.text = scrap.subTitle
            binding.othersScrapDetailEditTxtMemo.setText(scrap.memo)

            if (scrap.imgUrl != null && scrap.imgUrl != "") {
                Glide.with(this).load(scrap.imgUrl).transform(
                    CenterCrop(), RoundedCorners(8)
                )   .into(binding.othersScrapDetailImgThumbnail)
            } else {
                Glide.with(this).load(R.drawable.scrap_default_img1).transform(
                    CenterCrop(), RoundedCorners(8)
                ).into(binding.othersScrapDetailImgThumbnail)
            }

            val tags = scrap.tagsResponse

            if (tags != null) {
                    val tagAdapter = TagRecyclerviewAdapter2(tags)
                    val layoutManager = FlexboxLayoutManager(this)

                    layoutManager.flexDirection = FlexDirection.ROW
                    with(binding.othersScrapDetailRecyclerview) {
                        this.layoutManager = layoutManager
                        adapter = tagAdapter
                    }
            }


        }

    }
}