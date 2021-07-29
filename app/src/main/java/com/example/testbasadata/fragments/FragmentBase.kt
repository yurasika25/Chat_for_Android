package com.example.testbasadata.fragments

import androidx.fragment.app.Fragment
import com.example.testbasadata.utilits.APP_ACTIVITY

open class FragmentBase(layout: Int) : Fragment(layout) {

    override fun onStart() {
        super.onStart()
        (APP_ACTIVITY).mAppDrawer.disableDrawer()
    }

    override fun onStop() {
        super.onStop()
        (APP_ACTIVITY).mAppDrawer.enableDrawer()

    }
}