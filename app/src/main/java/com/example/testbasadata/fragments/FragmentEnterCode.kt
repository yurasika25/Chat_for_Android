package com.example.testbasadata.fragments

import androidx.fragment.app.Fragment
import com.example.testbasadata.R
import com.example.testbasadata.activities.RegisterActivity
import com.example.testbasadata.telegram.ActivityTelegram
import com.example.testbasadata.utilits.*
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_code.*

class FragmentEnterCode(private val mPhoneNumber: String, val id: String) :
    Fragment(R.layout.fragment_enter_code) {

    override fun onStart() {
        super.onStart()
        (activity as RegisterActivity).title = mPhoneNumber
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
                dateMap[CHILD_PHONE] = mPhoneNumber
                dateMap[CHILD_USER_NAME] = uid

                REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dateMap)
                    .addOnCompleteListener { task2 ->
                        if (task2.isSuccessful) {
                            showToast("Ласкаво просимо")
                            (activity as RegisterActivity).replaceActivity(ActivityTelegram())
                        } else showToast(task2.exception?.message.toString())
                    }
            } else showToast(task.exception?.message.toString())
        }
    }
}