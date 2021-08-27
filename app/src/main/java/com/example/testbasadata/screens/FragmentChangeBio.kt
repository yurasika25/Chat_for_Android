package com.example.testbasadata.screens

import com.example.testbasadata.R
import com.example.testbasadata.database.USER
import com.example.testbasadata.database.setBioToDatabase
import com.example.testbasadata.screens.settings.BaseChangeFragment
import kotlinx.android.synthetic.main.fragment_change_bio.*

class FragmentChangeBio : BaseChangeFragment(R.layout.fragment_change_bio) {

    override fun onResume() {
        super.onResume()
        settings_input_bio.setText(USER.bio)
    }

    override fun change() {
        super.change()
        val newBio = settings_input_bio.text.toString()
        setBioToDatabase(newBio)
    }
}