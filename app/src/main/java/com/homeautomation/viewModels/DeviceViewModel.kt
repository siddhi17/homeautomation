package com.homeautomation.viewModels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.homeautomation.base.BaseViewModel

class DeviceViewModel : BaseViewModel() {

    private lateinit var context: Context
    var mProgess = MutableLiveData<Boolean>()

    var access_token: String = ""

    var isPasswordVisible: Boolean = false

}