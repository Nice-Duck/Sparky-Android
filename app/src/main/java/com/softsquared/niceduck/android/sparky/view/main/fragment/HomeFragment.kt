package com.softsquared.niceduck.android.sparky.view.main.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.FragmentHomeBinding
import com.softsquared.niceduck.android.sparky.model.Scrap
import com.softsquared.niceduck.android.sparky.utill.BaseFragment
import com.softsquared.niceduck.android.sparky.view.my_page.MyPageActivity
import com.softsquared.niceduck.android.sparky.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment :
    BaseFragment<FragmentHomeBinding>(
        FragmentHomeBinding::bind, R.layout.fragment_home
    ) {
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 스크랩 조회
        mainViewModel.getHomeScrapLoad()

        mainViewModel.homeScrapLoadResponse.observe(
            viewLifecycleOwner
        ) { response ->
            when (response.code) {
                "0000" -> {
                    val myScrapDataSet = response.result.myScraps
                    val othersScrapDataSet = response.result.recScraps
                    setMyRecyclerview(myScrapDataSet)
                    setOthersRecyclerview(othersScrapDataSet)
                }
                else -> {
                    binding.homeRecyclerviewMyScrap.visibility = INVISIBLE
                    showCustomToast("네트워크 연결이 원활하지 않습니다.")
                }
            }
            hideLoading()
        }

        mainViewModel.homeScrapLoadFailure.observe(
            viewLifecycleOwner
        ) { code ->
            when (code) {
                401 -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        mainViewModel.postReissueAccessToken()
                        mainViewModel.getHomeScrapLoad()
                    }
                }
                else -> {
                    showCustomToast("네트워크 연결이 원활하지 않습니다.")
                }
            }
            hideLoading()
        }

        binding.homeBtnScrapAdd.setOnClickListener {
            val bottomDialogFragment = ScrapAddBottomDialogFragment()
            bottomDialogFragment.show(childFragmentManager, bottomDialogFragment.tag)
        }

        binding.homeImgMyPage.setOnClickListener {
            val intent = Intent(activity, MyPageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setMyRecyclerview(myScrapDataSet: List<Scrap>?) {
        if (myScrapDataSet.isNullOrEmpty()) {
            binding.homeLLNoneMyScrap.visibility = VISIBLE
        } else {
            val layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

            val myScrapRecyclerviewAdapter = MyScrapRecyclerviewAdapter()
            myScrapRecyclerviewAdapter.submitList(myScrapDataSet)

            with(binding.homeRecyclerviewMyScrap) {
                this.layoutManager = layoutManager
                adapter = myScrapRecyclerviewAdapter
                visibility = VISIBLE
            }
        }
    }

    private fun setOthersRecyclerview(othersScrapDataSet: List<Scrap>?) {
        if (!othersScrapDataSet.isNullOrEmpty()) {
            val layoutManager = GridLayoutManager(activity, 2)
            layoutManager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val i = (position + 1) % 6
                    return when (i) {
                        1, 2 -> 1
                        else -> 2
                    }
                }
            }

            val othersScrapRecyclerviewAdapter = OthersScrapRecyclerviewAdapter()
            othersScrapRecyclerviewAdapter.submitList(othersScrapDataSet)

            with(binding.homeRecyclerviewOthersScrap) {
                this.layoutManager = layoutManager
                adapter = othersScrapRecyclerviewAdapter
                visibility = VISIBLE
            }
        }
    }

    private fun hideLoading() {
        with(binding.homeLoading1) {
            if (isShimmerStarted) {
                stopShimmer()
                visibility = GONE
            }
        }

        with(binding.homeLoading2) {
            if (isShimmerStarted) {
                stopShimmer()
                visibility = GONE
            }
        }
    }
}
