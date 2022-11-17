package com.softsquared.niceduck.android.sparky.view.inquiry

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.databinding.ActivityInquiryBinding
import com.softsquared.niceduck.android.sparky.databinding.ActivityTagListBinding
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import com.softsquared.niceduck.android.sparky.viewmodel.MyPageViewModel

class InquiryActivity : BaseActivity<ActivityInquiryBinding>(ActivityInquiryBinding::inflate) {
    lateinit var loadingDlg: Dialog
    private val myPageViewModel: MyPageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadingDlg = Dialog(this)
        loadingDlg.setCancelable(false)
        loadingDlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loadingDlg.setContentView(R.layout.dialog_lottie_loading)
        loadingDlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}