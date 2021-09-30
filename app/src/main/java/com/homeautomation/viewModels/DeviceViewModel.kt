package com.homeautomation.viewModels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.homeautomation.Activities.Models.AddDevice
import com.homeautomation.Activities.Models.Device
import com.homeautomation.Activities.Responses.*
import com.homeautomation.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DeviceViewModel : BaseViewModel() {

    private lateinit var context: Context
    var mProgess = MutableLiveData<Boolean>()
    private lateinit var disposable: Disposable
    var wifiDeviceList: ArrayList<String> = ArrayList()
    var wifiSSId: String = ""
    var password: String = ""

    var access_token: String = ""

    var switchesList : ArrayList<AddDevice.Switche> = ArrayList()
    var locationId: String = ""
    var roomId: String = ""
    var deviceId: String = ""
    var userId: String = ""
    var deviceName: String = ""

    var isPasswordVisible: Boolean = false

    /*Response*/
    var getDeviceDetailsResponse = MutableLiveData<GetDeviceDetailsResponse>()
    var getNetworksResponse = MutableLiveData<GetNetworks>()
    var getConnectionStatusResponse = MutableLiveData<GetConnectionStatusResponse>()
    var restartDeviceResponse = MutableLiveData<RestartDeviceResponse>()
    var createDeviceResponse = MutableLiveData<AddDeviceResponse>()

    /*Error*/
    var errorGetDeviceDetails = MutableLiveData<Throwable>()
    var errorGetNetworks = MutableLiveData<Throwable>()
    var errorGetConnectionStatus = MutableLiveData<Throwable>()
    var errorRestartDevice = MutableLiveData<Throwable>()
    var errorCreateDevice = MutableLiveData<Throwable>()

    fun hitCreateDeviceApi() {

        disposable = apiInterface.createDeviceApi(
                device = AddDevice(deviceId,deviceName,userId,roomId,locationId,switchesList)
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    mProgess.value = true
                }.doOnTerminate {
                    mProgess.value = false
                }
                .subscribe({
                    onSuccessCreateDevice(it)
                },
                        {
                            onErrorCreateDevice(it)
                        })
    }

    private fun onSuccessCreateDevice(it: AddDeviceResponse) {

        createDeviceResponse.value = it
    }

    private fun onErrorCreateDevice(it: Throwable) {
        errorCreateDevice.value = it
    }

    fun hitRestartDeviceApi() {

        disposable = apiInterface.restartDeviceApi().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    mProgess.value = true
                }.doOnTerminate {
                    mProgess.value = false
                }
                .subscribe({
                    onSuccessRestartDevice(it)
                },
                        {
                            onErrorRestartDevice(it)
                        })
    }

    private fun onSuccessRestartDevice(it: RestartDeviceResponse) {

        restartDeviceResponse.value = it
    }

    private fun onErrorRestartDevice(it: Throwable) {
        errorRestartDevice.value = it
    }

    fun hitSetNetworkApi() {
        disposable = apiInterface.setNetworkApi(wifiSSId,password).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    mProgess.value = true
                }.doOnTerminate {
                    mProgess.value = false
                }
                .subscribe({},
                        {})
    }

    fun hitGetConnectionStatusApi() {

        disposable = apiInterface.getConnectionStatusApi().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    mProgess.value = true
                }.doOnTerminate {
                    mProgess.value = false
                }
                .subscribe({
                    onSuccessGetConnectionStatus(it)
                },
                        {
                            onErrorGetConnectionStatus(it)
                        })
    }

    private fun onSuccessGetConnectionStatus(it: GetConnectionStatusResponse) {

        getConnectionStatusResponse.value = it
    }

    private fun onErrorGetConnectionStatus(it: Throwable) {
        errorGetConnectionStatus.value = it
    }

    fun hitGetDeviceDetailsApi() {

        disposable = apiInterface.getDeviceDetailsApi().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                mProgess.value = true
            }.doOnTerminate {
                mProgess.value = false
            }
            .subscribe({
                onSuccessGetDeviceDetailsApi(it)
            },
                {
                    onErrorGetDeviceDetails(it)
                })
    }

    private fun onSuccessGetDeviceDetailsApi(it: GetDeviceDetailsResponse) {

        getDeviceDetailsResponse.value = it
    }

    private fun onErrorGetDeviceDetails(it: Throwable) {
        errorGetDeviceDetails.value = it
    }


    fun hitGetNetworksApi() {

        disposable = apiInterface.getNetworksApi().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                mProgess.value = true
            }.doOnTerminate {
                mProgess.value = false
            }
            .subscribe({
                onSuccessGetNetworks(it)
            },
                {
                    onErrorGetNetworks(it)
                })
    }

    private fun onSuccessGetNetworks(it: GetNetworks) {

        getNetworksResponse.value = it
    }

    private fun onErrorGetNetworks(it: Throwable) {
        errorGetNetworks.value = it
    }

}