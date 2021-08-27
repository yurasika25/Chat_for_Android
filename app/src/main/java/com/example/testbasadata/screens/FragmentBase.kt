package com.example.testbasadata.screens

import androidx.fragment.app.Fragment
import com.example.testbasadata.utilits.appMainActivity

open class FragmentBase(layout: Int) : Fragment(layout) {

    override fun onStart() {
        super.onStart()
        (appMainActivity).mAppDrawer.disableDrawer()
    }
}