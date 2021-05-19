package com.homeautomation.Activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.homeautomation.Activities.Responses.GetLocationsResponse
import com.homeautomation.Adapters.LocationsAdapter
import com.homeautomation.R
import com.homeautomation.Utils.ErrorUtil
import com.homeautomation.Utils.NetworkUtils
import com.homeautomation.Utils.ProgressDialogUtils
import com.homeautomation.base.BaseActivity
import com.homeautomation.databinding.ActivityLocationBinding
import com.homeautomation.databinding.ActivityRegisterBinding
import com.homeautomation.isValidPasswordFormat
import com.homeautomation.showToast
import com.homeautomation.viewModels.LocationViewModel
import com.homeautomation.viewModels.LoginViewModel
import kotlinx.android.synthetic.main.activity_location.*
import kotlinx.android.synthetic.main.activity_register.*

class LocationActivity : BaseActivity(), View.OnClickListener {

    lateinit var locationViewModel: LocationViewModel
    var locationsAdapter: LocationsAdapter? = null


    override fun init() {

        val binding: ActivityLocationBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_location)

        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)

        binding.data = locationViewModel
        binding.click = this

        locationViewModel.userId = preference.userId

        et_location.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {


            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {


            }
            override fun afterTextChanged(s: Editable) {
                image_location.visibility = View.VISIBLE
            }
        })

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)


        init()
        myObserver()
        getLocations()
        setLocationsAdapter()

    }
    //Set Locations Adapter
    private fun setLocationsAdapter() {

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@LocationActivity)
        rv_locations.layoutManager = layoutManager

        if (locationsAdapter == null) {
            locationsAdapter = LocationsAdapter(this, locationViewModel.locationsList,
                    object: LocationsAdapter.LocationListener{
                        override fun clickItem(pos: Int, list: ArrayList<GetLocationsResponse.Location>) {
                            startActivity(Intent(this@LocationActivity,RoomsActivity::class.java)
                                    .putExtra("locationId",list[pos].id))
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
            R.id.text_add_location -> {

                text_add_location.visibility = View.GONE
                relative_location.visibility = View.VISIBLE

            }
            R.id.image_location -> {

                if (NetworkUtils.isInternetAvailable(this)) {

                    if (locationViewModel.locationValidation(v)) {
                        locationViewModel.hitAddLocationApi()
                    }
                }
                else {
                    showToast(getString(R.string.error_internet))
                }
            }
            R.id.imageView_back -> {

                finish()
            }
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

            if (it.locationId.isEmpty())
                showToast("Location Already Exist")
            else
                getLocations()

            et_location.clearFocus()
            et_location.setText("")
            image_location.visibility = View.GONE

        })

        locationViewModel.errorAddLocation.observe(this, Observer {

            ProgressDialogUtils.getInstance().hideProgress()
            ErrorUtil.handlerGeneralError(this, it)

        })


        locationViewModel.getLocationsResponse.observe(this, Observer {
            ProgressDialogUtils.getInstance().hideProgress()

            if(it.result == "true") {
                locationViewModel.locationsList.clear()
                locationViewModel.locationsList.add(0,GetLocationsResponse.Location("1","Home"
                        ,""))
                locationViewModel.locationsList.addAll(it.locations)

                setLocationsAdapter()
            }
            else
            {
                locationViewModel.locationsList.add(0,GetLocationsResponse.Location("1","Home"
                ,""))
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