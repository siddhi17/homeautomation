package com.homeautomation.viewModels

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.homeautomation.Activities.Models.User
import com.homeautomation.Activities.Responses.GetUserResponse
import com.homeautomation.Activities.Responses.LoginResponse
import com.homeautomation.Activities.Responses.RegisterResponse
import com.homeautomation.R
import com.homeautomation.Utils.AlertDialogUtil
import com.homeautomation.base.BaseViewModel
import com.homeautomation.isValidEmail
import com.homeautomation.isValidPasswordFormat
import com.homeautomation.showToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel: BaseViewModel() {

    @SuppressLint("StaticFieldLeak")
    private lateinit var context: Context
    var mProgess = MutableLiveData<Boolean>()
    private lateinit var disposable: Disposable
    var access_token: String = ""

    var emailId: String = ""
    var firstname: String = ""
    var lastName: String = ""
    var pass: String = ""
    var confirmPass: String = ""
    var isTermsSelected: Boolean = false
    var isPasswordVisible: Boolean = false

    /*Response*/
    var registerResponse = MutableLiveData<RegisterResponse>()
    var loginResponse = MutableLiveData<LoginResponse>()
    var getUserResponse = MutableLiveData<GetUserResponse>()

    /*Error*/
    var errorRegister = MutableLiveData<Throwable>()
    var errorLogin = MutableLiveData<Throwable>()
    var errorGetUser = MutableLiveData<Throwable>()


    fun hitGetUserApi() {

        disposable = apiInterface.getUserApi(
                email = emailId
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    mProgess.value = true
                }.doOnTerminate {
                    mProgess.value = false
                }
                .subscribe({
                    onSuccessGetUser(it)
                },
                        {
                            onErrorGetUser(it)
                        })
    }

    private fun onSuccessGetUser(it: GetUserResponse) {

        getUserResponse.value = it
    }

    private fun onErrorGetUser(it: Throwable) {
        errorGetUser.value = it
    }



    fun registerValidation(v: View): Boolean {
        context = v.context
        if (emailId.isEmpty()) {
            showToast(context, context.getString(R.string.email_id_err))
            return false
        }
        if (firstname.isEmpty()) {
            showToast(context, context.getString(R.string.first_name_error))
            return false
        }
        if (lastName.isEmpty()) {
            showToast(context, context.getString(R.string.last_name_error))
            return false
        }
        if (pass.isEmpty()) {
            showToast(context, context.getString(R.string.pass_err))
            return false
        }
        if (!emailId.isValidEmail) {
            showToast(context, context.getString(R.string.email_id_valid_error))
            return false
        }

        if (confirmPass.isEmpty()) {
            showToast(context, context.getString(R.string.confirm_password_error))
            return false
        }
        if (pass != confirmPass) {
            showToast(context, context.getString(R.string.password_confirm_does_not_match_error))
            return false
        }
        if (!isTermsSelected) {
            showToast(context, context.getString(R.string.terms_error))
            return false
        }
        return true
    }

    fun hitRegisterApi() {

        disposable = apiInterface.registerApi(
              user = User(firstname,lastName,emailId,pass)
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                mProgess.value = true
            }.doOnTerminate {
                mProgess.value = false
            }
            .subscribe({
                onSuccessSignUp(it)
            },
                {
                    onErrorSignUp(it)
                })
    }

    private fun onSuccessSignUp(it: RegisterResponse) {

        registerResponse.value = it
    }

    private fun onErrorSignUp(it: Throwable) {
        errorRegister.value = it
    }



    fun loginValidation(v: View): Boolean {
        context = v.context
        if (emailId.isEmpty()) {
            showToast(context, context.getString(R.string.email_id_err))
            return false
        }
        if (pass.isEmpty()) {
            showToast(context, context.getString(R.string.pass_err))
            return false
        }
        if (!emailId.isValidEmail) {
            showToast(context, context.getString(R.string.email_id_valid_error))
            return false
        }
        return true
    }

    fun hitLoginApi() {

        disposable = apiInterface.loginApi(
            email = emailId,
            pwd = pass
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                mProgess.value = true
            }.doOnTerminate {
                mProgess.value = false
            }
            .subscribe({
                onSuccessLogin(it)
            },
                {
                    onErrorLogin(it)
                })
    }

    private fun onSuccessLogin(it: LoginResponse) {

        loginResponse.value = it
    }

    private fun onErrorLogin(it: Throwable) {
        errorLogin.value = it
    }


}