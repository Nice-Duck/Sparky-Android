package com.softsquared.niceduck.android.sparky.view.sign_up.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.FragmentSignUpInputNameBinding
import com.softsquared.niceduck.android.sparky.utill.BaseFragment
import com.softsquared.niceduck.android.sparky.view.sign_up.SignUpSuccessActivity
import com.softsquared.niceduck.android.sparky.viewmodel.SignUpViewModel

class SignUpInputNameFragment :
    BaseFragment<FragmentSignUpInputNameBinding>(FragmentSignUpInputNameBinding::bind, R.layout.fragment_sign_up_input_name) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signUpViewModel: SignUpViewModel by activityViewModels()

        signUpViewModel.setProgress(100)

        binding.signUpInputNameBtnNext.setOnClickListener {
            val intent = Intent(activity, SignUpSuccessActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }
}