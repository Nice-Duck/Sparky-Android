package com.softsquared.niceduck.android.sparky.view.sign_up.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import com.softsquared.niceduck.android.sparky.databinding.FragmentSignUpInputNameBinding
import com.softsquared.niceduck.android.sparky.utill.BaseFragment
import com.softsquared.niceduck.android.sparky.view.sign_up.SignUpSuccessActivity
import com.softsquared.niceduck.android.sparky.viewmodel.SignUpViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import java.util.regex.Pattern

class SignUpInputNameFragment :
    BaseFragment<FragmentSignUpInputNameBinding>(FragmentSignUpInputNameBinding::bind, R.layout.fragment_sign_up_input_name) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signUpViewModel: SignUpViewModel by activityViewModels()

        val dlg = activity?.let { Dialog(it) }
        dlg?.setCancelable(false)
        dlg?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg?.setContentView(R.layout.dialog_lottie_loading)
        dlg?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        signUpViewModel.progress.value = 100

        binding.signUpInputNameEditTxtName.setOnEditorActionListener { textView, i, keyEvent ->
            if (i== EditorInfo.IME_ACTION_DONE){
                binding.signUpInputNameLL.clearFocus()
            }
            return@setOnEditorActionListener false
        }

        binding.signUpInputNameLL.setOnClickListener {
            hideKeyboard()
            it.clearFocus()
        }

        val nameValidation = "^[가-힣ㄱ-ㅎa-zA-Z0-9]{1,16}$"

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
                        binding.signUpInputNameTxtValidation.visibility = View.GONE
                        if (binding.signUpInputNameEditTxtName.text.length >= 2) {
                            binding.signUpInputNameBtnNext.isEnabled = true
                            binding.signUpInputNameBtnNext.setBackgroundResource(R.drawable.button)
                        }
                    } else {
                        binding.signUpInputNameBtnNext.isEnabled = false
                        binding.signUpInputNameBtnNext.setBackgroundResource(R.drawable.button2)
                        binding.signUpInputNameEditTxtName.setBackgroundResource(R.drawable.sign_input_validation)
                        binding.signUpInputNameTxtValidation.visibility = View.VISIBLE
                    }
                } else {
                    binding.signUpInputNameEditTxtName.setBackgroundResource(R.drawable.sign_input_selector)
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
                dlg?.show()
                signUpViewModel.postSignUp()
            }
        }

        signUpViewModel.duplicationNameCheckFailure.observe(viewLifecycleOwner) {
            binding.signUpInputNameEditTxtName.setBackgroundResource(R.drawable.sign_input_validation)
            binding.signUpInputNameBtnNext.isEnabled = false
            binding.signUpInputNameTxtValidation.text = it.message
            binding.signUpInputNameTxtValidation.visibility = View.VISIBLE
            binding.signUpInputNameBtnNext.setBackgroundResource(R.drawable.button2)
        }

        signUpViewModel.signUpResponse.observe(viewLifecycleOwner) {
            lifecycleScope.launch  {
                delay(1000)
                dlg?.dismiss()
            }
            if (it.code == "0000") {
                val editor = ApplicationClass.sSharedPreferences.edit()
                editor.putString(ApplicationClass.X_ACCESS_TOKEN, it.result?.accessToken)
                editor.putString(ApplicationClass.X_REFRESH_TOKEN, it.result?.refreshToken)
                editor.apply()

                val intent = Intent(activity, SignUpSuccessActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }

        signUpViewModel.signUpFailure.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                delay(1000)
                dlg?.dismiss()
                it.message?.let { message -> showCustomToast(message) }
            }
        }

        binding.signUpInputNameBtnNext.setOnClickListener {
            signUpViewModel.name = binding.signUpInputNameEditTxtName.text.toString()
            signUpViewModel.getDuplicationNameCheck()
        }
    }
}
