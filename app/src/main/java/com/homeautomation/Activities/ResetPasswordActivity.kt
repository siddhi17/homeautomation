package com.homeautomation.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.homeautomation.R
import com.homeautomation.base.BaseActivity
import com.homeautomation.databinding.ActivityAddDeviceBinding
import com.homeautomation.databinding.ActivityResetPasswordBinding
import com.homeautomation.viewModels.DeviceViewModel

class ResetPasswordActivity : BaseActivity(), View.OnClickListener {
    lateinit var deviceViewModel: DeviceViewModel
    override fun init() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityResetPasswordBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_reset_password)
        binding.data = deviceViewModel
        binding.click = this
    }

    override fun onClick(v: View?) {

    }
}