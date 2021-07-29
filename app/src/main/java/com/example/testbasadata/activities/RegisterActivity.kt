package com.example.testbasadata.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.testbasadata.R
import com.example.testbasadata.databinding.ActivityRegisterBinding
import com.example.testbasadata.fragments.FragmentEnterPhone
import com.example.testbasadata.utilits.initFireBase
import com.example.testbasadata.utilits.replaceFragment

class RegisterActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityRegisterBinding
    private lateinit var mToolBar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initFireBase()
    }

    override fun onStart() {
        super.onStart()
        mToolBar = mBinding.registerToolbar
        setSupportActionBar(mToolBar)
        title = "Ваш телефон"
        replaceFragment(FragmentEnterPhone(),false)
    }
}