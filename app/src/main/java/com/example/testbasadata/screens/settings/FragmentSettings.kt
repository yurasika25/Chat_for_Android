package com.example.testbasadata.screens.settings

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.example.testbasadata.R
import com.example.testbasadata.database.*
import com.example.testbasadata.screens.FragmentBase
import com.example.testbasadata.screens.FragmentChangeBio
import com.example.testbasadata.utilits.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_settings.*

class FragmentSettings : FragmentBase(R.layout.fragment_settings) {

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        initFields()
    }

    private fun initFields() {
        settings_bio.text = USER.bio
        settings_full_name.text = USER.fullname
        settings_number.text = USER.phone
        settings_status.text = USER.state
        settings_user_name.text = USER.username
        settings_btn_change_user_name.setOnClickListener { replaceFragment(FragmentChangeUserName()) }
        settings_btn_change_bio.setOnClickListener { replaceFragment(FragmentChangeBio()) }
        id_settings_user_photo_change.setOnClickListener { changePhotoUser() }
        id_settings_user_photo.downloadAndSetImage(USER.photoUrl)
    }

    private fun changePhotoUser() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(250, 250)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(appMainActivity, this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.menu_settings, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_menu_exit -> {
                AppStates.updateState(AppStates.OFFLINE)
                AUTH.signOut()
                restartActivity()
            }
            R.id.settings_menu_edit -> replaceFragment(ChangeNameFragment())
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == RESULT_OK && data != null
        ) {
            val uri = CropImage.getActivityResult(data).uri
            val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)
                .child(CURRENT_UID)

            putFileToStorage(uri, path) {
                getUrlFromStorage(path) {
                    putUrlToDataBase(it) {
                        id_settings_user_photo.downloadAndSetImage(it)
                        showToast("Дані оновлено успішно")
                        USER.photoUrl = it
                        appMainActivity.mAppDrawer.updateHeader()
                    }
                }
            }
        }
    }
}