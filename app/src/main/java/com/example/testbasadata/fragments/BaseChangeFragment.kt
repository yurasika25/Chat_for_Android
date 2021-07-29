package com.example.testbasadata.fragments

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.testbasadata.R
import com.example.testbasadata.telegram.ActivityTelegram
import com.example.testbasadata.utilits.APP_ACTIVITY
import com.example.testbasadata.utilits.hideKeyBoard

open class BaseChangeFragment(layout: Int): Fragment(layout) {

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
        (APP_ACTIVITY).mAppDrawer.disableDrawer()

    }

    override fun onStop() {
        super.onStop()
        hideKeyBoard()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (APP_ACTIVITY).menuInflater.inflate(R.menu.settings_menu_confirm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_confirm_change -> change()

        }
        return true
    }

    open fun change() {
    }
}