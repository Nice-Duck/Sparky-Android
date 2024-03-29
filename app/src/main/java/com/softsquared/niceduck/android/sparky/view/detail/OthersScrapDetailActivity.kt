package com.softsquared.niceduck.android.sparky.view.detail

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import com.softsquared.niceduck.android.sparky.databinding.ActivityMyScrapDetailBinding
import com.softsquared.niceduck.android.sparky.databinding.ActivityOthersScrapDetailBinding
import com.softsquared.niceduck.android.sparky.model.Scrap
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import com.softsquared.niceduck.android.sparky.view.main.fragment.TagRecyclerviewAdapter
import com.softsquared.niceduck.android.sparky.view.main.fragment.TagRecyclerviewAdapter2
import com.softsquared.niceduck.android.sparky.view.scrap.ScrapTemplateActivity
import com.softsquared.niceduck.android.sparky.view.sign_in.SignInActivity
import com.softsquared.niceduck.android.sparky.viewmodel.ScrapDetailViewModel
import kotlinx.coroutines.launch

class OthersScrapDetailActivity: BaseActivity<ActivityOthersScrapDetailBinding>(
    ActivityOthersScrapDetailBinding::inflate) {
    val scrapDetailViewModel: ScrapDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding.othersScrapDetailImgBack.setOnClickListener {
            finish()
        }


        scrapDetailViewModel.reissueAccessTokenResponse.observe(this) { response ->
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

        scrapDetailViewModel.reissueAccessTokenFailure.observe(this) {
            val editor = ApplicationClass.sSharedPreferences.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this, SignInActivity::class.java)
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()

        }


        val scrap: Scrap? = intent.getParcelableExtra("scrap")
        if (scrap != null) {

            scrapDetailViewModel.declarationFailure.observe(this) {
                when (it.code) {
                    "U000" -> {
                        lifecycleScope.launch {
                            scrapDetailViewModel.postReissueAccessToken()
                            scrapDetailViewModel.getDeclaration(scrap.scrapId.toString())
                        }
                    } else -> {
                    it.message?.let { it1 -> showCustomToast(it1) }
                }
                }
            }

            scrapDetailViewModel.declarationResponse.observe(this) { response ->
                when (response.code) {
                    "0000" -> {
                        showCustomToast(response.message.toString())
                    }

                }
            }

            binding.othersScrapDetailTxtDeclaration.setOnClickListener {
                val dlg = Dialog(this)
                dlg.setCancelable(false)
                dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dlg.setContentView(R.layout.dialog_two_btn_choice)
                dlg.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dlg.setOnDismissListener { }
                val dlgTextView = dlg.findViewById<TextView>(R.id.dialog_two_btn_choice_txt_content)
                dlgTextView.text = "신고 하시겠습니까?"
                val cancel = dlg.findViewById<View>(R.id.dialog_two_btn_choice_txt_left) as TextView
                val ok = dlg.findViewById<View>(R.id.dialog_two_btn_choice_txt_right) as TextView
                ok.text = "신고하기"
                ok.setOnClickListener {
                    dlg.dismiss()
                    scrapDetailViewModel.getDeclaration(scrap.scrapId.toString())
                }

                cancel.setOnClickListener { dlg.dismiss() }
                dlg.show()
            }

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