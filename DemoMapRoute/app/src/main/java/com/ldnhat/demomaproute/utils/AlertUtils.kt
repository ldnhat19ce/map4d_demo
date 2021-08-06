package com.ldnhat.demomaproute.utils

import android.app.Activity
import androidx.appcompat.app.AlertDialog

class AlertUtils {

    companion object{
        fun showAlertDialog(activity : Activity, title : Int, content : Int) {

            val builder: AlertDialog.Builder = AlertDialog.Builder(activity)

            builder.setTitle(title)
            builder.setMessage(content)
            builder.setPositiveButton("Đồng ý") { dialog, which -> dialog.cancel() }

            val create: AlertDialog = builder.create()
            create.setCancelable(false)
            create.show()
        }
    }
}