package com.softsquared.niceduck.android.sparky.view.inquiry

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Window
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import com.softsquared.niceduck.android.sparky.databinding.ActivityInquiryBinding
import com.softsquared.niceduck.android.sparky.databinding.ActivityTagListBinding
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import com.softsquared.niceduck.android.sparky.view.sign_in.SignInActivity
import com.softsquared.niceduck.android.sparky.viewmodel.MyPageViewModel
import kotlinx.coroutines.launch

class InquiryActivity : BaseActivity<ActivityInquiryBinding>(ActivityInquiryBinding::inflate) {
    lateinit var loadingDlg: Dialog
    private val myPageViewModel: MyPageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadingDlg = Dialog(this)
        loadingDlg.setCancelable(false)
        loadingDlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loadingDlg.setContentView(R.layout.dialog_lottie_loading)
        loadingDlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.inquiryImgBack.setOnClickListener {
            finish()
        }

        myPageViewModel.reissueAccessTokenResponse.observe(this) { response ->
            loadingDlg.dismiss()
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

        myPageViewModel.reissueAccessTokenFailure.observe(this) {
            val editor = ApplicationClass.sSharedPreferences.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this, SignInActivity::class.java)
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        myPageViewModel.inquiryFailure.observe(this) {
            when (it.code) {
                "U000" -> {
                    lifecycleScope.launch {
                        myPageViewModel.postReissueAccessToken()
                        myPageViewModel.postInquiry()
                    }
                } else -> {
                it.message?.let { it1 -> showCustomToast(it1) }
                loadingDlg.dismiss()
            }
            }
        }


        myPageViewModel.inquiryResponse.observe(this) { response ->
            loadingDlg.dismiss()
            when (response.code) {
                "0000" -> {
                    showCustomToast(response.message.toString())
                    finish()
                }
            }
        }

        binding.inquiryBtn.setOnClickListener {
            myPageViewModel.email = binding.inquiryEditTxtEmail.text.toString()
            myPageViewModel.contents = binding.inquiryEditTxtText.text.toString()
            myPageViewModel.title = binding.inquiryEditTxtTitle.text.toString()
            myPageViewModel.postInquiry()
            loadingDlg.show()
        }


        binding.inquiryEditTxtText.setOnEditorActionListener { textView, i, keyEvent ->
            if (i== EditorInfo.IME_ACTION_DONE){ // 뷰의 id를 식별, 키보드의 완료 키 입력 검출
                binding.inquiryEditTxtText.clearFocus()
            }
            return@setOnEditorActionListener false
        }

        binding.inquiryEditTxtEmail.addTextChangedListener(object : TextWatcher {


            // 입력하기 전에
            override fun beforeTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            // 입력되는 텍스트에 변화가 있을 때
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                isChecked()
                if (binding.inquiryEditTxtEmail.text.isNotEmpty()) {
                    binding.inquiryEditTxtEmail.setBackgroundResource(R.drawable.sign_input_focused)
                } else {
                    binding.inquiryEditTxtEmail.setBackgroundResource(R.drawable.sign_input_selector)
                }
            }

            // 입력이 끝났을 때
            override fun afterTextChanged(editable: Editable) {
            }
        })

        binding.inquiryEditTxtTitle.addTextChangedListener(object : TextWatcher {
            // 입력하기 전에
            override fun beforeTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            // 입력되는 텍스트에 변화가 있을 때
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                isChecked()
                if (binding.inquiryEditTxtTitle.text.isNotEmpty()) {
                    binding.inquiryEditTxtTitle.setBackgroundResource(R.drawable.sign_input_focused)
                } else {
                    binding.inquiryEditTxtTitle.setBackgroundResource(R.drawable.sign_input_selector)
                }
            }

            // 입력이 끝났을 때
            override fun afterTextChanged(editable: Editable) {
            }
        })


        binding.inquiryEditTxtText.addTextChangedListener(object : TextWatcher {
            // 입력하기 전에
            override fun beforeTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            // 입력되는 텍스트에 변화가 있을 때
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                isChecked()
                if (binding.inquiryEditTxtText.text.isNotEmpty()) {

                    binding.inquiryEditTxtText.setBackgroundResource(R.drawable.sign_input_focused)
                } else {
                    binding.inquiryEditTxtText.setBackgroundResource(R.drawable.sign_input_selector)
                }
            }

            // 입력이 끝났을 때
            override fun afterTextChanged(editable: Editable) {
            }
        })
    }

    override fun onStop() {
        super.onStop()
        loadingDlg.dismiss()
    }

    private fun isChecked() {
        if (binding.inquiryEditTxtText.text.isNotEmpty() &&
            binding.inquiryEditTxtEmail.text.isNotEmpty() &&
            binding.inquiryEditTxtTitle.text.isNotEmpty()) {
            binding.inquiryBtn.setBackgroundResource(R.drawable.button)
            binding.inquiryBtn.isEnabled = true
        } else {
            binding.inquiryBtn.isEnabled = false
            binding.inquiryBtn.setBackgroundResource(R.drawable.button2)
        }
    }

}