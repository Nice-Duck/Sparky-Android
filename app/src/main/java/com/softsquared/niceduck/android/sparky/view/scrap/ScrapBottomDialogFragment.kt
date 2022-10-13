package com.softsquared.niceduck.android.sparky.view.scrap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.FragmentScrapBottomDialogBinding
import com.softsquared.niceduck.android.sparky.databinding.FragmentSignUpInputEmailBinding
import com.softsquared.niceduck.android.sparky.utill.BaseFragment
import com.softsquared.niceduck.android.sparky.viewmodel.ScrapTemplateViewModel
import com.softsquared.niceduck.android.sparky.viewmodel.SignUpViewModel

class ScrapBottomDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentScrapBottomDialogBinding? = null
    private val binding get() = _binding!!
    private val scrapTemplateViewModel: ScrapTemplateViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScrapBottomDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTagAddRecyclerview()

        scrapTemplateViewModel.getTagAddData().observe(this, Observer {
            scrapTemplateViewModel.getTagAddAdapter().submitList(it)
        })
    }

    private fun setTagAddRecyclerview() {
        val layoutManager = FlexboxLayoutManager(activity)
        layoutManager.flexDirection = FlexDirection.ROW
        with(binding.scrapBottomDialogRecyclerview) {
            this.layoutManager = layoutManager
            adapter = scrapTemplateViewModel.getTagAddAdapter()
            visibility = View.VISIBLE
        }
    }
}

