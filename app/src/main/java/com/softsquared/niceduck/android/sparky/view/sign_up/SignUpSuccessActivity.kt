package com.softsquared.niceduck.android.sparky.view.sign_up

import android.content.Intent
import android.os.Bundle
import com.softsquared.niceduck.android.sparky.databinding.ActivitySignUpSuccessBinding
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import com.softsquared.niceduck.android.sparky.view.sign_in.SignInActivity

class SignUpSuccessActivity : BaseActivity<ActivitySignUpSuccessBinding>(ActivitySignUpSuccessBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.signUpSuccessBtnSignIn.setOnClickListener {
            finish()
        }
    }
}