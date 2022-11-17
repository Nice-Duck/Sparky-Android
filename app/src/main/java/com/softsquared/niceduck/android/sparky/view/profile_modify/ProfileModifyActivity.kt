package com.softsquared.niceduck.android.sparky.view.profile_modify

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.softsquared.niceduck.android.sparky.R
import com.softsquared.niceduck.android.sparky.config.ApplicationClass
import com.softsquared.niceduck.android.sparky.databinding.ActivityMyPageBinding
import com.softsquared.niceduck.android.sparky.databinding.ActivityProfileModifyBinding
import com.softsquared.niceduck.android.sparky.utill.BaseActivity
import com.softsquared.niceduck.android.sparky.view.sign_in.SignInActivity
import com.softsquared.niceduck.android.sparky.viewmodel.MyPageViewModel
import kotlinx.coroutines.launch

class ProfileModifyActivity : BaseActivity<ActivityProfileModifyBinding>(ActivityProfileModifyBinding::inflate) {
    lateinit var loadingDlg: Dialog
    var profileImg: String? = ""
    private val myPageViewModel: MyPageViewModel by viewModels()
    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
            result ->
        if (result.resultCode == RESULT_OK) {
            val imageUri = result.data?.data
            imageUri?.let {
                try {
                    // API level 28 이하는 MediaStore.Images.Media.getBitmap 사용 (deprecated)
                    // 그 이상부터 ImageDecoder.createSource 사용
                    val bitmap = it.let {
                        if (Build.VERSION.SDK_INT < 28) {
                            MediaStore.Images
                                .Media.getBitmap(contentResolver, it)
                        } else {
                            val source = ImageDecoder
                                .createSource(contentResolver, it)
                            ImageDecoder.decodeBitmap(source)
                        }
                    }
                    myPageViewModel.image = bitmap
                    Glide.with(this).load(imageUri).transform(
                        CenterCrop(), RoundedCorners(8)
                    ).into(binding.profileModifyImg)

                } catch (e: Exception) {

                    e.let { Log.d("test", e.message.toString())}
                    showCustomToast("이미지를 가져오는데 실패했습니다.")
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        loadingDlg.dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDlg = Dialog(this)
        loadingDlg.setCancelable(false)
        loadingDlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loadingDlg.setContentView(R.layout.dialog_lottie_loading)
        loadingDlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        try {
            profileImg = intent.getStringExtra("img")
            val profileName: String? = intent.getStringExtra("name")
            myPageViewModel.name = profileName?: ""
            binding.profileModifyEditTxt.setText(profileName)

            if (!profileImg.isNullOrEmpty()) {
                Glide.with(this).load(profileImg).centerCrop().into(binding.profileModifyImg)
            }
        } catch (e: Exception) {

        }

        if (binding.profileModifyEditTxt.text.isNotEmpty()) {

            binding.profileModifyEditTxt.backgroundTintList = ColorStateList.valueOf(
                Color.parseColor(
                    "#FF000000"
                )
            )

        }

        binding.profileModifyEditTxt.addTextChangedListener {
            if (binding.profileModifyEditTxt.text.isNotEmpty()) {

                binding.profileModifyEditTxt.backgroundTintList = ColorStateList.valueOf(
                    Color.parseColor(
                        "#FF000000"
                    )
                )

            } else {

                binding.profileModifyEditTxt.backgroundTintList = ColorStateList.valueOf(
                    Color.parseColor(
                        "#BEBDBD"
                    )
                )

            }
        }

        binding.profileModifyImgBack.setOnClickListener {
            finish()
        }

        binding.profileModifyBtnImgModify.setOnClickListener {
            selectGallery()
        }

        binding.profileModifyBtnModify.setOnClickListener {
            myPageViewModel.name = binding.profileModifyEditTxt.text.toString()
            myPageViewModel.patchUser()
            loadingDlg.show()

        }

        myPageViewModel.userUpdateResponse.observe(this) { response ->
            loadingDlg.dismiss()
            when (response.code) {
                "0000" -> {
                    showCustomToast("수정이 완료되었습니다.")
                }
            }
        }

        myPageViewModel.userUpdateFailure.observe(this) {
            Glide.with(this).load(profileImg).transform(
                CenterCrop(), RoundedCorners(8)
            ).into(binding.profileModifyImg)

            when (it.code) {

                "U000" -> {
                    lifecycleScope.launch {
                        myPageViewModel.postReissueAccessToken()
                        myPageViewModel.patchUser()
                    }
                } else -> {
                loadingDlg.dismiss()
                it.message?.let { it1 -> showCustomToast(it1) }
            }
            }
        }

        myPageViewModel.reissueAccessTokenResponse.observe(this) { response ->
            when (response.code) {
                "0000" -> {
                    val newToken = response.result?.accessToken
                    val editor = ApplicationClass.sSharedPreferences.edit()
                    editor.putString(ApplicationClass.X_ACCESS_TOKEN, newToken)
                    editor.apply()
                }
                else -> {
                    showCustomToast("네트워크 연결이 원활하지 않습니다.")
                }
            }

        }

        myPageViewModel.reissueAccessTokenFailure.observe(this) { response ->
            val editor = ApplicationClass.sSharedPreferences.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this, SignInActivity::class.java)
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }



    }

    private fun selectGallery() {
        val readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (readPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        } else {
            val intent = Intent()
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            imageResult.launch(intent)
        }
    }

}