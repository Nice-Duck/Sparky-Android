package com.softsquared.niceduck.android.sparky.view.sign_up.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.*
import android.view.Window
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.FragmentSignUpInputEmailBinding
import com.softsquared.niceduck.android.sparky.utill.BaseFragment
import com.softsquared.niceduck.android.sparky.viewmodel.SignUpViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern


class SignUpInputEmailFragment :
    BaseFragment<FragmentSignUpInputEmailBinding>(FragmentSignUpInputEmailBinding::bind, R.layout.fragment_sign_up_input_email) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dlg = activity?.let { Dialog(it) }
        dlg?.setCancelable(false)
        dlg?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg?.setContentView(R.layout.dialog_lottie_loading)
        dlg?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val signUpViewModel: SignUpViewModel by activityViewModels()

        signUpViewModel.isChecked = false
        signUpViewModel.progress.value = 0

        binding.signUpInputEmailLL.setOnClickListener {
            hideKeyboard()
            it.clearFocus()
        }

        binding.signUpInputEmailEditTxtEmail.setOnEditorActionListener { textView, i, keyEvent ->
            if (i== EditorInfo.IME_ACTION_DONE){
                binding.signUpInputEmailLL.clearFocus()
            }
            return@setOnEditorActionListener false
        }


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
                        binding.signUpInputEmailTxtValidation.visibility = GONE
                        binding.signUpInputEmailBtnNext.isEnabled = true
                        binding.signUpInputEmailBtnNext.setBackgroundResource(R.drawable.button)
                    } else {
                        binding.signUpInputEmailEditTxtEmail.setBackgroundResource(R.drawable.sign_input_validation)
                        binding.signUpInputEmailTxtValidation.visibility = VISIBLE
                        binding.signUpInputEmailBtnNext.isEnabled = false
                        binding.signUpInputEmailBtnNext.setBackgroundResource(R.drawable.button2)
                    }
                } else {
                    binding.signUpInputEmailEditTxtEmail.setBackgroundResource(R.drawable.sign_input_selector)
                    binding.signUpInputEmailTxtValidation.visibility = GONE
                    binding.signUpInputEmailBtnNext.isEnabled = false
                    binding.signUpInputEmailBtnNext.setBackgroundResource(R.drawable.button2)
                }
            }

            // 입력이 끝났을 때
            override fun afterTextChanged(editable: Editable) {
            }
        })

        signUpViewModel.duplicationEmailCheckResponse.observe(viewLifecycleOwner) {
            if (it.code == "0000") {
                dlg?.show()
                signUpViewModel.postCertificationSend()

            }
        }

        signUpViewModel.duplicationEmailCheckFailure.observe(viewLifecycleOwner) {
            binding.signUpInputEmailEditTxtEmail.setBackgroundResource(R.drawable.sign_input_validation)
            binding.signUpInputEmailTxtValidation.text = it.message
            binding.signUpInputEmailTxtValidation.visibility = VISIBLE
            binding.signUpInputEmailBtnNext.isEnabled = false
            binding.signUpInputEmailBtnNext.setBackgroundResource(R.drawable.button2)

        }

        signUpViewModel.certificationSendResponse.observe(viewLifecycleOwner) {
            lifecycleScope.launch  {
                delay(1000)
                dlg?.dismiss()
            }
            if (it.code == "0000") {
                val action =
                    SignUpInputEmailFragmentDirections
                        .actionSignUpInputEmailFragmentToSignUpInputCertificationNumFragment()
                view.findNavController().navigate(action)
            }
        }

        signUpViewModel.certificationSendFailure.observe(viewLifecycleOwner) {
           lifecycleScope.launch {
                delay(1000)
                dlg?.dismiss()
            }
            it.message?.let { message -> showCustomToast(message) }

        }

        binding.signUpInputEmailBtnNext.setOnClickListener {
            signUpViewModel.email = binding.signUpInputEmailEditTxtEmail.text.toString()
            signUpViewModel.getDuplicationEmailCheck()
        }
    }
}
