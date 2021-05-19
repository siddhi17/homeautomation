package com.homeautomation

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.homeautomation.base.Constants
import java.util.regex.Pattern


var toast: Toast? = null
var gson: Gson? = null

fun getGsonInstance(): Gson {
    if (gson == null)
        gson = Gson()
    return gson!!
}
fun Fragment.showToast(message: String) {
    if (toast != null) toast!!.cancel()
    Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show()
}

fun ViewModel.showToast(context: Context?, message: String) {
    if (toast != null) toast!!.cancel()
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.showToast(message: String) {
    if (toast != null) toast!!.cancel()
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun isValidPasswordFormat(password: String): Boolean {
    val passwordREGEX = Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=.*[@#$%^&+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{8,}" +               //at least 8 characters
            "$");
    return passwordREGEX.matcher(password).matches()
}

val String.isValidEmail: Boolean
    get() = if (this.length < 3 || this.length > 265)
        false
    else {
        this.matches(Constants.EMAIL_PATTERN.toRegex())
    }

fun hideKeyboardFrom(context: Context, view: View) {
    val imm: InputMethodManager =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

