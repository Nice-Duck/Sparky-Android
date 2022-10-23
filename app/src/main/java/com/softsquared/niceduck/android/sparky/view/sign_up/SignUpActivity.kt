package com.softsquared.niceduck.android.sparky.view.sign_up

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.softsquared.niceduck.android.sparky.databinding.ActivitySignUpBinding
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import com.softsquared.niceduck.android.sparky.viewmodel.SignUpViewModel

class SignUpActivity : BaseActivity<ActivitySignUpBinding>(ActivitySignUpBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val signUpViewModel: SignUpViewModel by viewModels()

        signUpViewModel.progress.observe(
            this,
            Observer {
                binding.signUpLinearProgressIndicator.progress = it
            }
        )
    }
}
