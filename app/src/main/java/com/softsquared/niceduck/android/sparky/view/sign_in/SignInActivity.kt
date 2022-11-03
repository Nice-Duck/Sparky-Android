package com.softsquared.niceduck.android.sparky.view.sign_in

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.config.ApplicationClass.Companion.X_ACCESS_TOKEN
import com.softsquared.niceduck.android.sparky.config.ApplicationClass.Companion.X_REFRESH_TOKEN
import com.softsquared.niceduck.android.sparky.config.ApplicationClass.Companion.sSharedPreferences
import com.softsquared.niceduck.android.sparky.databinding.ActivitySignInBinding
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import com.softsquared.niceduck.android.sparky.view.main.MainActivity
import com.softsquared.niceduck.android.sparky.view.sign_up.SignUpActivity
import com.softsquared.niceduck.android.sparky.viewmodel.SignInViewModel

class SignInActivity : BaseActivity<ActivitySignInBinding>(ActivitySignInBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val signInViewModel: SignInViewModel by viewModels()

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
            if (it.code == "0000") {
                val editor = sSharedPreferences.edit()
                editor.putString(X_ACCESS_TOKEN, it.result?.accessToken)
                editor.putString(X_REFRESH_TOKEN, it.result?.refreshToken)
                editor.apply()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        signInViewModel.signInFailure.observe(this) {
            // TODO: 로그인 실패 처리 코드
        }

        binding.signInBtnSignIn.setOnClickListener {
            signInViewModel.email = binding.signInEditTxtEmail.text.toString()
            signInViewModel.pwd = binding.signInEditTxtPwd.text.toString()

            signInViewModel.postSignIn()
        }
    }
}
