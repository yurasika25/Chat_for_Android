package com.example.testbasadata.database

import android.net.Uri
import com.example.testbasadata.models.CommonModel
import com.example.testbasadata.models.UserModel
import com.example.testbasadata.utilits.appMainActivity
import com.example.testbasadata.utilits.AppValueEventListener
import com.example.testbasadata.utilits.fragmentManagerCopy
import com.example.testbasadata.utilits.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

fun initFireBase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    USER = UserModel()
    CURRENT_UID = AUTH.currentUser?.uid.toString()
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
}

inline fun putUrlToDataBase(url: String, crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
        .child(CHILD_PHOTO_URL).setValue(url)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun getUrlFromStorage(path: StorageReference, crossinline function: (url: String) -> Unit) {
    path.downloadUrl
        .addOnSuccessListener { function(it.toString()) }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun putFileToStorage(uri: Uri, path: StorageReference, crossinline function: () -> Unit) {
    path.putFile(uri)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun initUser(crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
        .addListenerForSingleValueEvent(AppValueEventListener {
            USER = it.getValue(UserModel::class.java) ?: UserModel()
            if (USER.username.isEmpty()) {
                USER.username = CURRENT_UID
            }
            function()
        })
}

fun updatePhonesToDataBase(arrayContacts: ArrayList<CommonModel>) {
    if (AUTH.currentUser != null) {
        REF_DATABASE_ROOT.child(NODE_PHONES).addListenerForSingleValueEvent(AppValueEventListener {
            it.children.forEach { snapshot ->
                arrayContacts.forEach { contact ->
                    if (snapshot.key == contact.phone) {
                        REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)
                            .child(snapshot.value.toString()).child(CHILD_ID)
                            .setValue(snapshot.value.toString())
                            .addOnFailureListener { showToast(it.message.toString()) }

                        REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)
                            .child(snapshot.value.toString()).child(CHILD_FULLNAME)
                            .setValue(contact.fullname)
                            .addOnFailureListener { showToast(it.message.toString()) }
                    }
                }
            }
        })
    }
}

fun DataSnapshot.getCommonModel(): CommonModel =
    this.getValue(CommonModel::class.java) ?: CommonModel()

fun DataSnapshot.getUserModel(): UserModel =
    this.getValue(UserModel::class.java) ?: UserModel()

fun sendMessage(message: String, receivingUserId: String, typeText: String, function: () -> Unit) {

    val refDialogUser = "$NODE_MESSAGES/$CURRENT_UID/$receivingUserId"
    val refDialogReceiverUser = "$NODE_MESSAGES/$receivingUserId/$CURRENT_UID"
    val messageKey = REF_DATABASE_ROOT.child(refDialogUser).push().key

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_FROM] = CURRENT_UID
    mapMessage[CHILD_TYPE] = typeText
    mapMessage[CHILD_TEXT] = message
    mapMessage[CHILD_ID] = messageKey.toString()
    mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP

    val mapDialogs = hashMapOf<String, Any>()
    mapDialogs["$refDialogUser/$messageKey"] = mapMessage
    mapDialogs["$refDialogReceiverUser/$messageKey"] = mapMessage

    REF_DATABASE_ROOT
        .updateChildren(mapDialogs)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun updateCurrentUserName(newUserName: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_USERNAME)
        .setValue(newUserName)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                showToast("Успішно")
                deleteOldUserName(newUserName)
            } else {
                showToast(it.exception?.message.toString())
            }
        }
}

fun deleteOldUserName(newUserName: String) {
    REF_DATABASE_ROOT.child(NODE_USER_NAMES).child(USER.username).removeValue()
        .addOnSuccessListener {
            showToast("Успішно")
            fragmentManagerCopy()
            USER.username = newUserName
        }.addOnFailureListener { showToast(it.message.toString()) }
}

fun setBioToDatabase(newBio: String) {

    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_BIO).setValue(newBio)
        .addOnSuccessListener {
            showToast("Дані збережено")
            USER.bio = newBio
            fragmentManagerCopy()
        }.addOnFailureListener { showToast(it.message.toString()) }
}

fun setNameToDatabase(fullname: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_FULLNAME)
        .setValue(fullname)
        .addOnSuccessListener {
            showToast("Дані змінено")
            USER.fullname = fullname
            appMainActivity.mAppDrawer.updateHeader()
            fragmentManagerCopy()
        }.addOnFailureListener { showToast(it.message.toString()) }
}

fun sendMessageAsFile(
    receivingUserId: String,
    fileUrl: String,
    messageKey: String,
    typeMessage: String,
    filename: String
) {


    val refDialogUser = "$NODE_MESSAGES/$CURRENT_UID/$receivingUserId"
    val refDialogReceiverUser = "$NODE_MESSAGES/$receivingUserId/$CURRENT_UID"

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_FROM] = CURRENT_UID
    mapMessage[CHILD_TYPE] = typeMessage
    mapMessage[CHILD_ID] = messageKey
    mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP
    mapMessage[CHILD_FILE_URL] = fileUrl
    mapMessage[CHILD_TEXT] = filename

    val mapDialogs = hashMapOf<String, Any>()
    mapDialogs["$refDialogUser/$messageKey"] = mapMessage
    mapDialogs["$refDialogReceiverUser/$messageKey"] = mapMessage

    REF_DATABASE_ROOT
        .updateChildren(mapDialogs)
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun getMessageKey(id: String) = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)
    .child(id).push().key.toString()

fun uploadFileToStorage(
    uri: Uri,
    messageKey: String,
    receiveID: String,
    typeMessage: String,
    filename: String = ""
) {
    val path = REF_STORAGE_ROOT
        .child(FOLDER_FILES).child(messageKey)
    putFileToStorage(uri, path) {
        getUrlFromStorage(path) {
            sendMessageAsFile(receiveID, it, messageKey, typeMessage, filename)
        }
    }
}

fun getFileFromStorage(mFile: File, fileUrl: String, function: () -> Unit) {
    val path = REF_STORAGE_ROOT.storage.getReferenceFromUrl(fileUrl)
    path.getFile(mFile)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun saveToMainList(id: String, type: String) {
    val refUser = "$NODE_MAIN_LIST/$CURRENT_UID/$id"
    val refReceived = "$NODE_MAIN_LIST/$id/$CURRENT_UID"

    val mapUser = hashMapOf<String, Any>()
    val mapReceived = hashMapOf<String, Any>()

    mapUser[CHILD_ID] = id
    mapUser[CHILD_TYPE] = type

    mapReceived[CHILD_ID] = CURRENT_UID
    mapUser[CHILD_TYPE] = type

    val commonMap = hashMapOf<String, Any>()
    commonMap[refUser] = mapUser
    commonMap[refReceived] = mapReceived

    REF_DATABASE_ROOT.updateChildren(commonMap)
        .addOnFailureListener {
            showToast(it.message.toString())
        }
}

fun deleteChat(id: String, function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_MAIN_LIST).child(CURRENT_UID).child(id).removeValue()
        .addOnFailureListener { showToast(it.message.toString()) }
        .addOnSuccessListener { function() }

}

fun clearChat(id: String, function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)
        .child(id).removeValue()
        .addOnFailureListener { showToast(it.message.toString()) }
        .addOnSuccessListener {
            REF_DATABASE_ROOT.child(NODE_MESSAGES)
                .child(id).child(CURRENT_UID).removeValue()
                .addOnSuccessListener { function() }
        }
        .addOnFailureListener { showToast(it.message.toString()) }
}