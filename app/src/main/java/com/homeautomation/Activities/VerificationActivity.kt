package com.homeautomation.Activities

import android.content.Intent
import android.os.BaseBundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.homeautomation.R
import com.homeautomation.Utils.NetworkUtils
import com.homeautomation.base.BaseActivity
import com.homeautomation.databinding.ActivityAddDeviceBinding
import com.homeautomation.databinding.ActivityVerificationBinding
import com.homeautomation.hideKeyboardFrom
import com.homeautomation.showToast
import com.homeautomation.viewModels.DeviceViewModel
import com.homeautomation.viewModels.LoginViewModel
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_verification.*

class VerificationActivity : BaseActivity(), View.OnClickListener {

    lateinit var loginViewModel: LoginViewModel
    override fun init() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityVerificationBinding=
            DataBindingUtil.setContentView(this, R.layout.activity_verification)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.data = loginViewModel
        binding.click = this


        textView_email.text = intent.getStringExtra("emailId")

        startCounter()


        et_otp.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if(s.length == 4)
                {
                    if (NetworkUtils.isInternetAvailable(this@VerificationActivity)) {
                        loginViewModel.access_token = preference.accesToken
                      //  loginViewModel.hitOtpApi()
                        startActivity(Intent(this@VerificationActivity,ResetPasswordActivity::class.java))

                    } else {
                        showToast(getString(R.string.error_internet))
                    }
                }
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

            }
        })

    }
    private fun startCounter() {
        object : CountDownTimer(31000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.d("seconds remaining: ", (millisUntilFinished / 1000).toString())

                textView_resend_otp.text = getString(R.string.resendOtp) + " (" + (millisUntilFinished / 1000).toString() + ")s"
                textView_email.visibility = View.VISIBLE
                btn_resend.visibility = View.GONE
            }

            override fun onFinish() {
                // Called after timer finishes
                btn_resend.visibility = View.VISIBLE
                textView_resend.visibility = View.GONE
                textView_resend_otp.visibility = View.GONE
                textView_email.visibility = View.GONE
            }
        }.start()

    }
    override fun onClick(v: View?) {
        when(v!!.id) {


            R.id.btn_resend -> {

                hideKeyboardFrom(this, v)

                if (NetworkUtils.isInternetAvailable(this)) {

                    textView_resend.text = getString(R.string.resend) + intent.getStringExtra("emailId")

                     //   loginViewModel.hitLoginApi()

                } else {
                    showToast(getString(R.string.error_internet))
                }

            }
        }

    }
}