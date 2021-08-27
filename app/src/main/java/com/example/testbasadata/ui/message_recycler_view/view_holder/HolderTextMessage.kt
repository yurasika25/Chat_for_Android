package com.example.testbasadata.ui.message_recycler_view.view_holder

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.testbasadata.database.CURRENT_UID
import com.example.testbasadata.ui.message_recycler_view.views.MessageView
import com.example.testbasadata.utilits.asTime
import kotlinx.android.synthetic.main.message_item_text.view.*

class HolderTextMessage(view: View): RecyclerView.ViewHolder(view), MessageHolder {
    private val blockUserMessage: ConstraintLayout = view.block_user_message
    private val chatUserMessage: TextView = view.chat_user_massage
    private val chatUserMessageTime: TextView = view.chat_user_massage_time
    private val blocReceivedMessage: ConstraintLayout = view.block_user_message_two
    private val chatReceivedMessage: TextView = view.chat_user_message_two
    private val chatReceivedMessageTime: TextView = view.chat_user_message_time_two


    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blockUserMessage.visibility = View.VISIBLE
            blocReceivedMessage.visibility = View.GONE
            chatUserMessage.text = view.text
            chatUserMessageTime.text =
                view.timeStamp.asTime()
        } else {
            blockUserMessage.visibility = View.GONE
            blocReceivedMessage.visibility = View.VISIBLE
            chatReceivedMessage.text = view.text
            chatReceivedMessageTime.text =
                view.timeStamp.asTime()
        }
    }


    override fun onAttach(view: MessageView) {

    }

    override fun onDetach() {
    }
}

