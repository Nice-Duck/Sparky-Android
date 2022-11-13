package com.softsquared.niceduck.android.sparky.view.my_page

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import com.softsquared.niceduck.android.sparky.databinding.ActivityMyPageBinding
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import com.softsquared.niceduck.android.sparky.view.sign_in.SignInActivity
import com.softsquared.niceduck.android.sparky.viewmodel.MyPageViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyPageActivity : BaseActivity<ActivityMyPageBinding>(ActivityMyPageBinding::inflate) {
    lateinit var loadingDlg: Dialog
    private val myPageViewModel: MyPageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadingDlg = Dialog(this)
        loadingDlg.setCancelable(false)
        loadingDlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loadingDlg.setContentView(R.layout.dialog_lottie_loading)
        loadingDlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

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

        myPageViewModel.withdrawalResponse.observe(this) {
            val scope = lifecycleScope.launch {
                delay(1000)
                loadingDlg.dismiss()
            }
            if (it.code == "0000") {
                scope.cancel()
                val editor = ApplicationClass.sSharedPreferences.edit()
                editor.clear()
                editor.apply()

                val intent = Intent(this, SignInActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }
        }

        myPageViewModel.withdrawalFailure.observe(this) {
            lifecycleScope.launch  {
                delay(1000)
                loadingDlg.dismiss()
            }

            when (it.code) {
                "U000" -> {
                    lifecycleScope.launch {
                        myPageViewModel.postReissueAccessToken()
                        myPageViewModel.deleteWithdrawal()
                    }
                }
                else -> {
                    it.message?.let { it1 -> showCustomToast(it1) }
                }
            }
        }

        binding.myPageImgBack.setOnClickListener {
            finish()
        }

        binding.myPageLLWithdraw.setOnClickListener {
            val dlg = Dialog(this)
            dlg.setCancelable(false)
            dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dlg.setContentView(R.layout.dialog_two_btn_choice)
            dlg.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dlg.setOnDismissListener { }
            val dlgTextView = dlg.findViewById<TextView>(R.id.dialog_two_btn_choice_txt_content)
            dlgTextView.text = "탈퇴하시겠습니까?"
            val cancel = dlg.findViewById<View>(R.id.dialog_two_btn_choice_txt_left) as TextView
            val ok = dlg.findViewById<View>(R.id.dialog_two_btn_choice_txt_right) as TextView
            ok.text = "탈퇴하기"
            ok.setOnClickListener {
                dlg.dismiss()
                myPageViewModel.deleteWithdrawal()
                loadingDlg.show()
            }
            cancel.setOnClickListener { dlg.dismiss() }

            dlg.show()
        }

        binding.myPageLLLogout.setOnClickListener {
            val dlg = Dialog(this)
            dlg.setCancelable(false)
            dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dlg.setContentView(R.layout.dialog_two_btn_choice)
            dlg.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val dlgTextView = dlg.findViewById<TextView>(R.id.dialog_two_btn_choice_txt_content)
            dlgTextView.text = "로그아웃하시겠습니까?"
            dlg.setOnDismissListener { }

            val cancel = dlg.findViewById<View>(R.id.dialog_two_btn_choice_txt_left) as TextView
            val ok = dlg.findViewById<View>(R.id.dialog_two_btn_choice_txt_right) as TextView
            ok.text = "로그아웃"
            ok.setOnClickListener {
                dlg.dismiss()

                val editor = ApplicationClass.sSharedPreferences.edit()
                editor.clear()
                editor.apply()

                val intent = Intent(this, SignInActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }
            cancel.setOnClickListener { dlg.dismiss() }

            dlg.show()
        }


    }


    override fun onStop() {
        super.onStop()
        loadingDlg.dismiss()
    }
}