package com.softsquared.niceduck.android.sparky.view.tag_list

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import com.softsquared.niceduck.android.sparky.databinding.ActivityTagListBinding
import com.softsquared.niceduck.android.sparky.model.TagsResponse
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import com.softsquared.niceduck.android.sparky.view.scrap.ItemEvent
import com.softsquared.niceduck.android.sparky.view.sign_in.SignInActivity
import com.softsquared.niceduck.android.sparky.viewmodel.MyPageViewModel
import kotlinx.coroutines.launch
import java.util.*

class TagListActivity : BaseActivity<ActivityTagListBinding>(ActivityTagListBinding::inflate),
    ItemEvent {
    lateinit var loadingDlg: Dialog
    var tagListAdapter: TagListRecyclerviewAdapter? = null
    var tagList: ArrayList<TagsResponse>? = null
    private val myPageViewModel: MyPageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadingDlg = Dialog(this)
        loadingDlg.setCancelable(false)
        loadingDlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loadingDlg.setContentView(R.layout.dialog_lottie_loading)
        loadingDlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        myPageViewModel.getTagLastLoad()


        myPageViewModel.tagLastLoadResponse.observe(this) {
            if (it.code == "0000") {
                it.result.tagResponses?.let { it1 -> setRecyclerViewAdapter(it1) }
            }
        }

        myPageViewModel.tagLastLoadFailure.observe(this) {
            when (it.code) {
                "U000" -> {
                    lifecycleScope.launch {
                        myPageViewModel.postReissueAccessToken()
                        myPageViewModel.getTagLastLoad()
                    }
                } else -> {
                it.message?.let { it1 -> showCustomToast(it1) }
            }
            }

        }

        myPageViewModel.tagPatchResponse.observe(this) {
            if (it.code == "0000") {

            }
        }

        myPageViewModel.tagPatchFailure.observe(this) {
            when (it.code) {
                "U000" -> {
                    lifecycleScope.launch {
                        myPageViewModel.postReissueAccessToken()
                        //myPageViewModel.patchTag()
                    }
                } else -> {
                it.message?.let { it1 -> showCustomToast(it1) }
            }
            }

        }


        myPageViewModel.tagDeleteResponse.observe(this) {
            if (it.code == "0000") {

            }
        }

        myPageViewModel.tagDeleteFailure.observe(this) {
            when (it.code) {
                "U000" -> {
                    lifecycleScope.launch {
                        myPageViewModel.postReissueAccessToken()
                        //myPageViewModel.deleteTag()
                    }
                } else -> {
                it.message?.let { it1 -> showCustomToast(it1) }
            }
            }

        }

        myPageViewModel.reissueAccessTokenResponse.observe(this) { response ->
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

        myPageViewModel.reissueAccessTokenFailure.observe(this) { response ->
            val editor = ApplicationClass.sSharedPreferences.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this, SignInActivity::class.java)
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun setScrapTemplateRecyclerview(mAdapter: TagListRecyclerviewAdapter) {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        with(binding.tagListRecyclerview) {
            this.layoutManager = layoutManager
            adapter = mAdapter
            visibility = android.view.View.VISIBLE
        }
    }

    private fun setRecyclerViewAdapter(tags: ArrayList<TagsResponse>) {
        tagList = tags
        tagListAdapter = TagListRecyclerviewAdapter(this)
        tagListAdapter!!.submitList(tags)
        setScrapTemplateRecyclerview(tagListAdapter!!)
    }

    override fun removeItem(position: Int) {

    }

    override fun addItem() {

    }

    override fun selectItem(position: Int) {

    }
}