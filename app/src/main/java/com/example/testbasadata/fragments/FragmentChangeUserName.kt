package com.example.testbasadata.fragments

import com.example.testbasadata.R
import com.example.testbasadata.utilits.*
import kotlinx.android.synthetic.main.fragment_change_user_name.*
import java.util.*

class FragmentChangeUserName : BaseChangeFragment(R.layout.fragment_change_user_name) {

    private lateinit var mNewUserName: String


    override fun onResume() {
        super.onResume()
        settings_input_user.setText(USER.username)
    }

    override fun change() {
        mNewUserName = settings_input_user.text.toString().lowercase(Locale.getDefault())
        if (mNewUserName.isEmpty()) {
            showToast("Введіть псевдонім")
        } else {
            REF_DATABASE_ROOT.child(NODE_USER_NAMES)
                .addListenerForSingleValueEvent(AppValueEventListener {
                    if (it.hasChild(mNewUserName)) {
                        showToast("Такий користувач уже іcнує")
                    } else changeUserName()
                })
        }
    }

    private fun changeUserName() {
        REF_DATABASE_ROOT.child(NODE_USER_NAMES).child(mNewUserName).setValue(CURRENT_UID)
            .addOnCompleteListener {
                if (it.isSuccessful)
                    updateCurrentUserName()
            }
    }

    private fun updateCurrentUserName() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_USER_NAME)
            .setValue(mNewUserName)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast("Успішно")
                    deleteOldUserName()
                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }

    private fun deleteOldUserName() {
        REF_DATABASE_ROOT.child(NODE_USER_NAMES).child(USER.username).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast("Успішно")
                    fragmentManagerCopy()
                    USER.username = mNewUserName
                } else {
                    showToast(it.exception?.message.toString())
                }

            }

    }
}