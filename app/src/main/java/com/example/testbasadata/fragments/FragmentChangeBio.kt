package com.example.testbasadata.fragments

import com.example.testbasadata.R
import com.example.testbasadata.utilits.*
import kotlinx.android.synthetic.main.fragment_change_bio.*

class FragmentChangeBio : BaseChangeFragment(R.layout.fragment_change_bio) {

    override fun onResume() {
        super.onResume()
        settings_input_bio.setText(USER.bio)
    }

    override fun change() {
        super.change()
        val newBio = settings_input_bio.text.toString()
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_BIO).setValue(newBio)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast("Дані збережено")
                    USER.bio = newBio
                    fragmentManagerCopy()
                }
            }
    }
}