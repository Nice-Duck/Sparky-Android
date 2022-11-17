package com.softsquared.niceduck.android.sparky.view.tag_list

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import com.softsquared.niceduck.android.sparky.databinding.ActivityTagListBinding
import com.softsquared.niceduck.android.sparky.model.TagRequest2
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
        loadingDlg.show()


        myPageViewModel.tagLastLoadResponse.observe(this) {
            if (it.code == "0000") {
                it.result.tagResponses?.let { it1 -> setRecyclerViewAdapter(it1) }

            }
            loadingDlg.dismiss()
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
                loadingDlg.dismiss()
            }
            }

        }

        myPageViewModel.tagPatchResponse.observe(this) {
            if (it.code == "0000") {
                myPageViewModel.patchPosition?.let { position ->
                    tagList?.set(position, myPageViewModel.patchTag!!)
                    val updateList = mutableListOf<TagsResponse>()
                    tagList?.let { it1 ->
                        updateList.addAll(it1)
                        tagListAdapter?.submitList(updateList.toMutableList())
                    }
                    myPageViewModel.getTagLastLoad()
                    myPageViewModel.patchTag = null
                    myPageViewModel.patchPosition = null
                }
            }
        }

        myPageViewModel.tagPatchFailure.observe(this) {
            when (it.code) {
                "U000" -> {
                    lifecycleScope.launch {
                        myPageViewModel.postReissueAccessToken()
                        myPageViewModel.patchTag()
                    }
                } else -> {
                it.message?.let { it1 -> showCustomToast(it1) }
            }
            }

        }


        myPageViewModel.tagDeleteResponse.observe(this) {
            if (it.code == "0000") {
                myPageViewModel.deletePosition?.let { position ->

                    try {
                        tagList?.removeAt(position)
                        val updateList = mutableListOf<TagsResponse>()
                        tagList?.let { it1 ->
                            updateList.addAll(it1)
                            tagListAdapter?.submitList(updateList.toMutableList())
                        }
                    } catch (e: Exception) {
                        Log.d("test", e.message.toString())
                    }
                    myPageViewModel.getTagLastLoad()
                    myPageViewModel.deleteTagId = null
                    myPageViewModel.deletePosition = null
                }
            }
        }

        myPageViewModel.tagDeleteFailure.observe(this) {
            when (it.code) {
                "U000" -> {
                    lifecycleScope.launch {
                        myPageViewModel.postReissueAccessToken()
                        myPageViewModel.deleteTag()
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
                    loadingDlg.dismiss()
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
            loadingDlg.dismiss()
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
        val dlg = Dialog(this)
        dlg.setCancelable(false)
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setContentView(R.layout.dialog_two_btn_choice)
        dlg.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dlg.setOnDismissListener { }
        val dlgTextView = dlg.findViewById<TextView>(R.id.dialog_two_btn_choice_txt_content)
        dlgTextView.text = "태그를 삭제 하시겠습니까?"
        val cancel = dlg.findViewById<View>(R.id.dialog_two_btn_choice_txt_left) as TextView
        val ok = dlg.findViewById<View>(R.id.dialog_two_btn_choice_txt_right) as TextView
        ok.text = "삭제하기"
        ok.setOnClickListener {
            dlg.dismiss()
            tagList?.get(position)?.let {
                myPageViewModel.deleteTagId = it.tagId
                myPageViewModel.deletePosition = position
                myPageViewModel.deleteTag()
            }

        }
        cancel.setOnClickListener { dlg.dismiss() }

        dlg.show()
    }

    override fun addItem() {

    }

    override fun selectItem(position: Int) {
        val dlg = Dialog(this)
        dlg.setCancelable(false)
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setContentView(R.layout.dialog_two_btn_choice2)
        dlg.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dlg.setOnDismissListener { }
        val dlgTextView = dlg.findViewById<EditText>(R.id.dialog_two_btn_choice2_txt_content)
        dlgTextView.addTextChangedListener {
            if ( dlgTextView.text.isNotEmpty()) {
                dlgTextView.setBackgroundResource(R.drawable.sign_input_focused)
            } else {
                dlgTextView.setBackgroundResource(R.drawable.sign_input_normal)
            }
        }

        tagList?.get(position)?.let {
            dlgTextView.setText(it.name)
        }


        val cancel = dlg.findViewById<View>(R.id.dialog_two_btn_choice2_txt_left) as TextView
        val ok = dlg.findViewById<View>(R.id.dialog_two_btn_choice2_txt_right) as TextView

        ok.setOnClickListener {
            dlg.dismiss()
            tagList?.get(position)?.let {
                val patch = TagsResponse(it.color, dlgTextView.text.toString(), it.tagId)
                myPageViewModel.patchTag = patch
                myPageViewModel.patchPosition = position
                myPageViewModel.patchTag()
            }


        }
        cancel.setOnClickListener { dlg.dismiss() }

        dlg.show()
    }


    override fun onStop() {
        super.onStop()
        loadingDlg.dismiss()
    }
}