package com.softsquared.niceduck.android.sparky.view.main.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.View.*
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
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

        binding.homeLL.setOnClickListener {
            hideKeyboard()
            it.clearFocus()
        }


        binding.homeEditTxt.setOnEditorActionListener { textView, i, event ->
            if ((i == EditorInfo.IME_ACTION_DONE) ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                binding.homeLL.clearFocus()
                if (binding.homeEditTxt.text.isNotEmpty()) {
                    mainViewModel.homeSearchType = 0
                    mainViewModel.homeSearchTitle =  binding.homeEditTxt.text.toString()
                    mainViewModel.postHomeScrapSearch()
                }
            }
            return@setOnEditorActionListener false
        }

        // 검색 기능을 위한 watcher
        binding.homeEditTxt.addTextChangedListener {


            if (binding.homeEditTxt.text.isNotEmpty()) {
                binding.homeImgSearchDeleteBtn.visibility = VISIBLE
                binding.homeEditTxt.backgroundTintList = ColorStateList.valueOf(
                    Color.parseColor(
                        "#FF000000"
                    )
                )
                binding.homeEditTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit_txt_inner_search2, 0, 0, 0)
            } else {
                mainViewModel.getHomeScrapLoad()
                binding.homeImgSearchDeleteBtn.visibility = View.GONE
                binding.homeEditTxt.backgroundTintList = ColorStateList.valueOf(
                    Color.parseColor(
                        "#BEBDBD"
                    )
                )
                binding.homeEditTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit_txt_inner_search, 0, 0, 0)
            }
        }

        mainViewModel.homeScrapSearchResponse.observe(viewLifecycleOwner) { response ->
            when (response.code) {
                "0000" -> {
                    mainViewModel.homeScrapDataSet = response.result
                    val othersScrapDataSet = response.result
                    setOthersRecyclerview(othersScrapDataSet)
                }
            }
        }

        mainViewModel.homeScrapSearchFailure.observe(viewLifecycleOwner) { code ->
            when (code) {
                401 -> {
                    lifecycleScope.launch  {
                        mainViewModel.postReissueAccessToken()
                        mainViewModel.postScrapSearch()
                    }
                }
                else -> {
                    showCustomToast("네트워크 연결이 원활하지 않습니다.")
                }
            }
        }

        binding.homeImgSearchDeleteBtn.setOnClickListener {
            binding.homeEditTxt.text.clear()
        }


        // 스크랩 조회
        mainViewModel.getHomeScrapLoad()

        mainViewModel.homeScrapLoadResponse.observe(
            viewLifecycleOwner
        ) { response ->
            when (response.code) {
                "0000" -> {
                    binding.homeEditTxt.text.clear()
                    val myScrapDataSet = response.result.myScraps
                    mainViewModel.homeScrapDataSet = response.result.recScraps
                    val othersScrapDataSet =  response.result.recScraps
                    setMyRecyclerview(myScrapDataSet)
                    setOthersRecyclerview(othersScrapDataSet)
                    hideLoading()
                }
                else -> {
                    binding.homeRecyclerviewMyScrap.visibility = INVISIBLE
                    showCustomToast("네트워크 연결이 원활하지 않습니다.")
                    hideLoading()
                }
            }
        }

        mainViewModel.homeScrapLoadFailure.observe(
            viewLifecycleOwner
        ) { code ->
            when (code) {
                401 -> {
                    lifecycleScope.launch {
                        mainViewModel.postReissueAccessToken()
                        mainViewModel.getHomeScrapLoad()
                    }
                }
                else -> {
                    showCustomToast("네트워크 연결이 원활하지 않습니다.")
                    hideLoading()
                }
            }
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
        if (othersScrapDataSet != null) {
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
