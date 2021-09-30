package com.homeautomation.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.homeautomation.Activities.Models.Device
import com.homeautomation.Activities.Responses.GetLocationsResponse
import com.homeautomation.Adapters.LocationsAdapter
import com.homeautomation.R
import com.homeautomation.Utils.ErrorUtil
import com.homeautomation.Utils.NetworkUtils
import com.homeautomation.Utils.ProgressDialogUtils
import com.homeautomation.base.BaseActivity
import com.homeautomation.databinding.ActivityLocationBinding
import com.homeautomation.showToast
import com.homeautomation.viewModels.LocationViewModel
import kotlinx.android.synthetic.main.activity_location.*
import kotlinx.android.synthetic.main.activity_register.*
import java.lang.reflect.Type

class LocationActivity : BaseActivity(), View.OnClickListener {

    lateinit var locationViewModel: LocationViewModel
    var locationsAdapter: LocationsAdapter? = null

    val gson : Gson? = null

    override fun init() {

        val binding: ActivityLocationBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_location)

        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)

        binding.data = locationViewModel
        binding.click = this

        locationViewModel.userId = preference.userId



    /*    locationViewModel.locationsList.add(GetLocationsResponse.Location("","Home",locationViewModel.userId,false))
        locationViewModel.locationsList.add(GetLocationsResponse.Location("","Bungalow",locationViewModel.userId,false))
        locationViewModel.locationsList.add(GetLocationsResponse.Location("","Flat",locationViewModel.userId,false))
        locationViewModel.locationsList.add(GetLocationsResponse.Location("","Office",locationViewModel.userId,false))
        locationViewModel.locationsList.add(GetLocationsResponse.Location("","Workshop",locationViewModel.userId,false))
*/
        /*et_location.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {


            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {


            }
            override fun afterTextChanged(s: Editable) {
                image_location.visibility = View.VISIBLE
            }
        })*/

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)


        init()

        myObserver()
        setLocationsAdapter()

    }

    override fun onResume() {
        super.onResume()
        getLocations()
    }
    //Set Locations Adapter
    private fun setLocationsAdapter() {

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@LocationActivity)
        rv_locations.layoutManager = layoutManager

        if (locationsAdapter == null) {
            locationsAdapter = LocationsAdapter(this, locationViewModel.locationsList,
                    object : LocationsAdapter.LocationListener {
                        override fun clickItem(pos: Int, list: ArrayList<GetLocationsResponse.Location>) {

                            locationsAdapter?.updateStatus()
                            locationViewModel.location = list[pos].locationName.toString()

                            preference.locationId = list[pos].id.toString()
                        }
                    })
            rv_locations.adapter = locationsAdapter
        } else {
            locationsAdapter?.notifyDataSetChanged()
        }

    }
    override fun onClick(v: View?) {

        when(v!!.id)
        {
         /*   R.id.text_add_location -> {

                text_add_location.visibility = View.GONE
                relative_location.visibility = View.VISIBLE

            }
            R.id.image_location -> {

                if (NetworkUtils.isInternetAvailable(this)) {

                    if(locationViewModel.location.toLowerCase().contains("home"))
                        showToast("Location already exist.")
                    else {
                        if (locationViewModel.locationValidation(v)) {
                            locationViewModel.hitAddLocationApi()
                        }
                    }
                }
                else {
                    showToast(getString(R.string.error_internet))
                }
            }*/
            R.id.imageView_back -> {

                finish()
            }
            R.id.btn_proceed -> {

                if(locationViewModel.location == "")
                {
                    showToast(getString(R.string.add_location_error))
                }
                else {
                    startActivity(Intent(this@LocationActivity, RoomsActivity::class.java))
                    finish()
                }
            }
        }

    }
    fun addLocation(){

        if (NetworkUtils.isInternetAvailable(this)) {
            locationViewModel.hitAddLocationApi()
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


        locationViewModel.addLocationResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            if (it.locationId != "") {

            }
            else{

                Toast.makeText(this@LocationActivity,it.error,Toast.LENGTH_LONG).show()
            }

        })

        locationViewModel.errorAddLocation.observe(this, Observer {

            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)

        })


        locationViewModel.getLocationsResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()
            if (it.result == "true") {
                locationViewModel.locationsList.clear()

                it.locations.forEach {

                    if(!locationViewModel.locationsList.map { it.locationName }.contains(it.locationName))
                        locationViewModel.locationsList.add(it)

                }

                setLocationsAdapter()
            }
        })

        locationViewModel.errorGetLocations.observe(this, Observer {

            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)

        })

        locationViewModel.mProgess.observe(this, Observer {
            if (it) {
                ProgressDialogUtils.getInstance().hideProgress()
                ProgressDialogUtils.getInstance().showProgress(this, true)
            } else {
                ProgressDialogUtils.getInstance().hideProgress()
            }
        })

    }
}