package com.softsquared.niceduck.android.sparky.view.main.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.FragmentHomeBinding
import com.softsquared.niceduck.android.sparky.databinding.FragmentMyBinding
import com.softsquared.niceduck.android.sparky.utill.BaseFragment
import com.softsquared.niceduck.android.sparky.viewmodel.MainViewModel


class MyFragment  :
    BaseFragment<FragmentMyBinding>(
        FragmentMyBinding::bind, R.layout.fragment_my) {
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.getMyScrapData().observe(viewLifecycleOwner, Observer {
            mainViewModel.getMyScrapAdapter2().submitList(it)
        })

        val layoutManager1 = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        with(binding.myRecyclerview) {
            this.layoutManager = layoutManager1
            adapter = mainViewModel.getMyScrapAdapter2()
        }
    }
}