package com.example.testbasadata.ui.message_recycler_view.view_holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.testbasadata.database.CURRENT_UID
import com.example.testbasadata.ui.message_recycler_view.views.MessageView
import com.example.testbasadata.utilits.asTime
import com.example.testbasadata.utilits.downloadAndSetImage
import kotlinx.android.synthetic.main.message_item_image.view.*

class HolderImageMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {
    private val blockReceivedImageMessage: CardView = view.block_received_image
    private val blockUserImageMessage: CardView = view.block_user_image
    private val chatUserImage: ImageView = view.chat_user_image
    private val chatReceivedImage: ImageView = view.chat_received_image
    private val chatUserImageMessageTime: TextView = view.chat_user_image_time
    private val chatReceivedImageMessageTime: TextView = view.chat_received_image_time
    override fun drawMessage(view: MessageView) {

        if (view.from == CURRENT_UID) {
            blockReceivedImageMessage.visibility = View.GONE
            blockUserImageMessage.visibility = View.VISIBLE
            chatUserImage.downloadAndSetImage(view.fileUrl)
            chatUserImageMessageTime.text =
                view.timeStamp.asTime()

        } else {

            blockReceivedImageMessage.visibility = View.VISIBLE
            blockUserImageMessage.visibility = View.GONE
            chatReceivedImage.downloadAndSetImage(view.fileUrl)
            chatReceivedImageMessageTime.text =
                view.timeStamp.asTime()
        }
    }

    override fun onAttach(view: MessageView) {

    }

    override fun onDetach() {

    }
}