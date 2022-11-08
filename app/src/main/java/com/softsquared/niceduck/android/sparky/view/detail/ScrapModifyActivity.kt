package com.softsquared.niceduck.android.sparky.view.detail

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import com.softsquared.niceduck.android.sparky.databinding.ActivityScrapModifyBinding
import com.softsquared.niceduck.android.sparky.model.Scrap
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import com.softsquared.niceduck.android.sparky.view.scrap.ScrapBottomDialogFragment
import com.softsquared.niceduck.android.sparky.view.sign_in.SignInActivity
import com.softsquared.niceduck.android.sparky.viewmodel.ScrapTemplateViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ScrapModifyActivity : BaseActivity<ActivityScrapModifyBinding>(
    ActivityScrapModifyBinding::inflate) {
    private val scrapTemplateViewModel: ScrapTemplateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dlg = Dialog(this)
        dlg.setCancelable(false)
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setContentView(R.layout.dialog_lottie_loading)
        dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 토큰 갱신
        scrapTemplateViewModel.reissueAccessTokenResponse.observe(this) { response ->
            when (response.code) {
                "0000" -> {
                    val newToken = response.result?.accessToken
                    val editor = ApplicationClass.sSharedPreferences.edit()
                    editor.putString(ApplicationClass.X_ACCESS_TOKEN, newToken)
                    editor.apply()
                }
                else -> {
                    showCustomToast("네트워크 연결이 원활하지 않습니다.")
                }
            }

        }

        scrapTemplateViewModel.reissueAccessTokenFailure.observe(this) { response ->
            val editor = ApplicationClass.sSharedPreferences.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this, SignInActivity::class.java)
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        binding.scrapModifyImgThumbnail.setOnClickListener {
            // TODO: 갤러리에서 이미지 추가
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
                dlg.show()
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
            lifecycleScope.launch {
                delay(1000)
                dlg.dismiss()
            }
            if (it.code == "0000") {
                finish()
            }
        }

        scrapTemplateViewModel.scrapUpdateFailure.observe(this) {
            lifecycleScope.launch  {
                delay(1000)
                dlg.dismiss()
            }

            when (it.code) {
                "U000" -> {
                    lifecycleScope.launch {
                        scrapTemplateViewModel.postReissueAccessToken()
                        scrapTemplateViewModel.postScrapStore()
                    }
                }
                else -> {
                    it.message?.let { it1 -> showCustomToast(it1) }
                }
            }

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
            if (it.code == "0000") {
                it.result.tagResponses?.let { response ->
                    scrapTemplateViewModel.lastTags.setValue(
                        response
                    )
                }
            }
        }
        scrapTemplateViewModel.scrapStoreResponse.observe(this) {

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