package com.example.testbasadata.firstchat

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.testbasadata.R
import com.example.testbasadata.dialogs.DialogConfirm
import com.example.testbasadata.ui.Test
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main_fire_test.*
import kotlinx.android.synthetic.main.start_activity.*
import java.util.*


open class LoginActivity : AppCompatActivity() {

    private lateinit var edLogin: EditText
    private lateinit var edPassword: EditText
    private lateinit var mAuth: FirebaseAuth
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var tvInformation: TextView
    private lateinit var checkBox: CheckBox
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_activity)
        init()
        checkBox.setOnClickListener {
            if (checkBox.isChecked) {
                edPassword.inputType = 1
            } else {
                edPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val cUser = mAuth.currentUser
        if (cUser != null) {
            val intent = Intent(this@LoginActivity, Test::class.java)
            startActivity(intent)
//            val userName = "Ви ввійшли як: " + cUser.email
////            tvInformation.text = userName

        } else {
            checkBox.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    private fun init() {
        edLogin = findViewById(R.id.id_edit_email)
        edPassword = findViewById(R.id.id_edit_password)
        mAuth = FirebaseAuth.getInstance()

        btnLogin = findViewById(R.id.id_btn_login)
        btnRegister = findViewById(R.id.id_btn_register)

        tvInformation = findViewById(R.id.id_text_information)

        checkBox = findViewById(R.id.checkBox)
        progressBar = findViewById(R.id.progressBar)

    }

    fun onExitBase() {
        FirebaseAuth.getInstance().signOut()
    }

    fun onClickSingIn(view: View?) {
        if (!TextUtils.isEmpty(edLogin.text.toString())
            && !TextUtils.isEmpty(edPassword.text.toString())
        ) {
            mAuth.signInWithEmailAndPassword(
                edLogin.text.toString(),
                edPassword.text.toString()
            ).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    showSigned()
                } else {
                    wormTypeText()
                    progressBar.visibility = View.GONE
                }
            }
        }
    }


    @SuppressLint("ShowToast")
    private fun wormTypeText() {
        Snackbar.make(mainContainer, "Ви ввели некоректні дані!", Snackbar.LENGTH_SHORT)
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
            .setBackgroundTint(Color.parseColor("#0B0C15"))
            .show()
    }

    fun onClickRegister(view: View?) {
        if (!TextUtils.isEmpty(edLogin.text.toString())
            && !TextUtils.isEmpty(edPassword.text.toString())
        ) {
            mAuth.createUserWithEmailAndPassword(
                edLogin.text.toString(),
                edPassword.text.toString()
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showSigned()
                    sendEmailVer()
                } else {
                    warmRegister()
                }
            }
        } else {
            Toast.makeText(this, "Введіть дані!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showSigned() {
        val user: FirebaseUser = mAuth.currentUser!!
        if (user.isEmailVerified) {
            val intent = Intent(this@LoginActivity, Test::class.java)
            startActivity(intent)
            progressBar.visibility = View.VISIBLE
            tvInformation.visibility = View.VISIBLE
        } else {
            emailConfirm()
        }
    }

    private fun sendEmailVer() {
        val user: FirebaseUser = mAuth.currentUser!!
        user.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val myDialogFragment = DialogConfirm()
                val manager = supportFragmentManager
                myDialogFragment.show(manager, "myDialog")

            } else {
                Toast.makeText(this@LoginActivity, "Щось пішло не так!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    @SuppressLint("ShowToast")
    private fun warmRegister() {
        Snackbar.make(
            mainContainer,
            "Дані вказано некоректно або вже використовуються!",
            Snackbar.LENGTH_LONG
        )
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
            .setBackgroundTint(Color.parseColor("#0B0C15"))
            .show()
    }

    @SuppressLint("ShowToast")
    private fun emailConfirm() {
        Snackbar.make(
            mainContainer,
            "Підтвердіть адресу електронної пошти",
            Snackbar.LENGTH_SHORT
        )
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
            .setBackgroundTint(Color.parseColor("#0B0C15"))
            .show()
    }
}