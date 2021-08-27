package com.example.testbasadata.ui.message_recycler_view.view_holder

import com.example.testbasadata.ui.message_recycler_view.views.MessageView

interface MessageHolder {
    fun drawMessage(view: MessageView)
    fun onAttach(view: MessageView)
    fun onDetach()
}