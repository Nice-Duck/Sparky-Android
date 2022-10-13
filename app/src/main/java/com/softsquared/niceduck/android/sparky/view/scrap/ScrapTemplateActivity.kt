package com.softsquared.niceduck.android.sparky.view.scrap

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log.d
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.ActivityScrapTemplateBinding
import com.softsquared.niceduck.android.sparky.model.Tag
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import com.softsquared.niceduck.android.sparky.utill.BaseFragment
import com.softsquared.niceduck.android.sparky.viewmodel.ScrapTemplateViewModel
import com.softsquared.niceduck.android.sparky.viewmodel.SignUpViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ScrapTemplateActivity : BaseActivity<ActivityScrapTemplateBinding>(ActivityScrapTemplateBinding::inflate) {
    private val scrapTemplateViewModel: ScrapTemplateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        scrapTemplateViewModel.setDataViewCall.observe(this) {
            val url = it.first
            val ogMap = it.second

            scrapTemplateViewModel.url.setValue(url)

            if (ogMap["image"].isNullOrEmpty()) {
                scrapTemplateViewModel.img.setValue(intent.getStringExtra("ogImage")?:"")
            } else {
                ogMap["image"]?.let { scrapTemplateViewModel.img.setValue(it) }
            }
            if (ogMap["title"].isNullOrEmpty()) {
                scrapTemplateViewModel.title.setValue(intent.getStringExtra("title") ?:"")
            } else {
                ogMap["title"]?.let { scrapTemplateViewModel.title.setValue(it) }
            }
            if (ogMap["description"].isNullOrEmpty()) {
                scrapTemplateViewModel.memo.setValue(intent.getStringExtra("ogDescription") ?:"")
            } else {
                ogMap["description"]?.let { scrapTemplateViewModel.memo.setValue(it) }
            }
        }

        getScrapData()

        setScrapTemplateRecyclerview()

        scrapTemplateViewModel.getScrapTemplateData().observe(this, Observer {
            scrapTemplateViewModel.getScrapTemplateAdapter().submitList(it)
        })

        scrapTemplateViewModel.showBottomSheetCall.observe(this) {
            val bottomDialogFragment = ScrapBottomDialogFragment()
            bottomDialogFragment.show(supportFragmentManager, bottomDialogFragment.tag)
        }

        d("test_extras", "" +
                "${intent}\n ${intent}\n" +
                "${intent.getStringExtra(Intent.EXTRA_TEXT )}\n" +
                "${intent.extras}\n " +
                "${intent.type}\n " +
                "${intent.component}\n" +
                "${intent.sourceBounds}")

        intent.getStringExtra(Intent.EXTRA_TEXT)?.let { d("test_extra_text", it) }


        binding.scrapTemplateEditTxtMemo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.scrapTemplateEditTxtMemo.run {
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


        scrapTemplateViewModel.url.observe(this) {
        }

        scrapTemplateViewModel.title.observe(this) {
            binding.scrapTemplateEditTxtTitle.setText(it)
        }

        scrapTemplateViewModel.img.observe(this) {
            // Glide 옵션 fitCenter() or centerCrop()
            if (it == "") {
                Glide.with(this@ScrapTemplateActivity).load(getDrawable(R.drawable.sparky)).centerCrop().into(binding.scrapTemplateImgThumbnail)
            } else {
                Glide.with(this@ScrapTemplateActivity).load(it).centerCrop().into(binding.scrapTemplateImgThumbnail)
            }
        }

        scrapTemplateViewModel.memo.observe(this) {
            binding.scrapTemplateEditTxtMemo.setText(it)
            binding.scrapTemplateEditTxtSummary.setText(it)
        }

    }

    private fun getScrapData() {
        CoroutineScope(Dispatchers.IO).launch {
            if (intent?.action == Intent.ACTION_SEND) {
                if ("text/plain" == intent.type) {
                    scrapTemplateViewModel.getScrapData(intent.getStringExtra(Intent.EXTRA_TEXT))
                }
            }
        }
    }

    private fun setScrapTemplateRecyclerview() {
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        with(binding.scrapTemplateRecyclerview) {
            this.layoutManager = layoutManager
            adapter = scrapTemplateViewModel.getScrapTemplateAdapter()
            visibility = VISIBLE
        }
    }

}