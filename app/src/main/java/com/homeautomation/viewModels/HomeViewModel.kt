package com.homeautomation.viewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.homeautomation.Activities.Models.sendObject
import com.homeautomation.Activities.Responses.*
import com.homeautomation.Utils.MqttClientHelper
import com.homeautomation.base.BaseViewModel
import com.homeautomation.showToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttException

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
    lateinit var snackbarMsg : String
    var topic = ""


    var devicesList: ArrayList<GetRoomsResponse.Device> = ArrayList()

    /*Response*/
    var getDevicesResponse = MutableLiveData<GetRoomsResponse>()
    var getRoomsResponse = MutableLiveData<GetRoomsResponse>()
    var mqttListenResponse = MutableLiveData<MqttListenResponse>()

    /*Error*/
    var errorGetDevices = MutableLiveData<Throwable>()
    var errorGetRooms = MutableLiveData<Throwable>()



    fun hitMqttServer(mqttClient: MqttClientHelper,topic: String)
    {
        if (topic.isNotEmpty()) {
            try {
                mqttClient.subscribe(topic)
              //  "Subscribed to topic'$topic'"
            } catch (ex: MqttException) {
                "Error subscribing to topic: $topic"
            }
        }
    }

    fun hitMqttServerPublish(mqttClient: MqttClientHelper,topic: String,sendObject: String)
    {
        if (topic.isNotEmpty()) {
            try {
                mqttClient.publish(topic, sendObject)
                "Published to topic '$topic'"
            } catch (ex: MqttException) {
                "Error Publishing to topic: $topic"
            }
        }
    }


    fun hitGetDevicesApi(isProgress:Boolean) {

        disposable = apiInterface.getDevicesApi(
            userId = userId
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                mProgess.value = true
            }.doOnTerminate {
                    if(!isProgress)
                mProgess.value = false
            }
            .subscribe({ onSuccessGetDevices(it)
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


    fun hitGetRoomsApi(isProgress: Boolean) {

        disposable = apiInterface.getRoomsApi(
            userId = userId
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                mProgess.value = true
            }.doOnTerminate {
                    if(!isProgress)
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