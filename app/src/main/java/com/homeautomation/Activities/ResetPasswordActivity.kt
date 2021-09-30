package com.homeautomation.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.homeautomation.R
import com.homeautomation.Utils.NetworkUtils
import com.homeautomation.base.BaseActivity
import com.homeautomation.databinding.ActivityAddDeviceBinding
import com.homeautomation.databinding.ActivityResetPasswordBinding
import com.homeautomation.hideKeyboardFrom
import com.homeautomation.showToast
import com.homeautomation.viewModels.DeviceViewModel
import com.homeautomation.viewModels.LoginViewModel
import kotlinx.android.synthetic.main.activity_register.*

class ResetPasswordActivity : BaseActivity(), View.OnClickListener {
    lateinit var loginViewModel: LoginViewModel
    override fun init() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityResetPasswordBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_reset_password)


        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.data = loginViewModel
        binding.click = this
    }

    override fun onClick(v: View?) {

        when(v!!.id) {


            R.id.imageView_show -> {

                if (loginViewModel.isPasswordVisible) {
                    loginViewModel.isPasswordVisible = false
                    etPassword.transformationMethod = null
                    imageView_show.setImageResource(R.drawable.ic_show)
                } else {
                    loginViewModel.isPasswordVisible = true
                    etPassword.transformationMethod =
                            PasswordTransformationMethod.getInstance()
                    imageView_show.setImageResource(R.drawable.ic_hide)
                }
            }

            R.id.imageView_confirm_show -> {

                if (loginViewModel.isPasswordVisible) {
                    loginViewModel.isPasswordVisible = false
                    et_confirm_Password.transformationMethod = null
                    imageView_confirm_show.setImageResource(R.drawable.ic_show)
                } else {
                    loginViewModel.isPasswordVisible = true
                    et_confirm_Password.transformationMethod =
                            PasswordTransformationMethod.getInstance()
                    imageView_confirm_show.setImageResource(R.drawable.ic_hide)
                }
            }

            R.id.btn_reset ->
            {

                hideKeyboardFrom(this, v)

                if (NetworkUtils.isInternetAvailable(this)) {

                    if (loginViewModel.resetPasswordValidation(v))
                        startActivity(Intent(this@ResetPasswordActivity,RegisterActivity::class.java))
                   //     loginViewModel.hitLoginApi()
                } else {
                    showToast(getString(R.string.error_internet))
                }

            }
            R.id.textView_cancel ->
            {
                finishAffinity()
                startActivity(Intent(this,LoginActivity::class.java))
            }
        }
    }
}