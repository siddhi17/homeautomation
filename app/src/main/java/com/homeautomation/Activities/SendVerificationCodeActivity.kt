package com.homeautomation.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.homeautomation.R
import com.homeautomation.base.BaseActivity
import com.homeautomation.databinding.ActivityAddDeviceBinding
import com.homeautomation.databinding.ActivitySendVerificationCodeBinding
import com.homeautomation.viewModels.DeviceViewModel

class SendVerificationCodeActivity : BaseActivity(), View.OnClickListener {

    lateinit var deviceViewModel: DeviceViewModel
    override fun init() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding: ActivitySendVerificationCodeBinding=
            DataBindingUtil.setContentView(this, R.layout.activity_send_verification_code)

        binding.data = deviceViewModel
        binding.click = this
    }

    override fun onClick(v: View?) {

    }
}