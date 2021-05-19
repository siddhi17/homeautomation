package com.homeautomation.viewModels

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.homeautomation.Activities.Models.AddLocation
import com.homeautomation.Activities.Responses.*
import com.homeautomation.R
import com.homeautomation.base.BaseViewModel
import com.homeautomation.isValidEmail
import com.homeautomation.showToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LocationViewModel : BaseViewModel() {

    @SuppressLint("StaticFieldLeak")
    private lateinit var context: Context
    var mProgess = MutableLiveData<Boolean>()
    private lateinit var disposable: Disposable
    var access_token: String = ""

    var location: String = ""
    var userId: String = ""
    var locationsList: ArrayList<GetLocationsResponse.Location> = ArrayList()

    /*Response*/
    var addLocationResponse = MutableLiveData<AddLocationResponse>()
    var getLocationsResponse = MutableLiveData<GetLocationsResponse>()
    var getUserResponse = MutableLiveData<GetUserResponse>()

    /*Error*/
    var errorAddLocation = MutableLiveData<Throwable>()
    var errorGetLocations = MutableLiveData<Throwable>()
    var errorGetUser = MutableLiveData<Throwable>()


    fun hitGetLocationsApi() {

        disposable = apiInterface.getLocationsApi(
                userId = userId
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    mProgess.value = true
                }.doOnTerminate {
                    mProgess.value = false
                }
                .subscribe({
                    onSuccessGetLocations(it)
                },
                        {
                            onErrorGetLocations(it)
                        })
    }

    private fun onSuccessGetLocations(it: GetLocationsResponse) {

        getLocationsResponse.value = it
    }

    private fun onErrorGetLocations(it: Throwable) {
        errorGetLocations.value = it
    }


    fun locationValidation(v: View): Boolean {
        context = v.context
        if (location.isEmpty()) {
            showToast(context, context.getString(R.string.add_location_error))
            return false
        }
        return true
    }


    fun hitAddLocationApi() {

        disposable = apiInterface.addLocationApi(
                addLocation = AddLocation(location,userId)
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    mProgess.value = true
                }.doOnTerminate {
                    mProgess.value = false
                }
                .subscribe({
                    onSuccessAddLocation(it)
                },
                        {
                            onErrorAddLocation(it)
                        })
    }

    private fun onSuccessAddLocation(it: AddLocationResponse) {

        addLocationResponse.value = it
    }

    private fun onErrorAddLocation(it: Throwable) {
        errorAddLocation.value = it
    }

}