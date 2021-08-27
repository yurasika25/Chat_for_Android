package com.example.testbasadata.main

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.testbasadata.`object`.AppDriver
import com.example.testbasadata.database.AUTH
import com.example.testbasadata.database.initFireBase
import com.example.testbasadata.database.initUser
import com.example.testbasadata.databinding.ActivityTelegramBinding
import com.example.testbasadata.screens.mainlist.MainListFragment
import com.example.testbasadata.screens.register.FragmentEnterPhone
import com.example.testbasadata.utilits.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var mAppDrawer: AppDriver
    private lateinit var mBinding: ActivityTelegramBinding
    lateinit var mToolBar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityTelegramBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initFireBase()
        initUser {
            CoroutineScope(Dispatchers.IO).launch {
                initContacts()
            }
            initFields()
            initFun()
        }
        appMainActivity = this
    }


    private fun initFun() {
        setSupportActionBar(mToolBar)
        if (AUTH.currentUser != null) {
            mAppDrawer.create()
            replaceFragment(MainListFragment(), false)
        } else {
            replaceFragment(FragmentEnterPhone(), false)
        }
    }

    private fun initFields() {
        mToolBar = mBinding.mainToolbar
        mAppDrawer = AppDriver()

    }

    override fun onStart() {
        super.onStart()
        AppStates.updateState(AppStates.ONLINE)
    }

    override fun onStop() {
        super.onStop()
        AppStates.updateState(AppStates.OFFLINE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(
                appMainActivity,
                READ_CONSTANTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            initContacts()
        }
    }
}