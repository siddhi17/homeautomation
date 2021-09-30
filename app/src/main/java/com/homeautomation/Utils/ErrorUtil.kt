package com.homeautomation.Utils

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.homeautomation.Activities.Models.ErrorBean
import com.homeautomation.Activities.RegisterActivity
import com.homeautomation.R
import com.homeautomation.R.string
import com.homeautomation.getGsonInstance
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorUtil {
    val TAG = ErrorUtil::class.simpleName

    fun handlerGeneralError(context: Context?, throwable: Throwable) {
        Log.e(TAG, "Error: ${throwable.message}")
        throwable.printStackTrace()

        if (context == null) return

        when (throwable) {
            //For Display Toast

            is ConnectException -> Toast.makeText(
                context,
                "Network Error PLease Try Later ",
                Toast.LENGTH_SHORT
            ).show()
            is SocketTimeoutException -> Toast.makeText(
                context,
                "Connection Lost PLease Try Later",
                Toast.LENGTH_SHORT
            ).show()
            is UnknownHostException,is InternalError -> Toast.makeText(
                context,
                "Server Error PLease Try Later",
                Toast.LENGTH_SHORT
            ).show()

            is HttpException -> {
                try {
                    when (throwable.code()) {
                        401 -> {
                            //Logout
                            //forceLogout(context)
                        }
                        500 -> {
                            //Logout
                            //forceLogout(context)

                            Toast.makeText(
                                    context,
                                    "Server Error PLease Try Later",
                                    Toast.LENGTH_SHORT
                            ).show()

                        }
                        403 -> {
                            ProgressDialogUtils.getInstance().hideProgress()
                            displayError(context, throwable)
                        }
                        else -> {

                            displayError(context, throwable)
                        }
                    }
                } catch (exception: Exception) {
                    Log.e("error",exception.toString())
                }
            }
            else -> {
                Toast.makeText(
                    context, context.resources.getString(string.something_went_wrong),
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }


//    ** Perform logout for both the success and error case (force logout)

/*    private fun forceLogout(context: Context) {

        SharedPreferenceUtil.getInstance(context).deletePreferences()
        context.apply {
            startActivity(
                Intent(context, RegisterActivity::class.java)
                .putExtra("IsTokenExpired",true))

            Toast.makeText(
                    context, getString(R.string.access_token_missing),
                    Toast.LENGTH_SHORT
            ).show()
        }

    }*/

    fun displayError(context: Context, exception: HttpException) {
        Log.i(TAG, "displayError()")
        try {
            val errorBody = getGsonInstance().fromJson(
                exception.response()!!.errorBody()?.charStream(),
                ErrorBean::class.java
            )
            //  SnackbarUtils.displaySnackbar(view, errorBody.message)

            Toast.makeText(context,errorBody.message,Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            Log.e("MyExceptions", e.message!!)
            Toast.makeText(
                context, context.getString(string.error_exception),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}