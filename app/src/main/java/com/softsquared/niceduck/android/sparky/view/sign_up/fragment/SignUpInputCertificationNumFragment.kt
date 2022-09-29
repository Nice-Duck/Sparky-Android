package com.softsquared.niceduck.android.sparky.view.sign_up.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.FragmentSignUpInputCertificationNumBinding
import com.softsquared.niceduck.android.sparky.databinding.FragmentSignUpInputEmailBinding
import com.softsquared.niceduck.android.sparky.utill.BaseFragment
import com.softsquared.niceduck.android.sparky.viewmodel.SignUpViewModel

class SignUpInputCertificationNumFragment :
    BaseFragment<FragmentSignUpInputCertificationNumBinding>(FragmentSignUpInputCertificationNumBinding::bind, R.layout.fragment_sign_up_input_certification_num) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signUpViewModel: SignUpViewModel by activityViewModels()

        signUpViewModel.setProgress(50)

        binding.signUpInputCertificationNumBtnNext.setOnClickListener {
            val action =
                SignUpInputCertificationNumFragmentDirections
                    .actionSignUpInputCertificationNumFragmentToSignUpInputPwdFragment()
            view.findNavController().navigate(action)

        }

    }
}