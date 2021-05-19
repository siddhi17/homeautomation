package com.homeautomation.base

import android.content.Context
import androidx.lifecycle.ViewModel
import com.storepanel.webservice.ApiInterface
import com.storepanel.webservice.RetrofitUtil


abstract class BaseViewModel : ViewModel() {

    val apiInterface: ApiInterface by lazy {
        RetrofitUtil.createBaseApiService()
    }
}