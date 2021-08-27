package com.example.testbasadata.ui.message_recycler_view.view_holder

import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.testbasadata.database.CURRENT_UID
import com.example.testbasadata.database.getFileFromStorage
import com.example.testbasadata.ui.message_recycler_view.views.MessageView
import com.example.testbasadata.utilits.WRITE_FILES
import com.example.testbasadata.utilits.asTime
import com.example.testbasadata.utilits.checkPermission
import com.example.testbasadata.utilits.showToast
import kotlinx.android.synthetic.main.message_item_file.view.*
import java.io.File

class HolderFileMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {

    private val blockReceivedFileMessage: ConstraintLayout = view.block_received_file_message
    private val blockUserFileMessage: ConstraintLayout = view.block_user_file_message
    private val chatUserFileMessageTime: TextView = view.chat_user_file_time
    private val chatReceivedFileMessageTime: TextView = view.chat_received_file_time

    private val chatUserFileName: TextView = view.chat_user_file_name
    private val chatUserBtnDownload: ImageView = view.chat_user_btn_download
    private val chatUserProgressBar: ProgressBar = view.progressBarUserFile

    private val chatReceivedFileName: TextView = view.chat_received_file_name
    private val chatReceivedBtnDownload: ImageView = view.chat_received_btn_download
    private val chatReceivedProgressBar: ProgressBar = view.progressBarReceivedFile


    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blockReceivedFileMessage.visibility = View.GONE
            blockUserFileMessage.visibility = View.VISIBLE
            chatUserFileMessageTime.text =
                view.timeStamp.asTime()
            chatUserFileName.text = view.text

        } else {
            blockReceivedFileMessage.visibility = View.VISIBLE
            blockUserFileMessage.visibility = View.GONE
            chatReceivedFileMessageTime.text =
                view.timeStamp.asTime()
            chatReceivedFileName.text = view.text
        }
    }

    override fun onAttach(view: MessageView) {
        if (view.from == CURRENT_UID) chatUserBtnDownload.setOnClickListener { clickToBtnFile(view) }
        else chatReceivedBtnDownload.setOnClickListener {clickToBtnFile(view) }

    }

    private fun clickToBtnFile(view: MessageView) {
        if (view.from == CURRENT_UID) {
            chatUserBtnDownload.visibility = View.INVISIBLE
            chatUserProgressBar.visibility = View.VISIBLE
        } else {
            chatReceivedBtnDownload.visibility = View.INVISIBLE
            chatReceivedProgressBar.visibility = View.VISIBLE
        }
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            view.text
        )
        try {
            if (checkPermission(WRITE_FILES)) {
                file.createNewFile()
                getFileFromStorage(file, view.fileUrl) {
                    if (view.from == CURRENT_UID) {
                        chatUserBtnDownload.visibility = View.VISIBLE
                        chatUserProgressBar.visibility = View.INVISIBLE
                    } else {
                        chatReceivedBtnDownload.visibility = View.VISIBLE
                        chatReceivedProgressBar.visibility = View.INVISIBLE
                    }
                }
            }
        } catch (e:Exception){
            showToast(e.message.toString())
        }
    }

    override fun onDetach() {
        chatUserBtnDownload.setOnClickListener(null)
        chatReceivedBtnDownload.setOnClickListener(null)

    }
}

