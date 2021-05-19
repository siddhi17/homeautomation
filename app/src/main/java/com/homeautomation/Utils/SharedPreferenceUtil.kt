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

    var mobileNumberCountryCode: String
        get() = sharedPreferences["MobileNumberCountryCode", ""]!!
        set(value) = sharedPreferences.set("MobileNumberCountryCode", value)


    var userPermission: String
        get() = sharedPreferences["userPermission", ""]!!
        set(value) = sharedPreferences.set("userPermission", value)

    var oldPassword: String
        get() = sharedPreferences["oldPassword", ""]!!
        set(value) = sharedPreferences.set("oldPassword", value)

    var defaultLocation: String
        get() = sharedPreferences["defaultLocation", ""]!!
        set(value) = sharedPreferences.set("defaultLocation", value)

    var isStoreAdded: Boolean
        get() = sharedPreferences["isStoreAdded"]!!
        set(value) = sharedPreferences.set("isStoreAdded", value)

    var city: String
        get() = sharedPreferences["city", ""]!!
        set(value) = sharedPreferences.set("city", value)

    var isStoreUser: String
        get() = sharedPreferences["isStoreUser", ""]!!
        set(value) = sharedPreferences.set("isStoreUser", value)

    var isVisibleDash: Boolean
        get() = sharedPreferences["isVisibleDash"]!!
        set(value) = sharedPreferences.set("isVisibleDash", value)

    var canSeeUpcomingRequestDash: Boolean
        get() = sharedPreferences["canSeeUpcomingRequestDash"]!!
        set(value) = sharedPreferences.set("canSeeUpcomingRequestDash", value)

    var canApproveRejectRequestDash: Boolean
        get() = sharedPreferences["canApproveRejectRequestDash"]!!
        set(value) = sharedPreferences.set("canApproveRejectRequestDash", value)

    var canViewOrderRequestDash: Boolean
        get() = sharedPreferences["canViewOrderRequestDash"]!!
        set(value) = sharedPreferences.set("canViewOrderRequestDash", value)

    var isVisibleProduct: Boolean
        get() = sharedPreferences["isVisibleProduct"]!!
        set(value) = sharedPreferences.set("isVisibleProduct", value)

    var viewItemProduct: Boolean
        get() = sharedPreferences["viewItemProduct"]!!
        set(value) = sharedPreferences.set("viewItemProduct", value)

    var addItemProduct: Boolean
        get() = sharedPreferences["addItemProduct"]!!
        set(value) = sharedPreferences.set("addItemProduct", value)

    var editItemProduct: Boolean
        get() = sharedPreferences["editItemProduct"]!!
        set(value) = sharedPreferences.set("editItemProduct", value)

    var deleteItemProduct: Boolean
        get() = sharedPreferences["deleteItemProduct"]!!
        set(value) = sharedPreferences.set("deleteItemProduct", value)

    var isVisibleReport: Boolean
        get() = sharedPreferences["isVisibleReport"]!!
        set(value) = sharedPreferences.set("isVisibleReport", value)

    var canExportItem: Boolean
        get() = sharedPreferences["canExportItem"]!!
        set(value) = sharedPreferences.set("canExportItem", value)

    var showQrCode: Boolean
        get() = sharedPreferences["showQrCode"]!!
        set(value) = sharedPreferences.set("showQrCode", value)

    var isVisibleOrder: Boolean
        get() = sharedPreferences["isVisibleOrder"]!!
        set(value) = sharedPreferences.set("isVisibleOrder", value)

    var generateInvoiceOrder: Boolean
        get() = sharedPreferences["generateInvoiceOrder"]!!
        set(value) = sharedPreferences.set("generateInvoiceOrder", value)

    var isVisibleDelivery: Boolean
        get() = sharedPreferences["isVisibleDelivery"]!!
        set(value) = sharedPreferences.set("isVisibleDelivery", value)

    var isVisibleInventory: Boolean
        get() = sharedPreferences["isVisibleInventory"]!!
        set(value) = sharedPreferences.set("isVisibleInventory", value)

    var isUpdateInventory: Boolean
        get() = sharedPreferences["isUpdateInventory"]!!
        set(value) = sharedPreferences.set("isUpdateInventory", value)

    var isVisibleFinance: Boolean
        get() = sharedPreferences["isVisibleFinance"]!!
        set(value) = sharedPreferences.set("isVisibleFinance", value)

    var canExportItemFinance: Boolean
        get() = sharedPreferences["canExportItemFinance"]!!
        set(value) = sharedPreferences.set("canExportItemFinance", value)

    var isVisibleOffer: Boolean
        get() = sharedPreferences["isVisibleOffer"]!!
        set(value) = sharedPreferences.set("isVisibleOffer", value)

    var viewItemOffer: Boolean
        get() = sharedPreferences["viewItemOffer"]!!
        set(value) = sharedPreferences.set("viewItemOffer", value)

    var addItemOffer: Boolean
        get() = sharedPreferences["addItemOffer"]!!
        set(value) = sharedPreferences.set("addItemOffer", value)

    var editItemOffer: Boolean
        get() = sharedPreferences["editItemOffer"]!!
        set(value) = sharedPreferences.set("editItemOffer", value)

    var deleteItemOffer: Boolean
        get() = sharedPreferences["deleteItemOffer"]!!
        set(value) = sharedPreferences.set("deleteItemOffer", value)

    var changeStatusOffer: Boolean
        get() = sharedPreferences["changeStatusOffer"]!!
        set(value) = sharedPreferences.set("changeStatusOffer", value)

    var isVisibleBranch: Boolean
        get() = sharedPreferences["isVisibleBranch"]!!
        set(value) = sharedPreferences.set("isVisibleBranch", value)

    var addItemBranch: Boolean
        get() = sharedPreferences["addItemBranch"]!!
        set(value) = sharedPreferences.set("addItemBranch", value)

    var editItemBranch: Boolean
        get() = sharedPreferences["editItemBranch"]!!
        set(value) = sharedPreferences.set("editItemBranch", value)

    var deleteItemBranch: Boolean
        get() = sharedPreferences["deleteItemBranch"]!!
        set(value) = sharedPreferences.set("deleteItemBranch", value)

    var isVisibleCategory: Boolean
        get() = sharedPreferences["isVisibleCategory"]!!
        set(value) = sharedPreferences.set("isVisibleCategory", value)

    var addItemCategory: Boolean
        get() = sharedPreferences["addItemCategory"]!!
        set(value) = sharedPreferences.set("addItemCategory", value)

    var editItemCategory: Boolean
        get() = sharedPreferences["editItemCategory"]!!
        set(value) = sharedPreferences.set("editItemCategory", value)

    var deleteItemCategory: Boolean
        get() = sharedPreferences["deleteItemCategory"]!!
        set(value) = sharedPreferences.set("deleteItemCategory", value)

    var isVisibleRole: Boolean
        get() = sharedPreferences["isVisibleRole"]!!
        set(value) = sharedPreferences.set("isVisibleRole", value)

    var addItemRole: Boolean
        get() = sharedPreferences["addItemRole"]!!
        set(value) = sharedPreferences.set("addItemRole", value)

    var editItemRole: Boolean
        get() = sharedPreferences["editItemRole"]!!
        set(value) = sharedPreferences.set("editItemRole", value)

    var deleteItemRole: Boolean
        get() = sharedPreferences["deleteItemRole"]!!
        set(value) = sharedPreferences.set("deleteItemRole", value)

    var viewItemRole: Boolean
        get() = sharedPreferences["viewItemRole"]!!
        set(value) = sharedPreferences.set("viewItemRole", value)

    var isVisibleUser: Boolean
        get() = sharedPreferences["isVisibleUser"]!!
        set(value) = sharedPreferences.set("isVisibleUser", value)

    var addItemUser: Boolean
        get() = sharedPreferences["addItemUser"]!!
        set(value) = sharedPreferences.set("addItemUser", value)

    var editItemUser: Boolean
        get() = sharedPreferences["editItemUser"]!!
        set(value) = sharedPreferences.set("editItemUser", value)

    var deleteItemUser: Boolean
        get() = sharedPreferences["deleteItemUser"]!!
        set(value) = sharedPreferences.set("deleteItemUser", value)


    var scanQrCodeDriver: Boolean
        get() = sharedPreferences["scanQrCodeDriver"]!!
        set(value) = sharedPreferences.set("scanQrCodeDriver", value)


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