package com.softsquared.niceduck.android.sparky.view.detail

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.ActivityScrapModifyBinding
import com.softsquared.niceduck.android.sparky.model.Scrap
import com.softsquared.niceduck.android.sparky.model.TagsResponse
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import com.softsquared.niceduck.android.sparky.view.scrap.ScrapBottomDialogFragment
import com.softsquared.niceduck.android.sparky.viewmodel.ScrapTemplateViewModel

class ScrapModifyActivity : BaseActivity<ActivityScrapModifyBinding>(
    ActivityScrapModifyBinding::inflate) {
    private val scrapTemplateViewModel: ScrapTemplateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.scrapModifyImgThumbnail.setOnClickListener {

        }



        val scrap: Scrap? = intent.getParcelableExtra("scrap")
        if (scrap != null) {

            scrapTemplateViewModel.memo = scrap.memo
            scrapTemplateViewModel.subTitle.setValue(scrap.subTitle)
            scrapTemplateViewModel.title.setValue(scrap.title)
            scrapTemplateViewModel.url = scrap.scpUrl
            scrapTemplateViewModel.img.setValue(scrap.imgUrl)

            binding.scrapModifyBtnStore.setOnClickListener {
                scrapTemplateViewModel.patchScrap(scrap.scrapId.toString())
            }

            binding.scrapModifyEditTxtTitle.text = scrap.title
            binding.scrapModifyEditTxtSummary.text = scrap.subTitle
            binding.scrapModifyEditTxtMemo.setText(scrap.memo)

            if (scrap.imgUrl != null && scrap.imgUrl != "") {
                Glide.with(this).load(scrap.imgUrl).transform(
                    CenterCrop(), RoundedCorners(8)
                ).into(binding.scrapModifyImgThumbnail)
            } else {
                Glide.with(this).load(R.drawable.scrap_default_img1).transform(
                    CenterCrop(), RoundedCorners(8)
                ).into(binding.scrapModifyImgThumbnail)
            }

            val tags = scrap.tagsResponse
            if (tags != null) {
                tags.forEach {
                    var i = scrapTemplateViewModel.scrapTemplateDataSet.value!!.size - 1
                    scrapTemplateViewModel.scrapTemplateDataSet.value!!.add(i, it)
                }
                val newTagList = scrapTemplateViewModel.scrapTemplateDataSet.value
                scrapTemplateViewModel.scrapTemplateDataSet.value = newTagList

            }

        }



        setScrapTemplateRecyclerview()

        scrapTemplateViewModel.scrapUpdateResponse.observe(this) {
            // TODO: 실패 코드 추가
            if (it.code == "0000") {
                finish()
            }
        }

        scrapTemplateViewModel.scrapUpdateFailure.observe(this) {

        }


        scrapTemplateViewModel.scrapTemplateDataSet.observe(
            this,
            Observer { it ->
                scrapTemplateViewModel.scrapTemplateRecyclerviewAdapter.submitList(it.toMutableList())
                scrapTemplateViewModel.tags.clear()
                it.forEach { tag ->
                    if (tag.name != "") scrapTemplateViewModel.tags.add(tag.tagId)
                }
            }
        )

        scrapTemplateViewModel.tagLastLoadResponse.observe(this) {
            // TODO: 실패 코드 추가
            if (it.code == "0000") {
                it.result.tagResponses?.let { response ->
                    scrapTemplateViewModel.lastTags.setValue(
                        response
                    )
                }
            }
        }
        scrapTemplateViewModel.scrapStoreResponse.observe(this) {
            // TODO: 실패 코드 추가
            if (it.code == "0000") {
                finish()
            }
        }

        binding.scrapModifyEditTxtMemo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.scrapModifyEditTxtMemo.run {
                    scrapTemplateViewModel.memo = text.toString()
                    if (text.isNotEmpty()) {
                        setBackgroundResource(R.drawable.sign_input_focused)
                    } else {
                        setBackgroundResource(R.drawable.sign_input_selector)
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.scrapModifyEditTxtTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.scrapModifyEditTxtTitle.run {
                    scrapTemplateViewModel.title.setValue(text.toString())
                }
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.scrapModifyEditTxtSummary.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.scrapModifyEditTxtSummary.run {
                    scrapTemplateViewModel.subTitle.setValue(text.toString())
                }
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })


        scrapTemplateViewModel.showBottomSheetCall.observe(this) {
            scrapTemplateViewModel.tagColor.setValue(scrapTemplateViewModel.randomColor())
            val bottomDialogFragment = ScrapBottomDialogFragment()
            bottomDialogFragment.show(supportFragmentManager, bottomDialogFragment.tag)
        }

    }

    private fun setScrapTemplateRecyclerview() {
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        with(binding.scrapModifyRecyclerview) {
            this.layoutManager = layoutManager
            adapter = scrapTemplateViewModel.scrapTemplateRecyclerviewAdapter
            visibility = android.view.View.VISIBLE
        }
    }

}