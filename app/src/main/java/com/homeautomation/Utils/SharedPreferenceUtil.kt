package com.homeautomation.Utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.homeautomation.base.Constants

class SharedPreferenceUtil
constructor(val context: Context) {
    private val TAG = SharedPreferenceUtil::class.java.simpleName

    private val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(Constants.PREFRENCE_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var instance: SharedPreferenceUtil? = null

        fun getInstance(ctx: Context): SharedPreferenceUtil {
            if (instance == null) {
                instance = SharedPreferenceUtil(ctx)
            }
            return instance!!
        }
    }


    var accesToken: String
        get() = sharedPreferences["AccessToken", ""]!!
        set(value) = sharedPreferences.set("AccessToken", value)

    var userId: String
        get() = sharedPreferences["UserId", ""]!!
        set(value) = sharedPreferences.set("UserId", value)

    var userEmailId: String
        get() = sharedPreferences["UserEmailId", ""]!!
        set(value) = sharedPreferences.set("UserEmailId", value)

    var userName: String
        get() = sharedPreferences["UserName", ""]!!
        set(value) = sharedPreferences.set("UserName", value)

    var isLogin: Boolean
        get() = sharedPreferences["IsLogin", false]!!
        set(value) = sharedPreferences.set("IsLogin", value)

    var is_mobileVerified: Boolean
        get() = sharedPreferences["is_mobileVerified", false]!!
        set(value) = sharedPreferences.set("is_mobileVerified", value)

    var userImage: String
        get() = sharedPreferences["UserImage", ""]!!
        set(value) = sharedPreferences.set("UserImage", value)

    var mobileNumber: String
        get() = sharedPreferences["MobileNumber", ""]!!
        set(value) = sharedPreferences.set("MobileNumber", value)

    var userPermission: String
        get() = sharedPreferences["userPermission", ""]!!
        set(value) = sharedPreferences.set("userPermission", value)

    var oldPassword: String
        get() = sharedPreferences["oldPassword", ""]!!
        set(value) = sharedPreferences.set("oldPassword", value)

    var firstName: String
        get() = sharedPreferences["firstName", ""]!!
        set(value) = sharedPreferences.set("firstName", value)

    var lastName: String
        get() = sharedPreferences["lastName"]!!
        set(value) = sharedPreferences.set("lastName", value)

    var locationCounter: Int
        get() = sharedPreferences["locationCounter"]!!
        set(value) = sharedPreferences.set("locationCounter", value)

    var roomCounter: Int
        get() = sharedPreferences["roomCounter"]!!
        set(value) = sharedPreferences.set("roomCounter", value)

    var deviceId: String
        get() = sharedPreferences["deviceId"]!!
        set(value) = sharedPreferences.set("deviceId", value)

    var locationId: String
        get() = sharedPreferences["locationId"]!!
        set(value) = sharedPreferences.set("locationId", value)

    var roomId: String
        get() = sharedPreferences["roomId"]!!
        set(value) = sharedPreferences.set("roomId", value)

    var areLocationsAdded: Boolean
        get() = sharedPreferences["areLocationsAdded"]!!
        set(value) = sharedPreferences.set("areLocationsAdded", value)

    var areRoomsAdded: Boolean
        get() = sharedPreferences["areRoomsAdded"]!!
        set(value) = sharedPreferences.set("areRoomsAdded", value)

    var deviceName: String
        get() = sharedPreferences["deviceName"]!!
        set(value) = sharedPreferences.set("deviceName", value)


    fun userPermissionArray(array: ArrayList<Boolean?>, arrayName: String, mContext: Context): Boolean {
        val prefs = mContext.getSharedPreferences("userPermissionArray", 0)
        val editor = prefs.edit()
        editor.putInt(arrayName + "_size", array.size)
        for (i in array.indices) editor.putBoolean(arrayName + "_" + i, array[i]!!)
        return editor.commit()
    }

    fun loadArray(arrayName: String, mContext: Context): Array<Boolean?>? {
        val prefs: SharedPreferences = mContext.getSharedPreferences("userPermissionArray", 0)
        val size = prefs.getInt(arrayName + "_size", 0)
        val array = arrayOfNulls<Boolean>(size)
        for (i in 0 until size) array[i] = prefs.getBoolean(arrayName + "_" + i, false)
        return array
    }

    /**
     * puts a key value pair in shared prefs if doesn't exists, otherwise updates value on given [key]
     */
    operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String? -> edit { it.putString(key, value) }
            is Int -> edit { it.putInt(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Long -> edit { it.putLong(key, value) }
            else -> Log.e(TAG, "Setting shared pref failed for key: $key and value: $value ")
        }
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    /**
     * finds value on given key.
     * [T] is the type of value
     * @param defaultValue optional default value - will take null for strings, false for bool and -1 for numeric values if [defaultValue] is not specified
     */
    inline operator fun <reified T : Any> SharedPreferences.get(
        key: String,
        defaultValue: T? = null
    ): T? {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    fun deletePreferences() {
        editor.clear()
        editor.apply()
    }
}