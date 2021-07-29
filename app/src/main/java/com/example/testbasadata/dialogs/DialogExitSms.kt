package com.example.testbasadata.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment
import java.util.*


class DialogExitSms : DialogFragment() {
    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val message = "Ви вийшли з облікового запису "
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(message)
        builder.setCancelable(true)

        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                timer.cancel() // also just top the timer thread, otherwise,
                // you may receive a crash report
            }
        }, 5000) // через 5 секунд (5000 миллисекунд), the task will be active.
        return builder.create()
    }
}
//
//fun onClick(v: View) {
//    val builder = AlertDialog.Builder(v.getContext())
//    builder.setTitle("Автоматическое закрытие окна")
//    builder.setMessage("Через пять секунд это окно закроется автоматически!")
//    builder.setCancelable(true)
//    val dlg = builder.create()
//    dlg.show()
//    val timer = Timer()
//    timer.schedule(object : TimerTask() {
//        override fun run() {
//            dlg.dismiss() // when the task active then close the dialog
//            timer.cancel() // also just top the timer thread, otherwise,
//            // you may receive a crash report
//        }
//    }, 5000) // через 5 секунд (5000 миллисекунд), the task will be active.
//}