package com.softsquared.niceduck.android.sparky.view.main


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import com.softsquared.niceduck.android.sparky.config.ApplicationClass.Companion.sSharedPreferences
import com.softsquared.niceduck.android.sparky.databinding.ActivityMainBinding
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import com.softsquared.niceduck.android.sparky.view.main.fragment.*
import com.softsquared.niceduck.android.sparky.view.sign_in.SignInActivity
import com.softsquared.niceduck.android.sparky.viewmodel.MainViewModel
import kotlinx.coroutines.*

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        mainViewModel.getMyScrapLoad()
        mainViewModel.getHomeScrapLoad()
        mainViewModel.getUser()
        mainViewModel.getTagLastLoad()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bottomSheetParent = findViewById<LinearLayout>(com.softsquared.niceduck.android.sparky.R.id.bottom_sheet_parent)
        val mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheetParent)
        mBottomSheetBehaviour.peekHeight = 0
        mBottomSheetBehaviour.skipCollapsed = true
        mBottomSheetBehaviour.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // slideOffset 범위: -1.0 ~ 1.0
                // -1.0 HIDDEN, 0.0 COLLAPSED, 1.0 EXPANDED
                if (slideOffset > 0.0) {

                } else {

                }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // state changed
                when(newState) {

                    // 사용자가 BottomSheet를 위나 아래로 드래그 중인 상태
                    BottomSheetBehavior.STATE_DRAGGING -> { }

                    // 드래그 동작 후 BottomSheet가 특정 높이로 고정될 때의 상태
                    // SETTLING 후 EXPANDED, SETTLING 후 COLLAPSED, SETTLING 후 HIDDEN
                    BottomSheetBehavior.STATE_SETTLING -> { }

                    // 최대 높이로 보이는 상태
                    BottomSheetBehavior.STATE_EXPANDED -> { }

                    // peek 높이 만큼 보이는 상태
                    BottomSheetBehavior.STATE_COLLAPSED -> { }

                    // 숨김 상태
                    BottomSheetBehavior.STATE_HIDDEN -> { }
                }
            }
        })



        binding.mainRefresh.setOnRefreshListener {
            mainViewModel.getMyScrapLoad()
            mainViewModel.getHomeScrapLoad()

            lifecycleScope.launch {
                delay(300)
                binding.mainRefresh.isRefreshing = false
            }
        }

        binding.mainImgScrapAddBtn.setOnClickListener {
            val bottomDialogFragment = ScrapAddBottomDialogFragment()
            bottomDialogFragment.show(supportFragmentManager, bottomDialogFragment.tag)
            //mBottomSheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED
        }


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment


        // 토큰 갱신
        mainViewModel.reissueAccessTokenResponse.observe(this) { response ->
            when (response.code) {
                "0000" -> {
                    val newToken = response.result?.accessToken
                    val editor = sSharedPreferences.edit()
                    editor.putString(ApplicationClass.X_ACCESS_TOKEN, newToken)
                    editor.apply()
                }
                else -> {
                }
            }

        }

        mainViewModel.reissueAccessTokenFailure.observe(this) { response ->
            val editor = sSharedPreferences.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this, SignInActivity::class.java)
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(com.softsquared.niceduck.android.sparky.R.id.main_bottomNavigationView)
            .setupWithNavController(navController)
    }
}
