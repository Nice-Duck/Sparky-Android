package com.softsquared.niceduck.android.sparky.view.main.fragment

import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.widget.RadioButton
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.FragmentMyBinding
import com.softsquared.niceduck.android.sparky.utill.BaseFragment
import com.softsquared.niceduck.android.sparky.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyFragment :
    BaseFragment<FragmentMyBinding>(
        FragmentMyBinding::bind, R.layout.fragment_my
    ) {
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.getMyScrapData().observe(
            viewLifecycleOwner,
            Observer {
                mainViewModel.getMyScrapAdapter2().submitList(it)
                mainViewModel.getMyScrapAdapter3().submitList(it)
            }
        )

        binding.myRadioBtn2.isChecked = true

        binding.myRadioBtn1.setOnClickListener {
            onRadioButtonClicked(it)
        }

        binding.myRadioBtn2.setOnClickListener {
            onRadioButtonClicked(it)
        }

        viewLifecycleOwner.lifecycleScope.launch {

            delay(1000)
            val layoutManager1 = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            with(binding.myRecyclerview) {
                this.layoutManager = layoutManager1
                adapter = mainViewModel.getMyScrapAdapter2()
            }
            binding.myRecyclerview.visibility = VISIBLE
            with(binding.myLoading) {
                if (isShimmerStarted) {
                    stopShimmer()
                    visibility = android.view.View.GONE
                }
            }
        }
    }

    private fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            when (view.getId()) {
                R.id.my_radio_btn1 ->
                    if (checked) {
                        with(binding.myRecyclerview) {
                            binding.myRadioBtn2.isChecked = false
                            this.layoutManager = layoutManager
                            adapter = mainViewModel.getMyScrapAdapter3()
                        }
                    }
                R.id.my_radio_btn2 ->
                    if (checked) {
                        binding.myRadioBtn1.isChecked = false
                        with(binding.myRecyclerview) {
                            this.layoutManager = layoutManager
                            adapter = mainViewModel.getMyScrapAdapter2()
                        }
                    }
            }
        }
    }
}
