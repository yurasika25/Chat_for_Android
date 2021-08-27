package com.example.testbasadata.screens.mainlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testbasadata.R
import com.example.testbasadata.models.CommonModel
import com.example.testbasadata.singlechat.FragmentSingleChat
import com.example.testbasadata.singlechat.SingleChatAdapter
import com.example.testbasadata.utilits.downloadAndSetImage
import com.example.testbasadata.utilits.replaceFragment
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_main_list.view.*

class MainLIstAdapter : RecyclerView.Adapter<MainLIstAdapter.MainListHolder>() {

    val listItems = mutableListOf<CommonModel>()

    class MainListHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.main_list_item_name
        val itemLastMessage: TextView = view.main_list_last_message
        val itemPhoto: CircleImageView = view.main_list_item_photo

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_main_list, parent, false)
        val holder = MainListHolder(view)
        holder.itemView.setOnClickListener {
            replaceFragment(FragmentSingleChat(listItems[holder.adapterPosition]))
        }
        return holder
    }


    override fun onBindViewHolder(holder: MainListHolder, position: Int) {
        holder.itemName.text = listItems[position].fullname
        holder.itemLastMessage.text = listItems[position].lastMessage
        holder.itemPhoto.downloadAndSetImage(listItems[position].photoUrl)

    }

    fun updateListItems(item: CommonModel) {
        listItems.add(item)
        notifyItemInserted(listItems.size)
    }

    override fun getItemCount(): Int = listItems.size

}