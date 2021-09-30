package com.homeautomation.Utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.widget.Toast

class WifiReceiver(var wifiManager: WifiManager) :
    BroadcastReceiver() {
    var sb: StringBuilder? = null
    private var message: Message? = null

    override fun onReceive(context: Context?, intent: Intent) {
        val action = intent.action

        if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION == action) {
            sb = StringBuilder()
            val wifiList: List<ScanResult> = wifiManager.scanResults
            val deviceList: ArrayList<String> = ArrayList()
            for (scanResult in wifiList) {
                sb!!.append("\n").append(scanResult.SSID).append(" - ")
                    .append(scanResult.capabilities)
                deviceList.add(scanResult.SSID.toString())
            }
            message?.getMsg(deviceList);
        }
    }
    interface Message {
        fun getMsg(arrayList: ArrayList<String>)
    }

    fun setMessage(message: Message?) {
        this.message = message
    }
}