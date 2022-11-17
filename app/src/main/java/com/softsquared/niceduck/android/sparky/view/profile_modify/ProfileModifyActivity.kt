package com.softsquared.niceduck.android.sparky.view.profile_modify

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.ActivityMyPageBinding
import com.softsquared.niceduck.android.sparky.databinding.ActivityProfileModifyBinding
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import com.softsquared.niceduck.android.sparky.viewmodel.MyPageViewModel

class ProfileModifyActivity : BaseActivity<ActivityProfileModifyBinding>(ActivityProfileModifyBinding::inflate) {
    lateinit var loadingDlg: Dialog
    private val myPageViewModel: MyPageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.profileModifyImgBack.setOnClickListener {
            finish()
        }

        binding.profileModifyBtnModify.setOnClickListener {
           // 닉네임 중복 확인했던 것 그대로!
        }



    }
}