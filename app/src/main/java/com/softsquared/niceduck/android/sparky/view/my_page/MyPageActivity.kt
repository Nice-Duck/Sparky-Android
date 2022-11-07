package com.softsquared.niceduck.android.sparky.view.my_page

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import com.softsquared.niceduck.android.sparky.databinding.ActivityMyPageBinding
import com.softsquared.niceduck.android.sparky.databinding.ActivityMyScrapDetailBinding
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import com.softsquared.niceduck.android.sparky.view.sign_in.SignInActivity

class MyPageActivity : BaseActivity<ActivityMyPageBinding>(ActivityMyPageBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.myPageImgBack.setOnClickListener {
            finish()
        }

        binding.myPageLLLogout.setOnClickListener {
            val editor = ApplicationClass.sSharedPreferences.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this, SignInActivity::class.java)
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }


    }
}