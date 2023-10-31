package com.tirex_projs.telegram_clone.utilits

import android.net.Uri
import android.provider.ContactsContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tirex_projs.telegram_clone.models.CommonModel
import com.tirex_projs.telegram_clone.models.User

lateinit var AUTH: FirebaseAuth
lateinit var CURRENT_UID :  String
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var USER : User

const val FOLDER_PROFILE_IMAGE = "profile_image"

const val NODE_USERS = "users"
const val NODE_USERNAMES = "usernames"
const val NODE_PHONES = "phones"

const val NODE_PHONES_CHILD_CONTACTS = "phones_contacts"

const val NODE_USERS_CHILD_ID = "id"
const val NODE_USERS_CHILD_PHONE = "phone"
const val NODE_USERS_CHILD_USERNAME = "username"
const val NODE_USERS_CHILD_FULLNAME = "fullname"
const val NODE_USERS_CHILD_BIO = "bio"
const val NODE_USERS_CHILD_PHOTOURL = "photoURL"
const val NODE_USERS_CHILD_STATE = "state"

fun initFirebase(){
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    USER = User()
    CURRENT_UID = AUTH.currentUser?.uid.toString()
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
}

inline fun putUrlToDB(url: String, crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
        .child(NODE_USERS_CHILD_PHOTOURL).setValue(url)
        .addOnSuccessListener {function()}
        .addOnFailureListener {showToast(it.message.toString())}
}

inline fun getUrlFromStorage(path: StorageReference, crossinline function: (url:String) -> Unit) {
    path.downloadUrl
        .addOnSuccessListener {function(it.toString())}
        .addOnFailureListener {showToast(it.message.toString())}
}

inline fun putImageToStorage(uri: Uri, path: StorageReference, crossinline function: () -> Unit) {
    path.putFile(uri)
        .addOnSuccessListener {function()}
        .addOnFailureListener {showToast(it.message.toString())}
}

inline fun initUser(crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
        .addListenerForSingleValueEvent(AppValueEventListener{
            USER = it.getValue(User::class.java) ?: User()
            if(USER.username.isEmpty()){
                USER.username = CURRENT_UID
            }
            function()
        })
}

fun initContacts() {
    if (checkPermission(READ_CONTACTS)){
        //showToast("READING CONTACTS...")
        val arrayContacts = arrayListOf<CommonModel>()
        val cursor = APP_ACTIVITY.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.let {
            while (it.moveToNext()){
                val tempContacts = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val tempPhone = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                if ((tempContacts >= 0) || (tempPhone >= 0)){
                    val fullName = it.getString(tempContacts)
                    val phone = it.getString(tempPhone)
                    val newModel = CommonModel()
                    newModel.fullname = fullName
                    newModel.phone = phone.replace(Regex("[\\s,-]"), "")
                    arrayContacts.add(newModel)
                }

            }
        }
        cursor?.close()
        updatePhonesToDatabase(arrayContacts)
    }
}

fun updatePhonesToDatabase(arrayContacts: ArrayList<CommonModel>) {
    REF_DATABASE_ROOT.child(NODE_PHONES).addListenerForSingleValueEvent(AppValueEventListener{
        it.children.forEach {snapshot ->
            arrayContacts.forEach{contact ->
                if (snapshot.key == contact.phone){
                    REF_DATABASE_ROOT.child(NODE_PHONES_CHILD_CONTACTS).child(CURRENT_UID)
                        .child(snapshot.value.toString()).child(NODE_USERS_CHILD_ID)
                        .setValue(snapshot.value.toString())
                        .addOnFailureListener{showToast(it.message.toString())}
                }
            }
        }
    })
}

fun DataSnapshot.getCommonModdel(): CommonModel =
    this.getValue(CommonModel::class.java)?: CommonModel()
