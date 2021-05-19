package com.homeautomation.Utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.homeautomation.R


class AlertDialogUtil {
    companion object {
        fun showSimpleAlertDialog(
            context: Context?,
            message: String?,
            isCancelable: Boolean
        ): AlertDialog {
            var dialogView: View? =
                LayoutInflater.from(context).inflate(R.layout.message_dialog, null)
            var tv_alert_message: TextView? =dialogView?.findViewById(R.id.tvHeading)

            tv_alert_message?.text=message

            var builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setView(dialogView)
            val alertDialog: AlertDialog = builder.create()
            alertDialog?.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);

            alertDialog.setCancelable(isCancelable)
            DialogInterface.OnClickListener { dialog, which -> alertDialog.dismiss() }
            alertDialog.show()
            return alertDialog
        }
    }

}