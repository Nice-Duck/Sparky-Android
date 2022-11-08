package com.softsquared.niceduck.android.sparky.view.main.fragment

import android.app.Dialog
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.FragmentScrapAddBottomDialogBinding
import com.softsquared.niceduck.android.sparky.view.scrap.ScrapTemplateActivity
import com.softsquared.niceduck.android.sparky.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ScrapAddBottomDialogFragment : BottomSheetDialogFragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentScrapAddBottomDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState)
        if (bottomSheetDialog is BottomSheetDialog) {
            bottomSheetDialog.behavior.skipCollapsed = true
            bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return bottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScrapAddBottomDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.scrapAddBottomDialogImgBackBtn.setOnClickListener {
            dismiss()
        }

        mainViewModel.scrapValidationResponse.observe(viewLifecycleOwner) { response ->
            when (response.code) {
                "0000" -> {
                    val intent = Intent(activity, ScrapTemplateActivity::class.java)
                    intent.putExtra("add", binding.scrapAddBottomDialogEditTxtEmail.text.toString())
                    startActivity(intent)
                }

                "F002" -> {
                    binding.scrapAddBottomDialogEditTxtEmail.text.clear()
                    binding.scrapAddBottomDialogBtn.setBackgroundResource(R.drawable.button2)
                    binding.scrapAddBottomDialogTxtValidation.visibility = VISIBLE
                    binding.scrapAddBottomDialogBtn.isEnabled = false
                }
            }
        }

        mainViewModel.scrapValidationFailure.observe(viewLifecycleOwner) { code ->
            when (code) {
                401 -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        mainViewModel.postReissueAccessToken()
                        mainViewModel.getScrapValidation(binding.scrapAddBottomDialogEditTxtEmail.text.toString())
                    }
                }
                else -> {
                    binding.scrapAddBottomDialogEditTxtEmail.text.clear()
                    binding.scrapAddBottomDialogBtn.setBackgroundResource(R.drawable.button2)
                    binding.scrapAddBottomDialogTxtValidation.visibility = VISIBLE
                    binding.scrapAddBottomDialogBtn.isEnabled = false
                }
            }
        }

        binding.scrapAddBottomDialogEditTxtEmail.addTextChangedListener {
            if (binding.scrapAddBottomDialogEditTxtEmail.text.isNotEmpty()) {
                binding.scrapAddBottomDialogTxtValidation.visibility = GONE
                binding.scrapAddBottomDialogEditTxtEmail.setBackgroundResource(R.drawable.sign_input_selector)
                binding.scrapAddBottomDialogBtn.isEnabled = true
                binding.scrapAddBottomDialogBtn.setBackgroundResource(R.drawable.button)
            } else {
                binding.scrapAddBottomDialogBtn.isEnabled = false
                binding.scrapAddBottomDialogBtn.setBackgroundResource(R.drawable.button2)
            }
        }

        binding.scrapAddBottomDialogBtn.setOnClickListener {
            mainViewModel.getScrapValidation(binding.scrapAddBottomDialogEditTxtEmail.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
