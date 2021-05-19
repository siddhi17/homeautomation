package com.homeautomation.Activities

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.homeautomation.Activities.Fragments.HomeFragment
import com.homeautomation.R
import com.homeautomation.Utils.ErrorUtil
import com.homeautomation.Utils.NetworkUtils
import com.homeautomation.Utils.ProgressDialogUtils
import com.homeautomation.base.BaseActivity
import com.homeautomation.showToast
import com.homeautomation.viewModels.DeviceViewModel
import com.homeautomation.viewModels.LoginViewModel

class MainActivity : BaseActivity(), View.OnClickListener {

    var userName: String = ""

    override fun init() {


        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_scheduler, R.id.navigation_favourites, R.id.navigation_settings))
      //  setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

      //  loadFragment(HomeFragment())

    }

    lateinit var loginViewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        init()
        getUser()
        myObserver()

    }

    private fun getUser(){


        if (NetworkUtils.isInternetAvailable(this)) {
            loginViewModel.emailId = preference.userEmailId
                loginViewModel.hitGetUserApi()
        } else {
            showToast(getString(R.string.error_internet))
        }
    }

    private fun myObserver() {

        loginViewModel.getUserResponse.observe(this, Observer {

            preference.userName = it.user.firstName + " " + it.user.lastName
            preference.userId = it.user.id
            preference.userEmailId = it.user.email

            userName = it.user.firstName + " " + it.user.lastName

        })

        loginViewModel.errorGetUser.observe(this, Observer {

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


    }
}