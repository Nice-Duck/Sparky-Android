package com.softsquared.niceduck.android.sparky.view.sign_up.fragment

import android.os.Bundle
import android.util.Log.d
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.FragmentSignUpInputEmailBinding
import com.softsquared.niceduck.android.sparky.utill.BaseFragment
import com.softsquared.niceduck.android.sparky.viewmodel.SignUpViewModel

class SignUpInputEmailFragment :
    BaseFragment<FragmentSignUpInputEmailBinding>(FragmentSignUpInputEmailBinding::bind, R.layout.fragment_sign_up_input_email) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signUpViewModel: SignUpViewModel by activityViewModels()

        signUpViewModel.setProgress(0)

        binding.signUpInputEmailBtnNext.setOnClickListener {
            val action =
                SignUpInputEmailFragmentDirections
                    .actionSignUpInputEmailFragmentToSignUpInputCertificationNumFragment()
            view.findNavController().navigate(action)

        }
    }
}