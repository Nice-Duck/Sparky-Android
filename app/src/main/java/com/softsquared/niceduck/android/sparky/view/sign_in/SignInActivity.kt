package com.softsquared.niceduck.android.sparky.view.sign_in

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.config.ApplicationClass.Companion.X_ACCESS_TOKEN
import com.softsquared.niceduck.android.sparky.config.ApplicationClass.Companion.X_REFRESH_TOKEN
import com.softsquared.niceduck.android.sparky.config.ApplicationClass.Companion.sSharedPreferences
import com.softsquared.niceduck.android.sparky.databinding.ActivitySignInBinding
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import com.softsquared.niceduck.android.sparky.view.main.MainActivity
import com.softsquared.niceduck.android.sparky.view.sign_up.SignUpActivity
import com.softsquared.niceduck.android.sparky.viewmodel.SignInViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SignInActivity : BaseActivity<ActivitySignInBinding>(ActivitySignInBinding::inflate) {
    private lateinit var dlg: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val signInViewModel: SignInViewModel by viewModels()

        dlg = Dialog(this)
        dlg.setCancelable(false)
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setContentView(R.layout.dialog_lottie_loading)
        dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        binding.signInEditTxtPwd.setOnEditorActionListener { textView, i, keyEvent ->
            if (i==EditorInfo.IME_ACTION_DONE){ // 뷰의 id를 식별, 키보드의 완료 키 입력 검출
                binding.signInEditTxtPwd.clearFocus()
            }
            return@setOnEditorActionListener false
        }

        binding.signInLL.setOnClickListener {
            hideKeyboard()
            it.clearFocus()
        }

        binding.signInTxtSignUpBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.signInEditTxtEmail.addTextChangedListener(object : TextWatcher {
            // 입력하기 전에
            override fun beforeTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            // 입력되는 텍스트에 변화가 있을 때
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (binding.signInEditTxtEmail.text.isNotEmpty()) {
                    binding.signInEditTxtEmail.setBackgroundResource(R.drawable.sign_input_focused)
                } else {
                    binding.signInEditTxtEmail.setBackgroundResource(R.drawable.sign_input_selector)
                }
            }

            // 입력이 끝났을 때
            override fun afterTextChanged(editable: Editable) {
            }
        })

        binding.signInEditTxtPwd.addTextChangedListener(object : TextWatcher {
            // 입력하기 전에
            override fun beforeTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            // 입력되는 텍스트에 변화가 있을 때
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (binding.signInEditTxtPwd.text.isNotEmpty()) {
                    binding.signInEditTxtPwd.setBackgroundResource(R.drawable.sign_input_focused)
                } else {
                    binding.signInEditTxtPwd.setBackgroundResource(R.drawable.sign_input_selector)
                }
            }

            // 입력이 끝났을 때
            override fun afterTextChanged(editable: Editable) {
            }
        })

        signInViewModel.signInResponse.observe(this) {
            val scope = lifecycleScope.launch  {
                delay(1000)
                dlg.dismiss()
            }
            if (it.code == "0000") {
                scope.cancel()

                val editor = sSharedPreferences.edit()
                editor.putString(X_ACCESS_TOKEN, it.result?.accessToken)
                editor.putString(X_REFRESH_TOKEN, it.result?.refreshToken)
                editor.apply()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        signInViewModel.signInFailure.observe(this) { errorResponse ->
            lifecycleScope.launch {
                delay(1000)
                dlg.dismiss()
                errorResponse.message?.let { showCustomToast(it) }
            }
        }

        binding.signInBtnSignIn.setOnClickListener {
            signInViewModel.email = binding.signInEditTxtEmail.text.toString()
            signInViewModel.pwd = binding.signInEditTxtPwd.text.toString()

            dlg.show()

            signInViewModel.postSignIn()
        }
    }

    override fun onStop() {
        super.onStop()
        dlg.dismiss()
    }
}
