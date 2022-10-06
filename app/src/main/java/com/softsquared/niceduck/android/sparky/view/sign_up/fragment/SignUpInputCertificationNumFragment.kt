package com.softsquared.niceduck.android.sparky.view.sign_up.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.FragmentSignUpInputCertificationNumBinding
import com.softsquared.niceduck.android.sparky.databinding.FragmentSignUpInputEmailBinding
import com.softsquared.niceduck.android.sparky.utill.BaseFragment
import com.softsquared.niceduck.android.sparky.viewmodel.SignUpViewModel
import kotlin.math.sign

class SignUpInputCertificationNumFragment :
    BaseFragment<FragmentSignUpInputCertificationNumBinding>(FragmentSignUpInputCertificationNumBinding::bind, R.layout.fragment_sign_up_input_certification_num) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signUpViewModel: SignUpViewModel by activityViewModels()

        signUpViewModel.progress.value = 35



        binding.signUpInputCertificationNumBtnNext.setOnClickListener {
            signUpViewModel.postCertificationCheck(
                "${binding.signUpInputCertificationEditTxt1.text}" +
                    "${binding.signUpInputCertificationEditTxt2.text}" +
                    "${binding.signUpInputCertificationEditTxt3.text}" +
                    "${binding.signUpInputCertificationEditTxt4.text}" +
                    "${binding.signUpInputCertificationEditTxt5.text}" +
                    "${binding.signUpInputCertificationEditTxt6.text}")
        }

        val mTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (binding.signUpInputCertificationEditTxt1.text.isNotEmpty() ||
                    binding.signUpInputCertificationEditTxt2.text.isNotEmpty() ||
                    binding.signUpInputCertificationEditTxt3.text.isNotEmpty() ||
                    binding.signUpInputCertificationEditTxt4.text.isNotEmpty() ||
                    binding.signUpInputCertificationEditTxt5.text.isNotEmpty() ||
                    binding.signUpInputCertificationEditTxt6.text.isNotEmpty()) {

                    binding.signUpInputCertificationEditTxt1.setBackgroundResource(R.drawable.sign_input_selector)
                    binding.signUpInputCertificationEditTxt2.setBackgroundResource(R.drawable.sign_input_selector)
                    binding.signUpInputCertificationEditTxt3.setBackgroundResource(R.drawable.sign_input_selector)
                    binding.signUpInputCertificationEditTxt4.setBackgroundResource(R.drawable.sign_input_selector)
                    binding.signUpInputCertificationEditTxt5.setBackgroundResource(R.drawable.sign_input_selector)
                    binding.signUpInputCertificationEditTxt6.setBackgroundResource(R.drawable.sign_input_selector)
                    binding.signUpInputCertificationTxtValidation.visibility = GONE

                }
                if (binding.signUpInputCertificationEditTxt1.text.isNotEmpty() &&
                    binding.signUpInputCertificationEditTxt2.text.isNotEmpty() &&
                    binding.signUpInputCertificationEditTxt3.text.isNotEmpty() &&
                    binding.signUpInputCertificationEditTxt4.text.isNotEmpty() &&
                    binding.signUpInputCertificationEditTxt5.text.isNotEmpty() &&
                    binding.signUpInputCertificationEditTxt6.text.isNotEmpty()) {

                    binding.signUpInputCertificationNumBtnNext.apply {
                        isEnabled = true
                        setBackgroundResource(R.drawable.button)
                    }
                } else {
                    binding.signUpInputCertificationNumBtnNext.apply {
                        isEnabled = false
                        setBackgroundResource(R.drawable.button2)
                    }
                }
            }

            override fun afterTextChanged(editable: Editable) {
            }
        }

        binding.signUpInputCertificationEditTxt1.addTextChangedListener(mTextWatcher)
        binding.signUpInputCertificationEditTxt2.addTextChangedListener(mTextWatcher)
        binding.signUpInputCertificationEditTxt3.addTextChangedListener(mTextWatcher)
        binding.signUpInputCertificationEditTxt4.addTextChangedListener(mTextWatcher)
        binding.signUpInputCertificationEditTxt5.addTextChangedListener(mTextWatcher)
        binding.signUpInputCertificationEditTxt6.addTextChangedListener(mTextWatcher)


        signUpViewModel.certificationCheckResponse.observe(viewLifecycleOwner, Observer {
            if (it.code == "0000") {
                val action =
                    SignUpInputCertificationNumFragmentDirections
                        .actionSignUpInputCertificationNumFragmentToSignUpInputPwdFragment()
                view.findNavController().navigate(action)
            } else if (it.code == "0004") {
                binding.signUpInputCertificationEditTxt1.setBackgroundResource(R.drawable.sign_input_validation)
                binding.signUpInputCertificationEditTxt2.setBackgroundResource(R.drawable.sign_input_validation)
                binding.signUpInputCertificationEditTxt3.setBackgroundResource(R.drawable.sign_input_validation)
                binding.signUpInputCertificationEditTxt4.setBackgroundResource(R.drawable.sign_input_validation)
                binding.signUpInputCertificationEditTxt5.setBackgroundResource(R.drawable.sign_input_validation)
                binding.signUpInputCertificationEditTxt6.setBackgroundResource(R.drawable.sign_input_validation)

                binding.signUpInputCertificationEditTxt1.text.clear()
                binding.signUpInputCertificationEditTxt2.text.clear()
                binding.signUpInputCertificationEditTxt3.text.clear()
                binding.signUpInputCertificationEditTxt4.text.clear()
                binding.signUpInputCertificationEditTxt5.text.clear()
                binding.signUpInputCertificationEditTxt6.text.clear()

                binding.signUpInputCertificationTxtValidation.visibility = VISIBLE

                binding.signUpInputCertificationEditTxt1.requestFocus()

                binding.signUpInputCertificationNumBtnNext.isEnabled = false
            }
        })

        signUpViewModel.certificationCheckFailure.observe(viewLifecycleOwner, Observer {
            binding.signUpInputCertificationEditTxt1.setBackgroundResource(R.drawable.sign_input_validation)
            binding.signUpInputCertificationEditTxt2.setBackgroundResource(R.drawable.sign_input_validation)
            binding.signUpInputCertificationEditTxt3.setBackgroundResource(R.drawable.sign_input_validation)
            binding.signUpInputCertificationEditTxt4.setBackgroundResource(R.drawable.sign_input_validation)
            binding.signUpInputCertificationEditTxt5.setBackgroundResource(R.drawable.sign_input_validation)
            binding.signUpInputCertificationEditTxt6.setBackgroundResource(R.drawable.sign_input_validation)

            binding.signUpInputCertificationEditTxt1.text.clear()
            binding.signUpInputCertificationEditTxt2.text.clear()
            binding.signUpInputCertificationEditTxt3.text.clear()
            binding.signUpInputCertificationEditTxt4.text.clear()
            binding.signUpInputCertificationEditTxt5.text.clear()
            binding.signUpInputCertificationEditTxt6.text.clear()

            binding.signUpInputCertificationTxtValidation.visibility = VISIBLE

            binding.signUpInputCertificationEditTxt1.isFocusable = true

            binding.signUpInputCertificationNumBtnNext.isEnabled = false
        })
    }
}