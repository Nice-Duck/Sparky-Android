package com.softsquared.niceduck.android.sparky.view.scrap

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Log.d
import android.view.View.VISIBLE
import android.view.Window
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import com.softsquared.niceduck.android.sparky.config.ApplicationClass.Companion.X_ACCESS_TOKEN
import com.softsquared.niceduck.android.sparky.config.ApplicationClass.Companion.sSharedPreferences
import com.softsquared.niceduck.android.sparky.databinding.ActivityScrapTemplateBinding
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import com.softsquared.niceduck.android.sparky.view.sign_in.SignInActivity
import com.softsquared.niceduck.android.sparky.viewmodel.ScrapTemplateViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ScrapTemplateActivity : BaseActivity<ActivityScrapTemplateBinding>(ActivityScrapTemplateBinding::inflate) {
    private val scrapTemplateViewModel: ScrapTemplateViewModel by viewModels()
    private lateinit var dlg: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dlg = Dialog(this)
        dlg.setCancelable(false)
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setContentView(R.layout.dialog_lottie_loading)
        dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (sSharedPreferences.getString(X_ACCESS_TOKEN, null) == null) {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        scrapTemplateViewModel.setDataViewCallFailure.observe(this) {
            d("테스트", it)
            showCustomToast("스크랩 정보를 가져올 수 없습니다")
            finish()
        }

        binding.scrapTemplateBtnStore.setOnClickListener {
            scrapTemplateViewModel.postScrapStore()
            dlg.show()
        }

        scrapTemplateViewModel.setDataViewCall.observe(this) {
            val url = it.first
            val ogMap = it.second

            lifecycleScope.launch {
                delay(1000)
                dlg.dismiss()
            }

            scrapTemplateViewModel.url = url

            if (ogMap["image"].isNullOrEmpty()) {
                scrapTemplateViewModel.img.setValue(intent.getStringExtra("ogImage") ?: "")
            } else {
                ogMap["image"]?.let { scrapTemplateViewModel.img.setValue(it) }
            }
            if (ogMap["title"].isNullOrEmpty()) {
                scrapTemplateViewModel.title.setValue(intent.getStringExtra("title") ?: "")
            } else {
                ogMap["title"]?.let { scrapTemplateViewModel.title.setValue(it) }
            }
            if (ogMap["description"].isNullOrEmpty()) {
                scrapTemplateViewModel.subTitle.setValue(intent.getStringExtra("ogDescription") ?: "")
            } else {
                ogMap["description"]?.let { scrapTemplateViewModel.subTitle.setValue(it) }
            }
        }

        getScrapData()
        dlg.show()

        setScrapTemplateRecyclerview()

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
                it.result.tagResponses?.let { response -> scrapTemplateViewModel.lastTags.setValue(response) }
            }
        }

        scrapTemplateViewModel.tagLastLoadFailure.observe(this) {

            when (it.code) {
                "U000" -> {
                    lifecycleScope.launch {
                        scrapTemplateViewModel.postReissueAccessToken()
                        scrapTemplateViewModel.getTagLastLoad()
                    }
                } else -> {
                    it.message?.let { it1 -> showCustomToast(it1) }
                }
            }
        }

        scrapTemplateViewModel.scrapStoreResponse.observe(this) {
            lifecycleScope.launch  {
                delay(1000)
                dlg.dismiss()
            }
            if (it.code == "0000") {
                finish()
            }
        }

        scrapTemplateViewModel.scrapStoreFailure.observe(this) {
            lifecycleScope.launch  {
                delay(1000)
                dlg.dismiss()
            }

            when (it.code) {
                "U000" -> {
                    lifecycleScope.launch  {
                        scrapTemplateViewModel.postReissueAccessToken()
                        scrapTemplateViewModel.postScrapStore()
                    }
                }
                else -> {
                    it.message?.let { it1 -> showCustomToast(it1) }
                }
            }
        }



        scrapTemplateViewModel.showBottomSheetCall.observe(this) {
            scrapTemplateViewModel.tagColor.setValue(scrapTemplateViewModel.randomColor())
            val bottomDialogFragment = ScrapBottomDialogFragment()
            bottomDialogFragment.show(supportFragmentManager, bottomDialogFragment.tag)
        }

        d(
            "test_extras",
            "" +
                "${intent}\n ${intent}\n" +
                "${intent.getStringExtra(Intent.EXTRA_TEXT)}\n" +
                "${intent.extras}\n " +
                "${intent.type}\n " +
                "${intent.component}\n" +
                "${intent.sourceBounds}"
        )

        intent.getStringExtra(Intent.EXTRA_TEXT)?.let { d("test_extra_text", it) }

        binding.scrapTemplateEditTxtMemo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.scrapTemplateEditTxtMemo.run {
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

        scrapTemplateViewModel.title.observe(this) {
            binding.scrapTemplateEditTxtTitle.setText(it)
        }

        scrapTemplateViewModel.img.observe(this) {
            // Glide 옵션 fitCenter() or centerCrop()
            if (it == "") {
                Glide.with(this@ScrapTemplateActivity).load(R.drawable.scrap_default_img3).transform(
                    CenterCrop(), RoundedCorners(8)
                ).into(binding.scrapTemplateImgThumbnail)
            } else {
                d("test_img", it)
                Glide.with(this@ScrapTemplateActivity).load(it).transform(
                    CenterCrop(), RoundedCorners(8)
                ).into(binding.scrapTemplateImgThumbnail)
            }
        }

        scrapTemplateViewModel.subTitle.observe(this) {
            binding.scrapTemplateEditTxtSummary.setText(it)
        }

        // 토큰 갱신
        scrapTemplateViewModel.reissueAccessTokenResponse.observe(this) { response ->
            when (response.code) {
                "0000" -> {
                    val newToken = response.result?.accessToken
                    val editor = sSharedPreferences.edit()
                    editor.putString(ApplicationClass.X_ACCESS_TOKEN, newToken)
                    editor.apply()
                }
                else -> {
                    showCustomToast("네트워크 연결이 원활하지 않습니다.")
                }
            }

        }

        scrapTemplateViewModel.reissueAccessTokenFailure.observe(this) { response ->
            val editor = sSharedPreferences.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this, SignInActivity::class.java)
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }



    private fun getScrapData() {
        lifecycleScope.launch(Dispatchers.IO) {
            if (intent?.action == Intent.ACTION_SEND) {
                if ("text/plain" == intent.type) {
                    scrapTemplateViewModel.getScrapData(intent.getStringExtra(Intent.EXTRA_TEXT))
                }
            } else {
                scrapTemplateViewModel.getScrapData(intent.getStringExtra("add"))
            }
        }
    }

    private fun setScrapTemplateRecyclerview() {
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        with(binding.scrapTemplateRecyclerview) {
            this.layoutManager = layoutManager
            adapter = scrapTemplateViewModel.scrapTemplateRecyclerviewAdapter
            visibility = VISIBLE
        }
    }

    override fun onStop() {
        super.onStop()
        dlg.dismiss()
    }


}
