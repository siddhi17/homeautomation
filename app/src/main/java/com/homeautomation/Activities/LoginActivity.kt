package com.homeautomation.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.homeautomation.R
import com.homeautomation.Utils.ErrorUtil
import com.homeautomation.Utils.NetworkUtils
import com.homeautomation.Utils.ProgressDialogUtils
import com.homeautomation.base.BaseActivity
import com.homeautomation.databinding.ActivityLoginBinding
import com.homeautomation.hideKeyboardFrom
import com.homeautomation.showToast
import com.homeautomation.viewModels.LoginViewModel
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : BaseActivity(), View.OnClickListener {

    lateinit var loginViewModel: LoginViewModel
    
    override fun init() {

        val binding: ActivityLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.data = loginViewModel
        binding.click = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        myObserver()

    }


    private fun myObserver() {

        loginViewModel.loginResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()

            preference.userEmailId = loginViewModel.emailId

            if(it.result == "false")
                showToast("Incorrect Credentials. Please enter correct email id and password.")
            else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        })

        loginViewModel.errorRegister.observe(this, Observer {

            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)

        })

        loginViewModel.mProgess.observe(this, Observer {
            if (it) {
                ProgressDialogUtils.getInstance().hideProgress()
                ProgressDialogUtils.getInstance().showProgress(this, true)
            } else {
                ProgressDialogUtils.getInstance().hideProgress()
            }
        })

    }

    override fun onClick(v: View?) {

        when(v!!.id)
        {

            R.id.btn_proceed ->
            {

                hideKeyboardFrom(this, v)

                if (NetworkUtils.isInternetAvailable(this)) {

                    if (loginViewModel.loginValidation(v))
                        loginViewModel.hitLoginApi()
                } else {
                    showToast(getString(R.string.error_internet))
                }

            }
            R.id.textView_register ->
            {

                startActivity(Intent(this,RegisterActivity::class.java))

            }
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


        }

    }
}