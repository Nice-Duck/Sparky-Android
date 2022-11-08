package com.softsquared.niceduck.android.sparky.view.sign_up.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.FragmentSignUpInputCertificationNumBinding
import com.softsquared.niceduck.android.sparky.utill.BaseFragment
import com.softsquared.niceduck.android.sparky.viewmodel.SignUpViewModel

class SignUpInputCertificationNumFragment :
    BaseFragment<FragmentSignUpInputCertificationNumBinding>(FragmentSignUpInputCertificationNumBinding::bind, R.layout.fragment_sign_up_input_certification_num) {
    lateinit var countDownTimer: CountDownTimer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signUpViewModel: SignUpViewModel by activityViewModels()

        binding.signUpInputCertificationLL.setOnClickListener {
            hideKeyboard()
            it.clearFocus()
        }

        countDownTimer = object : CountDownTimer(180000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var minutes = (millisUntilFinished / 1000 / 60 % 60).toString()
                var seconds = (millisUntilFinished / 1000 % 60).toString()
                if (minutes.length < 2) {
                    minutes = "0$minutes"
                }
                if (seconds.length < 2) {
                    seconds = "0$seconds"
                }
                binding.signUpInputCertificationTxtTimer.text = "$minutes:$seconds"
            }

            override fun onFinish() {
                binding.signUpInputCertificationTxtTimer.visibility = GONE
                binding.signUpInputCertificationNumBtnNext.isEnabled = false
                showCustomToast("인증 시간이 초과되었습니다.")
                findNavController().popBackStack()
            }
        }

        if (!signUpViewModel.isChecked) {
            binding.signUpInputCertificationTxtTimer.visibility = VISIBLE
            countDownTimer.start()
        }

        signUpViewModel.progress.value = 35

        binding.signUpInputCertificationNumBtnNext.setOnClickListener {
            signUpViewModel.postCertificationCheck(
                "${binding.signUpInputCertificationEditTxt1.text}" +
                    "${binding.signUpInputCertificationEditTxt2.text}" +
                    "${binding.signUpInputCertificationEditTxt3.text}" +
                    "${binding.signUpInputCertificationEditTxt4.text}" +
                    "${binding.signUpInputCertificationEditTxt5.text}" +
                    "${binding.signUpInputCertificationEditTxt6.text}"
            )
        }

        binding.signUpInputCertificationEditTxt1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setViewBackground()
                if (binding.signUpInputCertificationEditTxt1.text.isNotEmpty()) {
                    binding.signUpInputCertificationEditTxt1.setBackgroundResource(R.drawable.sign_input_focused)
                    binding.signUpInputCertificationEditTxt2.requestFocus()
                } else {
                    binding.signUpInputCertificationEditTxt1.setBackgroundResource(R.drawable.sign_input_selector)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })


        binding.signUpInputCertificationEditTxt2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setViewBackground()
                if (binding.signUpInputCertificationEditTxt2.text.isNotEmpty()) {
                    binding.signUpInputCertificationEditTxt2.setBackgroundResource(R.drawable.sign_input_focused)
                    binding.signUpInputCertificationEditTxt3.requestFocus()
                } else {
                    binding.signUpInputCertificationEditTxt2.setBackgroundResource(R.drawable.sign_input_selector)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.signUpInputCertificationEditTxt3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setViewBackground()
                if (binding.signUpInputCertificationEditTxt3.text.isNotEmpty()) {
                    binding.signUpInputCertificationEditTxt3.setBackgroundResource(R.drawable.sign_input_focused)
                    binding.signUpInputCertificationEditTxt4.requestFocus()
                } else {
                    binding.signUpInputCertificationEditTxt3.setBackgroundResource(R.drawable.sign_input_selector)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.signUpInputCertificationEditTxt4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setViewBackground()
                if (binding.signUpInputCertificationEditTxt4.text.isNotEmpty()) {
                    binding.signUpInputCertificationEditTxt4.setBackgroundResource(R.drawable.sign_input_focused)
                    binding.signUpInputCertificationEditTxt5.requestFocus()
                } else {
                    binding.signUpInputCertificationEditTxt4.setBackgroundResource(R.drawable.sign_input_selector)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.signUpInputCertificationEditTxt5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setViewBackground()
                if (binding.signUpInputCertificationEditTxt5.text.isNotEmpty()) {
                    binding.signUpInputCertificationEditTxt5.setBackgroundResource(R.drawable.sign_input_focused)
                    binding.signUpInputCertificationEditTxt6.requestFocus()
                } else {
                    binding.signUpInputCertificationEditTxt5.setBackgroundResource(R.drawable.sign_input_selector)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.signUpInputCertificationEditTxt6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setViewBackground()
                if (binding.signUpInputCertificationEditTxt6.text.isNotEmpty()) {
                    binding.signUpInputCertificationEditTxt6.setBackgroundResource(R.drawable.sign_input_focused)
                } else {
                    binding.signUpInputCertificationEditTxt6.setBackgroundResource(R.drawable.sign_input_selector)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.signUpInputCertificationEditTxt2.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                if (binding.signUpInputCertificationEditTxt2.text.isEmpty()) {
                    binding.signUpInputCertificationEditTxt1.requestFocus()
                }
            }
            return@setOnKeyListener false
        }

        binding.signUpInputCertificationEditTxt3.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                if (binding.signUpInputCertificationEditTxt3.text.isEmpty()) {
                    binding.signUpInputCertificationEditTxt2.requestFocus()
                }
            }
            return@setOnKeyListener false
        }

        binding.signUpInputCertificationEditTxt4.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                if (binding.signUpInputCertificationEditTxt4.text.isEmpty()) {
                    binding.signUpInputCertificationEditTxt3.requestFocus()
                }
            }
            return@setOnKeyListener false
        }

        binding.signUpInputCertificationEditTxt5.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                if (binding.signUpInputCertificationEditTxt5.text.isEmpty()) {
                    binding.signUpInputCertificationEditTxt4.requestFocus()
                }
            }
            return@setOnKeyListener false
        }

        binding.signUpInputCertificationEditTxt6.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                if (binding.signUpInputCertificationEditTxt6.text.isEmpty()) {
                    binding.signUpInputCertificationEditTxt5.requestFocus()
                }
            }
            return@setOnKeyListener false
        }

        binding.signUpInputCertificationEditTxt6.setOnEditorActionListener { textView, i, keyEvent ->
            if (i== EditorInfo.IME_ACTION_DONE){
                binding.signUpInputCertificationLL.clearFocus()
            }
            return@setOnEditorActionListener false
        }

        signUpViewModel.certificationCheckResponse.observe(viewLifecycleOwner) {
            if (it.code == "0000") {
                countDownTimer.cancel()
                binding.signUpInputCertificationTxtTimer.visibility = GONE
                val action =
                    SignUpInputCertificationNumFragmentDirections
                        .actionSignUpInputCertificationNumFragmentToSignUpInputPwdFragment()
                view.findNavController().navigate(action)
            }
        }

        signUpViewModel.certificationCheckFailure.observe(viewLifecycleOwner) {
            it.message?.let { message -> showCustomToast(message) }
            binding.signUpInputCertificationEditTxt1.text.clear()
            binding.signUpInputCertificationEditTxt2.text.clear()
            binding.signUpInputCertificationEditTxt3.text.clear()
            binding.signUpInputCertificationEditTxt4.text.clear()
            binding.signUpInputCertificationEditTxt5.text.clear()
            binding.signUpInputCertificationEditTxt6.text.clear()

            binding.signUpInputCertificationEditTxt1.setBackgroundResource(R.drawable.sign_input_validation)
            binding.signUpInputCertificationEditTxt2.setBackgroundResource(R.drawable.sign_input_validation)
            binding.signUpInputCertificationEditTxt3.setBackgroundResource(R.drawable.sign_input_validation)
            binding.signUpInputCertificationEditTxt4.setBackgroundResource(R.drawable.sign_input_validation)
            binding.signUpInputCertificationEditTxt5.setBackgroundResource(R.drawable.sign_input_validation)
            binding.signUpInputCertificationEditTxt6.setBackgroundResource(R.drawable.sign_input_validation)

            binding.signUpInputCertificationTxtValidation.visibility = VISIBLE

            binding.signUpInputCertificationEditTxt1.isFocusable = true

            binding.signUpInputCertificationEditTxt1.requestFocus()

            binding.signUpInputCertificationNumBtnNext.isEnabled = false
        }
    }

    private fun setViewBackground() {
        // 하나라도 입력이 있다면 전체 selector
        if (binding.signUpInputCertificationEditTxt1.text.isNotEmpty() ||
            binding.signUpInputCertificationEditTxt2.text.isNotEmpty() ||
            binding.signUpInputCertificationEditTxt3.text.isNotEmpty() ||
            binding.signUpInputCertificationEditTxt4.text.isNotEmpty() ||
            binding.signUpInputCertificationEditTxt5.text.isNotEmpty() ||
            binding.signUpInputCertificationEditTxt6.text.isNotEmpty()
        ) {

            if (binding.signUpInputCertificationEditTxt1.text.isEmpty())
                binding.signUpInputCertificationEditTxt1.setBackgroundResource(R.drawable.sign_input_selector)
            if (binding.signUpInputCertificationEditTxt2.text.isEmpty())
                binding.signUpInputCertificationEditTxt2.setBackgroundResource(R.drawable.sign_input_selector)
            if (binding.signUpInputCertificationEditTxt3.text.isEmpty())
                binding.signUpInputCertificationEditTxt3.setBackgroundResource(R.drawable.sign_input_selector)
            if (binding.signUpInputCertificationEditTxt4.text.isEmpty())
                binding.signUpInputCertificationEditTxt4.setBackgroundResource(R.drawable.sign_input_selector)
            if (binding.signUpInputCertificationEditTxt5.text.isEmpty())
                binding.signUpInputCertificationEditTxt5.setBackgroundResource(R.drawable.sign_input_selector)
            if (binding.signUpInputCertificationEditTxt6.text.isEmpty())
                binding.signUpInputCertificationEditTxt6.setBackgroundResource(R.drawable.sign_input_selector)

            binding.signUpInputCertificationTxtValidation.visibility = GONE
        }

        // 모두 입력이 있다면 Button 활성화
        if (binding.signUpInputCertificationEditTxt1.text.isNotEmpty() &&
            binding.signUpInputCertificationEditTxt2.text.isNotEmpty() &&
            binding.signUpInputCertificationEditTxt3.text.isNotEmpty() &&
            binding.signUpInputCertificationEditTxt4.text.isNotEmpty() &&
            binding.signUpInputCertificationEditTxt5.text.isNotEmpty() &&
            binding.signUpInputCertificationEditTxt6.text.isNotEmpty()
        ) {

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

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer.cancel()
    }
}
