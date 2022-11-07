package com.softsquared.niceduck.android.sparky.view.main

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import com.softsquared.niceduck.android.sparky.config.ApplicationClass.Companion.sSharedPreferences
import com.softsquared.niceduck.android.sparky.databinding.ActivityMainBinding
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import com.softsquared.niceduck.android.sparky.view.main.fragment.*
import com.softsquared.niceduck.android.sparky.view.scrap.ScrapBottomDialogFragment
import com.softsquared.niceduck.android.sparky.view.sign_in.SignInActivity
import com.softsquared.niceduck.android.sparky.viewmodel.MainViewModel
import kotlinx.coroutines.*

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        mainViewModel.getMyScrapLoad()
        mainViewModel.getHomeScrapLoad()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.mainRefresh.setOnRefreshListener {
            mainViewModel.getMyScrapLoad()
            mainViewModel.getHomeScrapLoad()

            CoroutineScope(Dispatchers.Main).launch {
                delay(300)
                binding.mainRefresh.isRefreshing = false
            }

        }

        binding.mainImgScrapAddBtn.setOnClickListener {
            val bottomDialogFragment = ScrapAddBottomDialogFragment()
            bottomDialogFragment.show(supportFragmentManager, bottomDialogFragment.tag)
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
                    showCustomToast("네트워크 연결이 원활하지 않습니다.")
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
        findViewById<BottomNavigationView>(R.id.main_bottomNavigationView)
            .setupWithNavController(navController)
    }
}
