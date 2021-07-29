package com.example.testbasadata.fragments

import com.example.testbasadata.R
import com.example.testbasadata.utilits.*
import kotlinx.android.synthetic.main.fragment_change_name.*

class  ChangeNameFragment : BaseChangeFragment(R.layout.fragment_change_name) {
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
            REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_FULLNAME)
                .setValue(fullname).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showToast("Дані змінено")
                        USER.fullname = fullname
                        APP_ACTIVITY.mAppDrawer.updateHeader()
                        fragmentManagerCopy()
                    }
                }
        }
    }
}