package com.softsquared.niceduck.android.sparky.view.main.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.FragmentHomeBinding
import com.softsquared.niceduck.android.sparky.databinding.FragmentSignUpInputCertificationNumBinding
import com.softsquared.niceduck.android.sparky.utill.BaseFragment


class HomeFragment :
    BaseFragment<FragmentHomeBinding>(
        FragmentHomeBinding::bind, R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}