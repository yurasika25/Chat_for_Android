package com.example.testbasadata.screens.register

import androidx.fragment.app.Fragment
import com.example.testbasadata.R
import com.example.testbasadata.database.*
import com.example.testbasadata.utilits.*
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_code.*

class FragmentEnterCode(private val mphoneNumber: String, val id: String) :
    Fragment(R.layout.fragment_enter_code) {

    override fun onStart() {
        super.onStart()
        (appMainActivity).title = mphoneNumber
        register_input_phone_code.addTextChangedListener(AppTextWatcher {
            val string = register_input_phone_code.text.toString()
            if (string.length >= 6) {
                enterCode()
            }
        })
    }

    private fun enterCode() {
        val code = register_input_phone_code.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = AUTH.currentUser?.uid.toString()
                val dateMap = mutableMapOf<String, Any>()
                dateMap[CHILD_ID] = uid
                dateMap[CHILD_PHONE] = mphoneNumber

                REF_DATABASE_ROOT.child(NODE_USERS).child(uid)
                    .addListenerForSingleValueEvent(AppValueEventListener {

                        if (!it.hasChild(CHILD_USERNAME)){
                            dateMap[CHILD_USERNAME] = uid
                        }

                        REF_DATABASE_ROOT.child(NODE_PHONES).child(mphoneNumber).setValue(uid)
                            .addOnFailureListener { showToast(it.message.toString()) }
                            .addOnSuccessListener {
                                REF_DATABASE_ROOT.child(NODE_USERS).child(uid)
                                    .updateChildren(dateMap)
                                    .addOnSuccessListener {
                                        showToast("Ласкаво просимо")
                                        restartActivity()
                                    }
                                    .addOnFailureListener { showToast(it.message.toString()) }
                            }
                    })

            } else showToast(task.exception?.message.toString())
        }
    }
}