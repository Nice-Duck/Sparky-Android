package com.softsquared.niceduck.android.sparky.view.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.ActivityMainBinding
import com.softsquared.niceduck.android.sparky.databinding.ActivityMyScrapDetailBinding
import com.softsquared.niceduck.android.sparky.model.Scrap
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import com.softsquared.niceduck.android.sparky.view.main.fragment.TagRecyclerviewAdapter
import com.softsquared.niceduck.android.sparky.view.main.fragment.TagRecyclerviewAdapter2
import com.softsquared.niceduck.android.sparky.viewmodel.ScrapDetailViewModel
import com.softsquared.niceduck.android.sparky.viewmodel.ScrapTemplateViewModel

class MyScrapDetailActivity : BaseActivity<ActivityMyScrapDetailBinding>(ActivityMyScrapDetailBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scrapDetailViewModel: ScrapDetailViewModel by viewModels()

        binding.myScrapDetailImgBack.setOnClickListener {
            finish()
        }

        scrapDetailViewModel.scrapDeleteResponse.observe(this) {
            // TODO: 실패 코드 추가
            if (it.code == "0000") {
                finish()
            }
        }

        scrapDetailViewModel.scrapDeleteFailure.observe(this) {

        }

        val scrap: Scrap? = intent.getParcelableExtra("scrap")

        if (scrap != null) {
            binding.myScrapDetailTxtModify.setOnClickListener {
                val intent = Intent(this, ScrapModifyActivity::class.java)
                intent.putExtra("scrap", scrap)
                startActivity(intent)
            }

            binding.myScrapDetailTxtUrlDelete.setOnClickListener {
                scrapDetailViewModel.deleteScrap(scrap.scrapId.toString())
            }

            binding.myScrapDetailEditTxtTitle.text = scrap.title
            binding.myScrapDetailTxtSummary.text = scrap.subTitle
            binding.myScrapDetailEditTxtMemo.setText(scrap.memo)

            if (scrap.imgUrl != null && scrap.imgUrl != "") {
                Glide.with(this).load(scrap.imgUrl).transform(
                    CenterCrop(), RoundedCorners(8)
                ).into(binding.myScrapDetailImgThumbnail)
            } else {
                Glide.with(this).load(R.drawable.scrap_default_img1).transform(
                    CenterCrop(), RoundedCorners(8)
                ).into(binding.myScrapDetailImgThumbnail)
            }

            val tags = scrap.tagsResponse

            if (tags != null) {
                    val tagAdapter = TagRecyclerviewAdapter2(tags)
                    val layoutManager = FlexboxLayoutManager(this)

                    layoutManager.flexDirection = FlexDirection.ROW
                    with(binding.myScrapDetailRecyclerview) {
                        this.layoutManager = layoutManager
                        adapter = tagAdapter
                    }

            }



        }
    }
}