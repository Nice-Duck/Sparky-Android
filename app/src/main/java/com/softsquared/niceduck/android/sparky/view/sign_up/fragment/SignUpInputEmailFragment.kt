package com.softsquared.niceduck.android.sparky.view.sign_up.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.FragmentSignUpInputEmailBinding
import com.softsquared.niceduck.android.sparky.utill.BaseFragment
import com.softsquared.niceduck.android.sparky.viewmodel.SignUpViewModel
import java.util.regex.Pattern


class SignUpInputEmailFragment :
    BaseFragment<FragmentSignUpInputEmailBinding>(FragmentSignUpInputEmailBinding::bind, R.layout.fragment_sign_up_input_email) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signUpViewModel: SignUpViewModel by activityViewModels()

        signUpViewModel.progress.value = 0


        binding.signUpInputEmailEditTxtEmail.addTextChangedListener(object : TextWatcher {
            // 이메일 정규식
            val emailValidation = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

            // 입력하기 전에
            override fun beforeTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            // 입력되는 텍스트에 변화가 있을 때
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (binding.signUpInputEmailEditTxtEmail.text.toString().isNotEmpty()) {
                    val p = Pattern.matches(emailValidation, binding.signUpInputEmailEditTxtEmail.text.toString())
                    if (p) {
                        binding.signUpInputEmailEditTxtEmail.setBackgroundResource(R.drawable.sign_input_selector)
                        binding.signUpInputEmailEditTxtEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                        binding.signUpInputEmailTxtValidation.visibility = GONE
                        binding.signUpInputEmailBtnNext.isEnabled = true
                        binding.signUpInputEmailBtnNext.setBackgroundResource(R.drawable.button)
                    } else {
                        binding.signUpInputEmailEditTxtEmail.setBackgroundResource(R.drawable.sign_input_validation)
                        binding.signUpInputEmailEditTxtEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.edit_txt_inner, 0)
                        binding.signUpInputEmailTxtValidation.visibility = VISIBLE
                        binding.signUpInputEmailBtnNext.isEnabled = false
                        binding.signUpInputEmailBtnNext.setBackgroundResource(R.drawable.button2)
                    }
                } else {
                    binding.signUpInputEmailEditTxtEmail.setBackgroundResource(R.drawable.sign_input_selector)
                    binding.signUpInputEmailEditTxtEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    binding.signUpInputEmailTxtValidation.visibility = GONE
                    binding.signUpInputEmailBtnNext.isEnabled = false
                    binding.signUpInputEmailBtnNext.setBackgroundResource(R.drawable.button2)
                }
            }

            // 입력이 끝났을 때
            override fun afterTextChanged(editable: Editable) {

            }
        })


        signUpViewModel.duplicationEmailCheckResponse.observe(viewLifecycleOwner, Observer {
            if (it.code == "0000") {
                signUpViewModel.postCertificationSend()
            } else if (it.code == "0001") {
                binding.signUpInputEmailEditTxtEmail.setBackgroundResource(R.drawable.sign_input_validation)
                binding.signUpInputEmailEditTxtEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.edit_txt_inner, 0)
                binding.signUpInputEmailTxtValidation.text = "이미 가입된 이메일입니다"
                binding.signUpInputEmailTxtValidation.visibility = VISIBLE
                binding.signUpInputEmailBtnNext.isEnabled = false
                binding.signUpInputEmailBtnNext.setBackgroundResource(R.drawable.button2)
            }

        })

        signUpViewModel.duplicationEmailCheckFailure.observe(viewLifecycleOwner, Observer {
            when (it) {
                409 -> {
                    binding.signUpInputEmailEditTxtEmail.setBackgroundResource(R.drawable.sign_input_validation)
                    binding.signUpInputEmailEditTxtEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.edit_txt_inner, 0)
                    binding.signUpInputEmailTxtValidation.text = "이미 가입된 이메일입니다"
                    binding.signUpInputEmailTxtValidation.visibility = VISIBLE
                    binding.signUpInputEmailBtnNext.isEnabled = false
                    binding.signUpInputEmailBtnNext.setBackgroundResource(R.drawable.button2)
               }
            }
        })

        signUpViewModel.certificationSendResponse.observe(viewLifecycleOwner, Observer {
            if (it.code == "0000") {
                val action =
                    SignUpInputEmailFragmentDirections.
                    actionSignUpInputEmailFragmentToSignUpInputCertificationNumFragment()
                view.findNavController().navigate(action)
            }
        })

        signUpViewModel.certificationSendFailure.observe(viewLifecycleOwner, Observer {

        })

        binding.signUpInputEmailBtnNext.setOnClickListener {
            signUpViewModel.email = binding.signUpInputEmailEditTxtEmail.text.toString()
            signUpViewModel.getDuplicationEmailCheck()
        }

    }
}