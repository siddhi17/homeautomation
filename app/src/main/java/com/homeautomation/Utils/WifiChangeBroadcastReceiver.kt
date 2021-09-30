package com.homeautomation.Utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class WifiChangeBroadcastReceiver(private val listener: WifiChangeBroadcastListener) :
    BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        listener.onWifiChangeBroadcastReceived(context, intent)
    }

    interface WifiChangeBroadcastListener {
        fun onWifiChangeBroadcastReceived(context: Context?, intent: Intent?)
    }
}
