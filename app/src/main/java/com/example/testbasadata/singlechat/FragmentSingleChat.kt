package com.example.testbasadata.singlechat

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.*
import android.widget.AbsListView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.testbasadata.R
import com.example.testbasadata.database.*
import com.example.testbasadata.screens.FragmentBase
import com.example.testbasadata.ui.message_recycler_view.views.AppViewFactory
import com.example.testbasadata.models.CommonModel
import com.example.testbasadata.models.UserModel
import com.example.testbasadata.screens.mainlist.MainListFragment
import com.example.testbasadata.utilits.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.DatabaseReference
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_telegram.view.*
import kotlinx.android.synthetic.main.choice_upload.*
import kotlinx.android.synthetic.main.fragment_single_chat.*
import kotlinx.android.synthetic.main.toolbar_info.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentSingleChat(private val contact: CommonModel) :
    FragmentBase(R.layout.fragment_single_chat) {

    private lateinit var mListenerInfoToolBar: AppValueEventListener
    private lateinit var mReceivingUser: UserModel
    private lateinit var mToolbarInfo: View
    private lateinit var mRefUsers: DatabaseReference
    private lateinit var mRefMessages: DatabaseReference
    private lateinit var mAdapter: SingleChatAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mMessagesListener: AppChildEventListener
    private var mCountMessages = 10
    private var mIssScrolling = false
    private var mSmoothScrollToPosition = true
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAppVoiceRecorder: AppVoiceRecorder
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<*>


    override fun onResume() {
        super.onResume()
        initFields()
        initToolBar()
        initRecyclerView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initFields() {
        setHasOptionsMenu(true)
        mBottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_choice)
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        mAppVoiceRecorder = AppVoiceRecorder()
        mSwipeRefreshLayout = chat_swipe_refresh
        mLayoutManager = LinearLayoutManager(this.context)
        type_message.addTextChangedListener(AppTextWatcher {
            val string = type_message.text.toString()
            if (string.isEmpty() || string == "Триває запис...") {
                image_send_message.visibility = View.GONE
                image_attach.visibility = View.VISIBLE
                image_voice.visibility = View.VISIBLE
            } else {
                image_send_message.visibility = View.VISIBLE
                image_attach.visibility = View.GONE
                image_voice.visibility = View.GONE
            }
        })
        image_attach.setOnClickListener {
            attach()

        }

        CoroutineScope(Dispatchers.IO).launch {
            image_voice.setOnTouchListener { v, event ->
                if (checkPermission(RECORD_AUDIO)) {
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        type_message.setText("Триває запис...")
                        image_voice.setColorFilter(ContextCompat.getColor(appMainActivity,R.color.color_telegram
                            )
                        )
                        val messageKey = getMessageKey(contact.id)
                        mAppVoiceRecorder.startRecord(messageKey)
                    } else if (event.action == MotionEvent.ACTION_UP) {
                        type_message.setText("")
                        image_voice.colorFilter = null
                        mAppVoiceRecorder.stopRecord { file, messageKey ->
                            uploadFileToStorage(
                                Uri.fromFile(file),
                                messageKey,
                                contact.id,
                                TYPE_MESSAGE_VOICE
                            )
                            mSmoothScrollToPosition = true
                        }
                    }
                }
                true
            }
        }
    }

    private fun attach() {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        btn_attach_file.setOnClickListener { attachFile() }
        btn_attach_image.setOnClickListener { attachImage() }
    }

    private fun attachFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)

    }

    private fun attachImage() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(250, 250)
            .start(appMainActivity, this)
    }

    private fun initRecyclerView() {
        mRecyclerView = chat_recycler_view
        mAdapter = SingleChatAdapter()
        mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES)
            .child(CURRENT_UID)
            .child(contact.id)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.isNestedScrollingEnabled = false
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = mLayoutManager
        mMessagesListener = AppChildEventListener {
            val message = it.getCommonModel()
            if (mSmoothScrollToPosition) {
                mAdapter.addItemToBottom(AppViewFactory.getView(message)) {
                    mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
                }
            } else {
                mAdapter.addItemToTop(AppViewFactory.getView(message)) {
                    mSwipeRefreshLayout.isRefreshing = false
                }
            }
        }

        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                println(mRecyclerView.recycledViewPool.getRecycledViewCount(0))
                if (mIssScrolling && dy < 0 && mLayoutManager.findFirstVisibleItemPosition() <= 3) {
                    updateDate()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    mIssScrolling = true

            }

        })
        mSwipeRefreshLayout.setOnRefreshListener {
            updateDate()
        }
    }

    private fun updateDate() {
        mSmoothScrollToPosition = false
        mIssScrolling = false
        mCountMessages += 10
        mRefMessages.removeEventListener(mMessagesListener)
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)
    }

    private fun initToolBar() {
        mToolbarInfo = appMainActivity.mToolBar.toolBarInfo
        mToolbarInfo.visibility = View.VISIBLE
        mListenerInfoToolBar = AppValueEventListener {
            mReceivingUser = it.getUserModel()
            initInfoToolBar()
        }
        mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS).child(contact.id)
        mRefUsers.addValueEventListener(mListenerInfoToolBar)

        image_send_message.setOnClickListener {
            mSmoothScrollToPosition = true
            val message = type_message.text.toString()
            if (message.isEmpty()) {
                showToast("Введіть повідомлення")
            } else sendMessage(message, contact.id, TYPE_TEXT) {
                saveToMainList(contact.id, TYPE_CHAT)
                type_message.setText("")
            }
        }
    }


    private fun initInfoToolBar() {
        if (mReceivingUser.fullname.isEmpty()) {
            mToolbarInfo.toolbar_chat_fullname.text = contact.fullname
        } else mToolbarInfo.toolbar_chat_fullname.text = mReceivingUser.fullname

        mToolbarInfo.toolbar_chat_image.downloadAndSetImage(mReceivingUser.photoUrl)
        mToolbarInfo.toolbar_chat_status.text = mReceivingUser.state
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {

            when (requestCode) {
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val uri = CropImage.getActivityResult(data).uri
                    val messageKey = getMessageKey(contact.id)
                    uploadFileToStorage(uri, messageKey, contact.id, TYPE_MESSAGE_IMAGE)
                    mSmoothScrollToPosition = true
                }
                PICK_FILE_REQUEST_CODE -> {
                    val uri = data.data
                    val messageKey = getMessageKey(contact.id)
                    val filename = getFileNameFromUri(uri!!)
                    uploadFileToStorage(uri, messageKey, contact.id, TYPE_MESSAGE_FILE,
                        filename.toString()
                    )
                    mSmoothScrollToPosition = true
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mToolbarInfo.visibility = View.GONE
        mRefUsers.removeEventListener(mListenerInfoToolBar)
        mRefMessages.removeEventListener(mMessagesListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        mAppVoiceRecorder.releaseRecord()
        mAdapter.destroy()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.single_chat_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_clear_chat -> clearChat(contact.id){
                showToast("Історію очищено")
                replaceFragment(MainListFragment())
            }
            R.id.menu_delete_chat -> deleteChat(contact.id){
                showToast("Чат видалено")
                replaceFragment(MainListFragment())
            }
        }
        return true
    }

}