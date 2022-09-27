package com.softsquared.niceduck.android.sparky.view.sign_up.fragment

import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.FragmentSignUpInputEmailBinding
import com.softsquared.niceduck.android.sparky.databinding.FragmentSignUpInputPwdBinding
import com.softsquared.niceduck.android.sparky.utill.BaseFragment
import com.softsquared.niceduck.android.sparky.viewmodel.SignUpViewModel

class SignUpInputPwdFragment :
    BaseFragment<FragmentSignUpInputPwdBinding>(FragmentSignUpInputPwdBinding::bind, R.layout.fragment_sign_up_input_pwd) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signUpViewModel: SignUpViewModel by activityViewModels()

        signUpViewModel.setProgress(50)
        d("네비게이션 테스트", "signUpViewModel.setProgress(50) , ${signUpViewModel.progress.value}")

        binding.signUpInputPwdBtnNext.setOnClickListener {
            val action =
                SignUpInputPwdFragmentDirections.
                actionSignUpInputPwdFragmentToSignUpInputNameFragment()
            view.findNavController().navigate(action)

        }
    }
}