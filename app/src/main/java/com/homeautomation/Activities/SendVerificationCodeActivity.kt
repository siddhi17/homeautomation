package com.homeautomation.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.homeautomation.R
import com.homeautomation.Utils.NetworkUtils
import com.homeautomation.base.BaseActivity
import com.homeautomation.databinding.ActivitySendVerificationCodeBinding
import com.homeautomation.hideKeyboardFrom
import com.homeautomation.showToast
import com.homeautomation.viewModels.LoginViewModel

class SendVerificationCodeActivity : BaseActivity(), View.OnClickListener {

    lateinit var loginViewModel: LoginViewModel
    override fun init() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding: ActivitySendVerificationCodeBinding=
            DataBindingUtil.setContentView(this, R.layout.activity_send_verification_code)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.data = loginViewModel
        binding.click = this
    }

    override fun onClick(v: View?) {
        when(v!!.id) {

            R.id.btn_proceed -> {

                hideKeyboardFrom(this, v)

                if (NetworkUtils.isInternetAvailable(this)) {

                    loginViewModel.pass = "a"

                    if (loginViewModel.loginValidation(v))
                        startActivity(Intent(this@SendVerificationCodeActivity,VerificationActivity::class.java)
                                .putExtra("emailId",loginViewModel.emailId))
                    //   loginViewModel.hitLoginApi()

                } else {
                    showToast(getString(R.string.error_internet))
                }

            }

            R.id.textView_cancel -> {

                finishAffinity()
                startActivity(Intent(this,LoginActivity::class.java))
            }
        }
    }
}