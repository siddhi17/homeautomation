package com.homeautomation.Activities

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
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
import com.homeautomation.databinding.ActivityRegisterBinding
import com.homeautomation.hideKeyboardFrom
import com.homeautomation.isValidPasswordFormat
import com.homeautomation.showToast
import com.homeautomation.viewModels.LoginViewModel
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : BaseActivity(), View.OnClickListener {

    lateinit var loginViewModel: LoginViewModel

    override fun init() {

        val binding: ActivityRegisterBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_register)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.data = loginViewModel
        binding.click = this

        checkbox_terms.text = ""
        text_policy.text = Html.fromHtml("I agree with " +
                "<a href='http://www.kushall.com/privacy-policy.html'> Privacy Policy </a>")
        text_policy.isClickable = true
        text_policy.setLinkTextColor(Color.WHITE)
        text_policy.setMovementMethod(LinkMovementMethod.getInstance())

        text_terms.text = Html.fromHtml(" &  " + "<a href ='http://www.kushall.com/terms-condition.html'>Terms Of Use </a>")
        text_terms.isClickable = true
        text_terms.setLinkTextColor(Color.WHITE)
        text_terms.setMovementMethod(LinkMovementMethod.getInstance())

        checkbox_terms.setOnCheckedChangeListener { buttonView, isChecked ->

            loginViewModel.isTermsSelected = isChecked

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        myObserver()

    }

    private fun myObserver() {

        loginViewModel.registerResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()

            if (it.id.isEmpty())
                showToast("User Already Exist")
            else {
                val intent = Intent(this, LoginActivity::class.java)
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

        when(v!!.id){

            R.id.btn_proceed -> {

                hideKeyboardFrom(this, v)

                if (NetworkUtils.isInternetAvailable(this)) {

                    if (loginViewModel.registerValidation(v)) {
                        if (!isValidPasswordFormat(loginViewModel.pass)) {
                            text_error.visibility = View.VISIBLE
                            text_.visibility = View.INVISIBLE
                            text_error.setBackgroundResource(R.drawable.error_background)
                            text_error.text = getString(R.string.password_error)
                            text_error.setTextColor(Color.BLACK)
                        }
                        else
                            loginViewModel.hitRegisterApi()
                    }

                } else {
                    showToast(getString(R.string.error_internet))
                }

            }
            R.id.textView_cancel -> {
                finish()
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
           /* R.id.text_terms -> {

                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.kushall.com/tos"))
                startActivity(browserIntent)
            }
            R.id.text_policy -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.kushall.com/pp"))
                startActivity(browserIntent)

            }*/
        }
    }
}