package com.example.testbasadata.utilits

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.provider.OpenableColumns
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.testbasadata.R
import com.example.testbasadata.database.updatePhonesToDataBase
import com.example.testbasadata.models.CommonModel
import com.example.testbasadata.main.MainActivity
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

fun showToast(message: String) {
    Toast.makeText(appMainActivity, message, Toast.LENGTH_SHORT).show()
}

fun restartActivity() {
    val intent = Intent(appMainActivity, MainActivity::class.java)
    appMainActivity.startActivity(intent)
    appMainActivity.finish()
}

fun replaceFragment(fragment: Fragment, addStack: Boolean = true) {
    if (addStack) {
        appMainActivity.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(
                R.id.dataContainer,
                fragment
            ).commit()
    } else {
        appMainActivity.supportFragmentManager.beginTransaction()
            .replace(
                R.id.dataContainer,
                fragment
            ).commit()
    }
}


fun hideKeyBoard() {
    val imm: InputMethodManager = appMainActivity.getSystemService(Context.INPUT_METHOD_SERVICE)
            as InputMethodManager
    imm.hideSoftInputFromWindow(appMainActivity.window.decorView.windowToken, 0)
}

fun ImageView.downloadAndSetImage(url: String) {
    Picasso.get()
        .load(url)
        .fit()
        .placeholder(R.drawable.camera)
        .into(this)
}

fun fragmentManagerCopy() {
    val ft = appMainActivity.supportFragmentManager
    ft.popBackStack()
}

fun initContacts() {
    if (checkPermission(READ_CONSTANTS)) {
        val arrayContacts = arrayListOf<CommonModel>()
        val cursor = appMainActivity.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.let {
            while (cursor.moveToNext()) {
                val fullName =
                    it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phone =
                    it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val newModel = CommonModel()
                newModel.fullname = fullName
                newModel.phone = phone.replace(Regex("[\\s,-]"), "")
                arrayContacts.add(newModel)
            }
        }
        cursor?.close()
        updatePhonesToDataBase(arrayContacts)
    }
}

fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(time)
}

fun getFileNameFromUri(uri: Uri): Any {
    var result = ""
    val cursor = appMainActivity.contentResolver.query(uri, null, null, null, null)
    try {
        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
    } catch (e: Exception) {
        showToast(e.message.toString())
    } finally {
        cursor?.close()
        return result
    }
}

