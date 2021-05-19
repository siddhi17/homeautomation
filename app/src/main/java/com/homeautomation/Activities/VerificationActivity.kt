package com.homeautomation.Activities

import android.os.BaseBundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.homeautomation.R
import com.homeautomation.base.BaseActivity
import com.homeautomation.databinding.ActivityAddDeviceBinding
import com.homeautomation.databinding.ActivityVerificationBinding
import com.homeautomation.viewModels.DeviceViewModel

class VerificationActivity : BaseActivity(), View.OnClickListener {

    lateinit var deviceViewModel: DeviceViewModel
    override fun init() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityVerificationBinding=
            DataBindingUtil.setContentView(this, R.layout.activity_verification)

        binding.data = deviceViewModel
        binding.click = this
    }

    override fun onClick(v: View?) {

    }
}