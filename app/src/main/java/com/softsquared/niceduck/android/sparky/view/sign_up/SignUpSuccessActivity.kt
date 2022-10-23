package com.softsquared.niceduck.android.sparky.view.sign_up

import android.content.Intent
import android.os.Bundle
import com.softsquared.niceduck.android.sparky.databinding.ActivitySignUpSuccessBinding
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import com.softsquared.niceduck.android.sparky.view.main.MainActivity

class SignUpSuccessActivity : BaseActivity<ActivitySignUpSuccessBinding>(ActivitySignUpSuccessBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.signUpSuccessBtnSignIn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP // 액티비티 스택제거
            startActivity(intent)
            finish()
        }
    }
}
