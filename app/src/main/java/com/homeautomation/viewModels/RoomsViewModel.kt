package com.homeautomation.viewModels

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.homeautomation.Activities.Models.AddLocation
import com.homeautomation.Activities.Models.AddRoom
import com.homeautomation.Activities.Responses.AddLocationResponse
import com.homeautomation.Activities.Responses.AddRoomResponse
import com.homeautomation.Activities.Responses.GetLocationsResponse
import com.homeautomation.Activities.Responses.GetUserResponse
import com.homeautomation.R
import com.homeautomation.base.BaseViewModel
import com.homeautomation.showToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class RoomsViewModel: BaseViewModel() {

    @SuppressLint("StaticFieldLeak")
    private lateinit var context: Context
    var mProgess = MutableLiveData<Boolean>()
    private lateinit var disposable: Disposable
    var access_token: String = ""

    var roomName: String = ""
    var roomId: String = ""
    var userId: String = ""
    var locationId: String = ""

    /*Response*/
    var addRoomResponse = MutableLiveData<AddRoomResponse>()


    /*Error*/
    var errorAddRoom = MutableLiveData<Throwable>()



    fun roomValidation(v: View): Boolean {
        context = v.context
        if (roomName.isEmpty()) {
            showToast(context, context.getString(R.string.add_room_error))
            return false
        }
        return true
    }


    fun hitAddRoomApi() {

        disposable = apiInterface.addRoomApi(
                addRoom = AddRoom(roomName,userId,locationId)
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    mProgess.value = true
                }.doOnTerminate {
                    mProgess.value = false
                }
                .subscribe({
                    onSuccessAddRoom(it)
                },
                        {
                            onErrorAddRoom(it)
                        })
    }

    private fun onSuccessAddRoom(it: AddRoomResponse) {

        addRoomResponse.value = it
    }

    private fun onErrorAddRoom(it: Throwable) {
        errorAddRoom.value = it
    }

}