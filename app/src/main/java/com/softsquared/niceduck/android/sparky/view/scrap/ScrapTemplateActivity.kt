package com.softsquared.niceduck.android.sparky.view.scrap

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.ActivityScrapTemplateBinding
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import com.softsquared.niceduck.android.sparky.viewmodel.ScrapTemplateViewModel


class ScrapTemplateActivity : BaseActivity<ActivityScrapTemplateBinding>(ActivityScrapTemplateBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val scrapTemplateViewModel = ScrapTemplateViewModel()

        d("테스트", "" +
                "${intent}\n ${intent}\n" +
                "${intent.getStringExtra(Intent.EXTRA_TEXT )}\n" +
                "${intent.extras}\n " +
                "${intent.type}\n " +
                "${intent.component}\n" +
                "${intent.sourceBounds}")

        scrapTemplateViewModel.getScrapData(intent)

        scrapTemplateViewModel.url.observe(this, Observer {

        })

        scrapTemplateViewModel.title.observe(this, Observer {
            binding.scrapTemplateEditTxtTitle.setText(it)
        })

        scrapTemplateViewModel.img.observe(this, Observer {
            // Glide 옵션 fitCenter() or centerCrop()
            if (it == "") {
                Glide.with(this@ScrapTemplateActivity).load(getDrawable(R.drawable.sparky)).centerCrop().into(binding.scrapTemplateImgThumbnail)
            } else {
                Glide.with(this@ScrapTemplateActivity).load(it).centerCrop().into(binding.scrapTemplateImgThumbnail)
            }
        })

        scrapTemplateViewModel.memo.observe(this, Observer {
            binding.scrapTemplateEditTxtMemo.setText(it)
            binding.scrapTemplateEditTxtSummary.setText(it)
        })

        scrapTemplateViewModel.tags.observe(this, Observer {

        })
    }

}