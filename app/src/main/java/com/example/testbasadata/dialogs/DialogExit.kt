package com.example.testbasadata.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment
import com.example.testbasadata.ui.Test


class DialogExit : DialogFragment() {
    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val message = "Ви впевнені, що хочете вийти? "
        val btnYes = "ТАК"
        val btnCancel = "СКАСУВАТИ"
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(message)
        builder.setPositiveButton(
            btnYes
        ) { _, _ ->
            val finish = (activity as Test)
            finish.exitActivity()
            finish.closeDataBase()
//            finish.dialogExitSms()
        }
        builder.setNegativeButton(
            btnCancel
        ) { _, _ ->
        }
        builder.setCancelable(true)
        return builder.create()
    }
}