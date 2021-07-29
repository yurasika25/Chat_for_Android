package com.example.testbasadata.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.fragment.app.DialogFragment


class DialogConfirm : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val textViewRegister = TextView(activity)
            builder
                .setMessage("Перейдіть за посиланням, яке було відправлено на вашу пошту і підтвердіть адресу електронної пошти")
                .setPositiveButton("ОК") { dialog, _ ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}