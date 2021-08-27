package com.example.testbasadata.utilits

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


const val READ_CONSTANTS = android.Manifest.permission.READ_CONTACTS
const val PERMISSION_REQUEST = 200
const val RECORD_AUDIO = android.Manifest.permission.RECORD_AUDIO
const val WRITE_FILES = android.Manifest.permission.WRITE_EXTERNAL_STORAGE

@SuppressLint("ObsoleteSdkInt")
fun checkPermission(permission: String): Boolean {
    return if (Build.VERSION.SDK_INT >= 23
        && ContextCompat.checkSelfPermission(
            appMainActivity,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(appMainActivity, arrayOf(permission), PERMISSION_REQUEST)
        false
    } else true
}