package com.softsquared.niceduck.android.sparky.view.main.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.FragmentHomeBinding
import com.softsquared.niceduck.android.sparky.utill.BaseFragment
import com.softsquared.niceduck.android.sparky.viewmodel.MainViewModel



class HomeFragment :
    BaseFragment<FragmentHomeBinding>(
        FragmentHomeBinding::bind, R.layout.fragment_home) {
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.getMyScrapData().observe(viewLifecycleOwner, Observer {
            mainViewModel.getMyScrapAdapter().submitList(it)
        })

        mainViewModel.getOthersScrapData().observe(viewLifecycleOwner, Observer {
            mainViewModel.getOthersScrapAdapter().submitList(it)
        })

        val layoutManager1 = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        with(binding.homeRecyclerviewMyScrap) {
            this.layoutManager = layoutManager1
            adapter = mainViewModel.getMyScrapAdapter()
        }

        val layoutManager2 = GridLayoutManager(activity, 2)
        layoutManager2.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val i = (position+1) % 6
                Log.d("어답터 테스트", "position: $position, i: $i")
                return when (i) {
                    1, 2 -> 1
                    else -> 2
                }
            }
        }

        with(binding.homeRecyclerviewOthersScrap) {
            this.layoutManager = layoutManager2
            adapter = mainViewModel.getOthersScrapAdapter()
        }


    }
}