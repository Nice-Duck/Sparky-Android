package com.softsquared.niceduck.android.sparky.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import com.softsquared.niceduck.android.sparky.view.main.MainActivity
import com.softsquared.niceduck.android.sparky.view.sign_in.SignInActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)


        if (ApplicationClass.sSharedPreferences.getString(ApplicationClass.X_ACCESS_TOKEN, null) != null) {
            Log.d("토큰", "${ApplicationClass.sSharedPreferences.getString(ApplicationClass.X_ACCESS_TOKEN, null)}")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
        finish()
    }
}