package com.example.testbasadata.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testbasadata.R
import com.example.testbasadata.database.*
import com.example.testbasadata.models.CommonModel
import com.example.testbasadata.singlechat.FragmentSingleChat
import com.example.testbasadata.utilits.*
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.android.synthetic.main.item_contact.view.*

class FragmentContacts : FragmentBase(R.layout.fragment_contacts) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: FirebaseRecyclerAdapter<CommonModel, ContactHolder>
    private lateinit var mRefContacts: DatabaseReference
    private lateinit var mRefUser: DatabaseReference
    private lateinit var mRefUserListener: AppValueEventListener
    private var mapListener = hashMapOf<DatabaseReference, AppValueEventListener>()

    override fun onResume() {
        super.onResume()
        appMainActivity.title = "Контакти"
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mRecyclerView = contacts_recycler_view
        mRefContacts = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)

        val option = FirebaseRecyclerOptions.Builder<CommonModel>()
            .setQuery(mRefContacts, CommonModel::class.java)
            .build()

        mAdapter = object : FirebaseRecyclerAdapter<CommonModel, ContactHolder>(option) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_contact, parent, false)
                return ContactHolder(view)
            }

            override fun onBindViewHolder(
                holder: ContactHolder,
                position: Int,
                model: CommonModel
            ) {
                mRefUser = REF_DATABASE_ROOT.child(NODE_USERS).child(model.id)
                mRefUserListener = AppValueEventListener {
                    val contact = it.getCommonModel()
                    if (contact.fullname.isEmpty()) {
                        holder.name.text = model.fullname
                    } else holder.name.text = contact.fullname

                    holder.name.text = contact.fullname
                    holder.status.text = contact.state
                    holder.image.downloadAndSetImage(contact.photoUrl)
                    holder.itemView.setOnClickListener { replaceFragment(FragmentSingleChat(model)) }
                }
                mRefUser.addValueEventListener(mRefUserListener)
                mapListener[mRefUser] = mRefUserListener
            }
        }
        mRecyclerView.adapter = mAdapter
        mAdapter.startListening()
    }

    class ContactHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.contact_fullname
        val status: TextView = view.contact_status
        val image: CircleImageView = view.contact_photo
    }

    override fun onPause() {
        super.onPause()
        mAdapter.stopListening()
        mapListener.forEach {
            it.key.removeEventListener(it.value)
        }
    }
}
