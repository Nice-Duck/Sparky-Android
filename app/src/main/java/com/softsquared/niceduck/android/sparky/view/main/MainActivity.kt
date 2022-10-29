package com.softsquared.niceduck.android.sparky.view.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.ActivityMainBinding
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import com.softsquared.niceduck.android.sparky.view.main.fragment.MyScrapRecyclerviewAdapter
import com.softsquared.niceduck.android.sparky.view.main.fragment.MyScrapRecyclerviewAdapter2
import com.softsquared.niceduck.android.sparky.view.main.fragment.MyScrapRecyclerviewAdapter3
import com.softsquared.niceduck.android.sparky.view.main.fragment.OthersScrapRecyclerviewAdapter
import com.softsquared.niceduck.android.sparky.viewmodel.MainViewModel

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.main_bottomNavigationView)
            .setupWithNavController(navController)
    }
}
