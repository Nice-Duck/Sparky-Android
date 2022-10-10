package com.softsquared.niceduck.android.sparky.view.sign_up.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.FragmentSignUpInputNameBinding
import com.softsquared.niceduck.android.sparky.model.SignUpRequest
import com.softsquared.niceduck.android.sparky.utill.BaseFragment
import com.softsquared.niceduck.android.sparky.view.sign_up.SignUpSuccessActivity
import com.softsquared.niceduck.android.sparky.viewmodel.SignUpViewModel
import java.util.*
import java.util.regex.Pattern

class SignUpInputNameFragment :
    BaseFragment<FragmentSignUpInputNameBinding>(FragmentSignUpInputNameBinding::bind, R.layout.fragment_sign_up_input_name) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signUpViewModel: SignUpViewModel by activityViewModels()

        signUpViewModel.progress.value = 100

        val nameValidation = "^[가-힣ㄱ-ㅎa-zA-Z0-9]{2,16}$"

        binding.signUpInputNameEditTxtName.addTextChangedListener(object : TextWatcher {
            // 입력하기 전에
            override fun beforeTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            // 입력되는 텍스트에 변화가 있을 때
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val p = Pattern.matches(nameValidation, binding.signUpInputNameEditTxtName.text.toString())
                if (binding.signUpInputNameEditTxtName.text.isNotEmpty()) {
                    binding.signUpInputNameTxtValidation.text = "특수문자는 사용할 수 없습니다"
                    if (p) {
                        binding.signUpInputNameEditTxtName.setBackgroundResource(R.drawable.sign_input_selector)
                        binding.signUpInputNameEditTxtName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                        binding.signUpInputNameTxtValidation.visibility = View.GONE
                        binding.signUpInputNameBtnNext.isEnabled = true
                        binding.signUpInputNameBtnNext.setBackgroundResource(R.drawable.button)
                    } else {
                        binding.signUpInputNameEditTxtName.setBackgroundResource(R.drawable.sign_input_validation)
                        binding.signUpInputNameEditTxtName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.edit_txt_inner, 0)
                        binding.signUpInputNameTxtValidation.visibility = View.VISIBLE
                        binding.signUpInputNameBtnNext.isEnabled = false
                        binding.signUpInputNameBtnNext.setBackgroundResource(R.drawable.button2)
                    }
                } else {
                    binding.signUpInputNameEditTxtName.setBackgroundResource(R.drawable.sign_input_selector)
                    binding.signUpInputNameEditTxtName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    binding.signUpInputNameTxtValidation.visibility = View.GONE
                    binding.signUpInputNameBtnNext.isEnabled = false
                    binding.signUpInputNameBtnNext.setBackgroundResource(R.drawable.button2)
                }
            }

            // 입력이 끝났을 때
            override fun afterTextChanged(editable: Editable) {

            }
        })

        signUpViewModel.duplicationNameCheckResponse.observe(viewLifecycleOwner) {
            if (it.code == "0000") {
                signUpViewModel.postSignUp()
            } else if (it.code == "0004") {
                binding.signUpInputNameEditTxtName.setBackgroundResource(R.drawable.sign_input_validation)
                binding.signUpInputNameEditTxtName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.edit_txt_inner, 0)
                binding.signUpInputNameBtnNext.isEnabled = false
                binding.signUpInputNameTxtValidation.text = "이미 존재하는 닉네임입니다"
                binding.signUpInputNameTxtValidation.visibility = View.VISIBLE
                binding.signUpInputNameBtnNext.setBackgroundResource(R.drawable.button2)
            }
        }

        signUpViewModel.duplicationNameCheckFailure.observe(viewLifecycleOwner) {
            binding.signUpInputNameEditTxtName.setBackgroundResource(R.drawable.sign_input_validation)
            binding.signUpInputNameEditTxtName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.edit_txt_inner, 0)
            binding.signUpInputNameBtnNext.isEnabled = false
            binding.signUpInputNameTxtValidation.text = "이미 존재하는 닉네임입니다"
            binding.signUpInputNameTxtValidation.visibility = View.VISIBLE
            binding.signUpInputNameBtnNext.setBackgroundResource(R.drawable.button2)
        }

        signUpViewModel.signUpResponse.observe(viewLifecycleOwner) {
            if (it.code == "0000") {
                val intent = Intent(activity, SignUpSuccessActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }

        signUpViewModel.signUpFailure.observe(viewLifecycleOwner) {

        }

        binding.signUpInputNameBtnNext.setOnClickListener {
            signUpViewModel.name = binding.signUpInputNameEditTxtName.text.toString()
            signUpViewModel.getDuplicationNameCheck()
        }
    }
}