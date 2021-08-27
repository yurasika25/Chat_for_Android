package com.example.testbasadata.screens.settings

import com.example.testbasadata.R
import com.example.testbasadata.database.USER
import com.example.testbasadata.database.setNameToDatabase
import com.example.testbasadata.utilits.*
import kotlinx.android.synthetic.main.fragment_change_name.*

class ChangeNameFragment : BaseChangeFragment(R.layout.fragment_change_name) {
    override fun onResume() {
        super.onResume()
        initFullNameList()
    }

    private fun initFullNameList() {
        val fullnameList = USER.fullname.split(" ")
        if (fullnameList.size > 1) {
            settings_input_name.setText(fullnameList[0])
            settings_input_surname.setText(fullnameList[1])
        } else settings_input_name.setText(fullnameList[0])
    }

    override fun change() {
        val name = settings_input_name.text.toString()
        val surname = settings_input_surname.text.toString()
        if (name.isEmpty()) {
            showToast("Ім'я не може бути пустим")
        } else {
            val fullname = "$name $surname"
            setNameToDatabase(fullname)
        }
    }
}