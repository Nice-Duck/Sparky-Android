package com.softsquared.niceduck.android.sparky.view.main.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import com.softsquared.niceduck.android.sparky.databinding.FragmentMyBinding
import com.softsquared.niceduck.android.sparky.utill.BaseFragment
import com.softsquared.niceduck.android.sparky.view.scrap.ScrapBottomDialogFragment
import com.softsquared.niceduck.android.sparky.view.sign_in.SignInActivity
import com.softsquared.niceduck.android.sparky.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MyFragment :
    BaseFragment<FragmentMyBinding>(
        FragmentMyBinding::bind, R.layout.fragment_my
    ) {
    private val mainViewModel: MainViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.myLLFilterAddBtn.setOnClickListener {
            showCustomToast("아직 개발 중입니다.")
        }

        binding.myLL.setOnClickListener {
            hideKeyboard()
            it.clearFocus()
        }

        binding.myEditTxt.setOnEditorActionListener { textView, i, keyEvent ->
            if (i== EditorInfo.IME_ACTION_DONE){
                binding.myLL.clearFocus()
            }
            return@setOnEditorActionListener false
        }

        // 스크랩 데이터 조회
        mainViewModel.getMyScrapLoad()

        binding.myImgSearchDeleteBtn.setOnClickListener {
            binding.myEditTxt.text.clear()
        }

        mainViewModel.myScrapLoadResponse.observe(viewLifecycleOwner) { response ->
            when (response.code) {
                "0000" -> {
                    binding.myEditTxt.text.clear()
                    mainViewModel.myScrapDataSet = response.result.myScraps
                    setMyRecyclerview()

                    binding.myRadioBtn1.setOnClickListener {
                        onRadioButtonClicked(it)
                    }

                    binding.myRadioBtn2.setOnClickListener {
                        onRadioButtonClicked(it)
                    }

                    binding.myRadioBtn2.isChecked = true


                    // 검색 기능을 위한 watcher
                    binding.myEditTxt.addTextChangedListener {
                        if (binding.myEditTxt.text.isNotEmpty()) {
                            mainViewModel.searchType = 1
                            mainViewModel.searchTitle =  binding.myEditTxt.text.toString()
                            mainViewModel.postScrapSearch()
                            binding.myImgSearchDeleteBtn.visibility = VISIBLE
                            binding.myEditTxt.backgroundTintList = ColorStateList.valueOf(
                                Color.parseColor(
                                    "#FF000000"
                                )
                            )
                            binding.myEditTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit_txt_inner_search2, 0, 0, 0)
                        } else {
                            mainViewModel.getMyScrapLoad()
                            binding.myImgSearchDeleteBtn.visibility = View.GONE
                            binding.myEditTxt.backgroundTintList = ColorStateList.valueOf(
                                Color.parseColor(
                                    "#BEBDBD"
                                )
                            )
                            binding.myEditTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit_txt_inner_search, 0, 0, 0)
                        }
                    }
                }
                else -> {
                    showCustomToast("네트워크 연결이 원활하지 않습니다.")
                }
            }
            hideLoading()
        }

        mainViewModel.myScrapLoadFailure.observe(viewLifecycleOwner) { code ->
            when (code) {
                401 -> {
                    lifecycleScope.launch {
                        mainViewModel.postReissueAccessToken()
                        mainViewModel.getMyScrapLoad()
                    }
                }
                else -> {
                    showCustomToast("네트워크 연결이 원활하지 않습니다.")
                }
            }
            hideLoading()
        }


        mainViewModel.myScrapSearchResponse.observe(viewLifecycleOwner) { response ->
            when (response.code) {
                "0000" -> {
                    mainViewModel.myScrapDataSet = response.result
                    setMyRecyclerview()
                }
                else -> {
                    showCustomToast("네트워크 연결이 원활하지 않습니다.")
                }
            }
        }

        mainViewModel.myScrapSearchFailure.observe(viewLifecycleOwner) { code ->
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


        binding.myImgSearchDeleteBtn.setOnClickListener {
            binding.myEditTxt.text.clear()
        }


    }

    private fun hideLoading() {
        with(binding.myLoading) {
            if (isShimmerStarted) {
                stopShimmer()
                visibility = View.GONE
            }
        }
    }

    private fun setMyRecyclerview() {
        if (mainViewModel.myScrapDataSet != null) {
            val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            val myScrapRecyclerviewAdapter = MyScrapRecyclerviewAdapter2()
            myScrapRecyclerviewAdapter.submitList(mainViewModel.myScrapDataSet)
            with(binding.myRecyclerview) {
                this.layoutManager = layoutManager
                adapter = myScrapRecyclerviewAdapter
                visibility = VISIBLE
            }
            binding.myTxtCount.text = "총 ${mainViewModel.myScrapDataSet!!.size}개"
        }
    }

    private fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            when (view.getId()) {
                R.id.my_radio_btn1 -> {
                        val myScrapRecyclerviewAdapter = MyScrapRecyclerviewAdapter3()
                        myScrapRecyclerviewAdapter.submitList(mainViewModel.myScrapDataSet)
                        with(binding.myRecyclerview) {
                            binding.myRadioBtn2.isChecked = false
                            this.layoutManager = layoutManager
                            adapter = myScrapRecyclerviewAdapter
                        }
                        binding.myTxtCount.text = "총 ${mainViewModel.myScrapDataSet!!.size}개"
                    }
                R.id.my_radio_btn2 -> {
                        val myScrapRecyclerviewAdapter = MyScrapRecyclerviewAdapter2()
                        myScrapRecyclerviewAdapter.submitList(mainViewModel.myScrapDataSet)
                        binding.myRadioBtn1.isChecked = false
                        with(binding.myRecyclerview) {
                            this.layoutManager = layoutManager
                            adapter = myScrapRecyclerviewAdapter
                        }
                        binding.myTxtCount.text = "총 ${mainViewModel.myScrapDataSet!!.size}개"
                    }
            }
        }
    }

}
