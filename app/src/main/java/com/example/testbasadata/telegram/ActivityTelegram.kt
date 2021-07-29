package com.example.testbasadata.telegram

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.testbasadata.`object`.AppDriver
import com.example.testbasadata.activities.RegisterActivity
import com.example.testbasadata.databinding.ActivityTelegramBinding
import com.example.testbasadata.fragments.FragmentChat
import com.example.testbasadata.models.User
import com.example.testbasadata.utilits.*
import com.theartofdev.edmodo.cropper.CropImage

class ActivityTelegram : AppCompatActivity() {

    lateinit var mAppDrawer: AppDriver
    private lateinit var mBinding: ActivityTelegramBinding
    private lateinit var mToolBar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mBinding = ActivityTelegramBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initFireBase()
        initUser {
            initFields()
            initFun()
        }
        APP_ACTIVITY = this
    }

    private fun initFun() {
        if (AUTH.currentUser != null) {
            setSupportActionBar(mToolBar)
            mAppDrawer.create()
            replaceFragment(FragmentChat(), false)

        } else {
            replaceActivity(RegisterActivity())
        }
    }

    private fun initFields() {
        mToolBar = mBinding.mainToolbar
        mAppDrawer = AppDriver(this, mToolBar)

    }

    override fun onStart() {
        super.onStart()
        AppStates.updateState(AppStates.ONLINE)
    }

    override fun onStop() {
        super.onStop()
        AppStates.updateState(AppStates.OFFLINE)
    }
}