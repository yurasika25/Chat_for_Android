 package com.example.testbasadata.screens.register

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.testbasadata.R
import com.example.testbasadata.database.AUTH
import com.example.testbasadata.utilits.*
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_phone.*
import java.util.concurrent.TimeUnit


class FragmentEnterPhone : Fragment(R.layout.fragment_enter_phone) {

    private lateinit var mPhoneNumber: String
    private lateinit var mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        AUTH = FirebaseAuth.getInstance()
        mCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast("Ласкаво просимо")
                        restartActivity()
                    } else showToast(task.exception?.message.toString())
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showToast(p0.message.toString())
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                replaceFragment(FragmentEnterCode(mPhoneNumber, id))
            }
        }

        register_input_phone_number.setAutofillHints(View.AUTOFILL_HINT_PHONE)
        id_btn_float_register_next.setOnClickListener { sendCode() }
    }

    private fun sendCode() {
        if (register_input_phone_number.text.toString().isEmpty()) {
            showToast("Введіть номер телефону")
        } else {
            autUser()
        }
    }


    private fun autUser() {
        mPhoneNumber = register_input_phone_number.text.toString()
        PhoneAuthProvider.verifyPhoneNumber(
            PhoneAuthOptions
                .newBuilder(FirebaseAuth.getInstance())
                .setActivity(appMainActivity)
                .setPhoneNumber(mPhoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(mCallBack)
                .build()
        )
    }
}