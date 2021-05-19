package com.homeautomation.viewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.homeautomation.Activities.Responses.*
import com.homeautomation.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel : BaseViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    @SuppressLint("StaticFieldLeak")
    private lateinit var context: Context
    var mProgess = MutableLiveData<Boolean>()
    private lateinit var disposable: Disposable
    var access_token: String = ""

    var userId: String = ""

    /*Response*/
    var getDevicesResponse = MutableLiveData<GetRoomsResponse>()
    var getRoomsResponse = MutableLiveData<GetRoomsResponse>()

    /*Error*/
    var errorGetDevices = MutableLiveData<Throwable>()
    var errorGetRooms = MutableLiveData<Throwable>()


    fun hitGetDevicesApi() {

        disposable = apiInterface.getDevicesApi(
            userId = userId
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                mProgess.value = true
            }.doOnTerminate {
                mProgess.value = false
            }
            .subscribe({
                onSuccessGetDevices(it)
            },
                {
                    onErrorGetDevices(it)
                })
    }

    private fun onSuccessGetDevices(it: GetRoomsResponse) {

        getDevicesResponse.value = it
    }

    private fun onErrorGetDevices(it: Throwable) {
        errorGetDevices.value = it
    }


    fun hitGetRoomsApi() {

        disposable = apiInterface.getRoomsApi(
            userId = userId
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                mProgess.value = true
            }.doOnTerminate {
                mProgess.value = false
            }
            .subscribe({
                onSuccessGetRooms(it)
            },
                {
                    onErrorGetRooms(it)
                })
    }

    private fun onSuccessGetRooms(it: GetRoomsResponse) {

        getRoomsResponse.value = it
    }

    private fun onErrorGetRooms(it: Throwable) {
        errorGetRooms.value = it
    }

}