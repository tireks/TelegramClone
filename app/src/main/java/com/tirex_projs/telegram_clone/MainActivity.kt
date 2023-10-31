package com.tirex_projs.telegram_clone

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuthSettings
import com.tirex_projs.telegram_clone.activities.AuthControlActivity
import com.tirex_projs.telegram_clone.databinding.ActivityMainBinding
import com.tirex_projs.telegram_clone.models.User
import com.tirex_projs.telegram_clone.ui.fragments.ChatsFragment
import com.tirex_projs.telegram_clone.ui.objects.AppDrawer
import com.tirex_projs.telegram_clone.utilits.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var mActivityMainBinding: ActivityMainBinding
    lateinit var mAppDrawer: AppDrawer
    lateinit var mToolbar: Toolbar
    //private lateinit var mAuthFirebase: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mActivityMainBinding.root)
        APP_ACTIVITY = this
        initFirebase()
        initUser{
            CoroutineScope(Dispatchers.IO).launch {
                initContacts()
            }
            initFields()
            initFunctionality()
        }
    }

    override fun onStart() {
        super.onStart()
        AppStates.updateState(AppStates.ONLINE)
    }

    private fun initFunctionality() {
        if (AUTH.currentUser != null){
            setSupportActionBar(mToolbar)
            mAppDrawer.create()
            replaceFragment(ChatsFragment(), R.id.dataContainer, false)
        } else {
            replaceActivity(AuthControlActivity())
        }
    }

    private fun initFields() {
        mToolbar = mActivityMainBinding.mainToolBar
        mAppDrawer = AppDrawer()
        val firebaseAuthSettings: FirebaseAuthSettings = AUTH.firebaseAuthSettings
        firebaseAuthSettings.setAppVerificationDisabledForTesting(true)
    }


    override fun onStop() {
        super.onStop()
        AppStates.updateState(AppStates.OFFLINE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(APP_ACTIVITY, READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
            initContacts()
        }
    }
}