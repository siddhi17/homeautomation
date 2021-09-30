package com.homeautomation.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.homeautomation.Activities.Responses.GetLocationsResponse
import com.homeautomation.Activities.Responses.GetRoomsResponse
import com.homeautomation.R
import com.homeautomation.Utils.ErrorUtil
import com.homeautomation.Utils.NetworkUtils
import com.homeautomation.Utils.ProgressDialogUtils
import com.homeautomation.base.BaseActivity
import com.homeautomation.databinding.ActivityLoginBinding
import com.homeautomation.hideKeyboardFrom
import com.homeautomation.showToast
import com.homeautomation.viewModels.HomeViewModel
import com.homeautomation.viewModels.LocationViewModel
import com.homeautomation.viewModels.LoginViewModel
import com.homeautomation.viewModels.RoomsViewModel
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : BaseActivity(), View.OnClickListener {

    lateinit var loginViewModel: LoginViewModel
    lateinit var homeViewModel: HomeViewModel
    lateinit var locationViewModel: LocationViewModel
    lateinit var roomsViewModel: RoomsViewModel

    var locationsList: ArrayList<GetLocationsResponse.Location> = ArrayList()
    var roomList: ArrayList<GetRoomsResponse.Room> = ArrayList()

    override fun init() {

        val binding: ActivityLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)

        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        loginViewModel =
            ViewModelProvider(this).get(LoginViewModel::class.java)
        locationViewModel =
            ViewModelProvider(this).get(LocationViewModel::class.java)
        roomsViewModel = ViewModelProvider(this).get(RoomsViewModel::class.java)

        homeViewModel.userId = preference.userId
        locationViewModel.userId = preference.userId

        binding.data = loginViewModel
        binding.click = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        myObserver()

    }

    fun getRooms(){
        if (NetworkUtils.isInternetAvailable(this)) {
            homeViewModel.hitGetRoomsApi(true)
        }
        else {
            showToast(getString(R.string.error_internet))
        }
    }

    fun getLocations(){
        if (NetworkUtils.isInternetAvailable(this)) {
            locationViewModel.hitGetLocationsApi()
        }
        else {
            showToast(getString(R.string.error_internet))
        }
    }


    private fun myObserver() {

        loginViewModel.loginResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()


            preference.userEmailId = loginViewModel.emailId

            if(it.result == "true") {

                getUser()

            }
            else {

                showToast("Incorrect Credentials. Please enter correct email id and password.")
            }

        })

        loginViewModel.errorLogin.observe(this, Observer {

            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)

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

        loginViewModel.getUserResponse.observe(this, androidx.lifecycle.Observer {

            preference.firstName = it.user.firstName
            preference.lastName = it.user.lastName
            preference.userId = it.user.id
            preference.userEmailId = it.user.email

            homeViewModel.userId = preference.userId
            locationViewModel.userId = preference.userId

            if(!preference.areLocationsAdded)
                getLocations()
            else if(!preference.areRoomsAdded)
                getRooms()

        })

        loginViewModel.errorGetUser.observe(this, androidx.lifecycle.Observer {

            ErrorUtil.handlerGeneralError(this, it)
        })

        locationViewModel.addLocationResponse.observe(this, androidx.lifecycle.Observer {
            ProgressDialogUtils.getInstance().hideProgress()

            if(it.locationId != "")
            {
                preference.locationCounter++

                if (preference.locationCounter == 4) {

                    //   preference.areLocationsAdded = true
                        if(!preference.areRoomsAdded)
                            addRooms()
                }
            }
        })

        locationViewModel.getLocationsResponse.observe(this, androidx.lifecycle.Observer {
            ProgressDialogUtils.getInstance().hideProgress()


            loginViewModel.mProgess.value = true

            preference.areLocationsAdded = it.result != "false"

            if(!preference.areLocationsAdded)
                    addLocations()
            else{
                loginViewModel.mProgess.value = false
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        homeViewModel.getRoomsResponse.observe(this, androidx.lifecycle.Observer {
            ProgressDialogUtils.getInstance().hideProgress()

            preference.areRoomsAdded = it.result != "false"

        })
        locationViewModel.errorGetLocations.observe(this, androidx.lifecycle.Observer  {
            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)
        })

        homeViewModel.errorGetRooms.observe(this, androidx.lifecycle.Observer  {
            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)
        })


        locationViewModel.errorAddLocation.observe(this, androidx.lifecycle.Observer  {
            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)
        })

        roomsViewModel.addRoomResponse.observe(this, androidx.lifecycle.Observer {
            ProgressDialogUtils.getInstance().hideProgress()

            if(it.roomId != "")
            {
                preference.roomCounter++

                if (preference.roomCounter == 18) {

                    //    preference.areRoomsAdded = true
                    loginViewModel.mProgess.value = false
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        })


        roomsViewModel.errorAddRoom.observe(this, androidx.lifecycle.Observer  {
            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)
        })
    }
    private fun getUser() {


        if (NetworkUtils.isInternetAvailable(this)) {
            loginViewModel.emailId = preference.userEmailId
            loginViewModel.hitGetUserApi()
        } else {
            showToast(getString(R.string.error_internet))
        }
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
            R.id.textView_forgot_pass ->
            {
                startActivity(Intent(this,SendVerificationCodeActivity::class.java))
            }
        }

    }
    fun addLocations()
    {

        locationsList.add(GetLocationsResponse.Location("", "Home", preference.userId, false))
        locationsList.add(GetLocationsResponse.Location("", "Bungalow", preference.userId, false))
        locationsList.add(GetLocationsResponse.Location("", "Flat", preference.userId, false))
        locationsList.add(GetLocationsResponse.Location("", "Office", preference.userId, false))
        locationsList.add(GetLocationsResponse.Location("", "Workshop", preference.userId, false))
        for (location in locationsList)
        {
            //   if(locationsList.indexOf(location) > preference.locationCounter) {
            if (NetworkUtils.isInternetAvailable(this)) {
                locationViewModel.location = location.locationName.toString()
                locationViewModel.userId = preference.userId
                locationViewModel.hitAddLocationApi()
            } else {
                showToast(getString(R.string.error_internet))
            }
            //  }
            // else if(preference.locationCounter == 5)
            //  addRooms()
        }
        if(preference.locationCounter == 4)
            addRooms()
    }

    fun addRooms()
    {
        val device: List<GetRoomsResponse.Device> = ArrayList()

        roomList.add(GetRoomsResponse.Room("","Balcony","","",device,false, R.drawable.ic_balcony))
        roomList.add(GetRoomsResponse.Room("","Backyard","","",device,false, R.drawable.ic_backyard))
        roomList.add(GetRoomsResponse.Room("","Basement","","",device,false, R.drawable.ic_basement))
        roomList.add(GetRoomsResponse.Room("","Bedroom","","",device,false, R.drawable.ic_bed_room))

        roomList.add(GetRoomsResponse.Room("","Common Area","","",device,false, R.drawable.ic_common_area))
        roomList.add(GetRoomsResponse.Room("","Conference Room","","",device,false, R.drawable.ic_conference_room))
        roomList.add(GetRoomsResponse.Room("","Corridor","","",device,false, R.drawable.ic_corridor))

        roomList.add(GetRoomsResponse.Room("","Dinning Room","","",device,false, R.drawable.ic_dinniing_room))
        roomList.add(GetRoomsResponse.Room("","Gate","","",device,false, R.drawable.ic_gate))
        roomList.add(GetRoomsResponse.Room("","Hall","","",device,false, R.drawable.ic_hall))
        roomList.add(GetRoomsResponse.Room("","Kitchen","","",device,false, R.drawable.ic_kitchen))

        roomList.add(GetRoomsResponse.Room("","Living Room","","",device,false, R.drawable.ic_living_room))
        roomList.add(GetRoomsResponse.Room("","Office Cabin","","",device,false, R.drawable.ic_office_cabin))
        roomList.add(GetRoomsResponse.Room("","Out Door","","",device,false, R.drawable.ic_outdoor))

        roomList.add(GetRoomsResponse.Room("","Stairs","","",device,false, R.drawable.ic_stairs))
        roomList.add(GetRoomsResponse.Room("","Stair Way","","",device,false, R.drawable.ic_stairway))
        roomList.add(GetRoomsResponse.Room("","Store Room","","",device,false, R.drawable.ic_store_room))
        roomList.add(GetRoomsResponse.Room("","Study Room","","",device,false, R.drawable.ic_study_room))
        roomList.add(GetRoomsResponse.Room("","Wash Room","","",device,false, R.drawable.ic_washroom))

        for (room in roomList)
        {
            //    if(roomList.indexOf(room) > preference.roomCounter-1) {
            if (NetworkUtils.isInternetAvailable(this)) {
                roomsViewModel.roomName = room.roomName.toString()
                roomsViewModel.userId = preference.userId
                roomsViewModel.hitAddRoomApi()
            } else {
                showToast(getString(R.string.error_internet))
            }
            // }
        }

    }


}