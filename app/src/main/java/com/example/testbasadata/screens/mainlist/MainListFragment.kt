package com.example.testbasadata.screens.mainlist

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

import com.example.testbasadata.R
import com.example.testbasadata.database.*
import com.example.testbasadata.models.CommonModel

import com.example.testbasadata.utilits.appMainActivity
import com.example.testbasadata.utilits.AppValueEventListener
import com.example.testbasadata.utilits.hideKeyBoard
import kotlinx.android.synthetic.main.fragment_main_list.*

class MainListFragment : Fragment(R.layout.fragment_main_list) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: MainLIstAdapter

    private val mRefMainList = REF_DATABASE_ROOT.child(NODE_MAIN_LIST).child(CURRENT_UID)
    private val mRefUser = REF_DATABASE_ROOT.child(NODE_USERS)
    private val mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)
    private var mListItems = listOf<CommonModel>()

    override fun onResume() {
        super.onResume()
        appMainActivity.title = "Чати"
        appMainActivity.mAppDrawer.enableDrawer()
        hideKeyBoard()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mRecyclerView = main_list_recycler_view
        mAdapter = MainLIstAdapter()
        mRefMainList.addListenerForSingleValueEvent(AppValueEventListener {
            mListItems = it.children.map { dataSnapshot -> dataSnapshot.getCommonModel() }
            mListItems.forEach { model ->
                mRefUser.child(model.id)
                    .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot2 ->
                        val newModel = dataSnapshot2.getCommonModel()
                        mRefMessages.child(model.id).limitToLast(1)
                            .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot3 ->
                                val tempList = dataSnapshot3.children.map { it.getCommonModel() }

                                if (tempList.isEmpty()) {
                                    newModel.lastMessage = "Історію очищено"
                                } else {

                                    newModel.lastMessage = tempList[0].text
                                }
                                if (newModel.fullname.isEmpty()) {
                                    newModel.fullname = newModel.phone
                                }
                                mAdapter.updateListItems(newModel)
                            })
                    })
            }
        })
        mRecyclerView.adapter = mAdapter
    }
}