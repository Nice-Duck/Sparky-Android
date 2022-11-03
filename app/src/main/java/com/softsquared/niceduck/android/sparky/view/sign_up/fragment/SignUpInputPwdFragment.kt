package com.softsquared.niceduck.android.sparky.view.sign_up.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.FragmentSignUpInputPwdBinding
import com.softsquared.niceduck.android.sparky.utill.BaseFragment
import com.softsquared.niceduck.android.sparky.viewmodel.SignUpViewModel
import java.util.regex.Pattern

class SignUpInputPwdFragment :
    BaseFragment<FragmentSignUpInputPwdBinding>(FragmentSignUpInputPwdBinding::bind, R.layout.fragment_sign_up_input_pwd) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signUpViewModel: SignUpViewModel by activityViewModels()

        signUpViewModel.progress.value = 70

        val pwdValidation = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{8,16}$" // 영문, 숫자 8 ~ 16자 패턴

        binding.signUpInputPwdEditTxtPwd.addTextChangedListener(object : TextWatcher {
            // 입력하기 전에
            override fun beforeTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            // 입력되는 텍스트에 변화가 있을 때
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (binding.signUpInputPwdEditTxtPwd.text.isNotEmpty()) {
                    val p = Pattern.matches(pwdValidation, binding.signUpInputPwdEditTxtPwd.text.toString())
                    if (p) {
                        binding.signUpInputPwdEditTxtPwd.setBackgroundResource(R.drawable.sign_input_selector)
                        binding.signUpInputPwdTxtValidation.visibility = View.GONE
                        binding.signUpInputPwdBtnNext.isEnabled = true
                        binding.signUpInputPwdBtnNext.setBackgroundResource(R.drawable.button)
                    } else {
                        binding.signUpInputPwdEditTxtPwd.setBackgroundResource(R.drawable.sign_input_validation)
                        binding.signUpInputPwdTxtValidation.visibility = View.VISIBLE
                        binding.signUpInputPwdBtnNext.isEnabled = false
                        binding.signUpInputPwdBtnNext.setBackgroundResource(R.drawable.button2)
                    }
                } else {
                    binding.signUpInputPwdEditTxtPwd.setBackgroundResource(R.drawable.sign_input_selector)
                    binding.signUpInputPwdTxtValidation.visibility = View.GONE
                    binding.signUpInputPwdBtnNext.isEnabled = false
                    binding.signUpInputPwdBtnNext.setBackgroundResource(R.drawable.button2)
                }
            }

            // 입력이 끝났을 때
            override fun afterTextChanged(editable: Editable) {
            }
        })

        binding.signUpInputPwdBtnNext.setOnClickListener {
            signUpViewModel.pwd = binding.signUpInputPwdEditTxtPwd.text.toString()

            val action =
                SignUpInputPwdFragmentDirections
                    .actionSignUpInputPwdFragmentToSignUpInputNameFragment()
            view.findNavController().navigate(action)
        }
    }
}
