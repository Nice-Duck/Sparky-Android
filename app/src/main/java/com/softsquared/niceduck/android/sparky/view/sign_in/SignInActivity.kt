package com.softsquared.niceduck.android.sparky.view.sign_in

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.softsquared.niceduck.android.sparky.databinding.ActivitySignInBinding
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import com.softsquared.niceduck.android.sparky.view.sign_up.SignUpActivity
import com.softsquared.niceduck.android.sparky.viewmodel.SignInViewModel
import kotlin.math.sign

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

            }

            // 입력이 끝났을 때
            override fun afterTextChanged(editable: Editable) {

            }
        })

        signInViewModel.signInResponse.observe(this, Observer {

        })

        signInViewModel.signInFailure.observe(this, Observer {

        })

        binding.signInBtnSignIn.setOnClickListener {
            signInViewModel.email = binding.signInEditTxtEmail.text.toString()
            signInViewModel.pwd = binding.signInEditTxtPwd.text.toString()

            signInViewModel.postSignIn()

        }
    }

}