package com.homeautomation.base


import android.view.View
import androidx.fragment.app.Fragment
import com.homeautomation.Utils.NetworkUtils
import com.homeautomation.Utils.SharedPreferenceUtil

abstract class BaseFragment : Fragment(){


    abstract fun init()

    val preference by lazy {
        SharedPreferenceUtil.getInstance(requireContext())
    }

    val checkInternet by lazy {
        NetworkUtils.isInternetAvailable(requireContext())
    }

}