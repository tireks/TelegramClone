package com.tirex_projs.telegram_clone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.tirex_projs.telegram_clone.R
import com.tirex_projs.telegram_clone.databinding.ActivityAuthControlBinding
import com.tirex_projs.telegram_clone.ui.fragments.ChatsFragment
import com.tirex_projs.telegram_clone.ui.fragments.EnterPhoneNumFragment
import com.tirex_projs.telegram_clone.utilits.initFirebase
import com.tirex_projs.telegram_clone.utilits.replaceFragment

class AuthControlActivity : AppCompatActivity() {

    private lateinit var mBiAuthBinding: ActivityAuthControlBinding
    private lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBiAuthBinding = ActivityAuthControlBinding.inflate(layoutInflater)
        setContentView(mBiAuthBinding.root)
        //initFirebase()
    }

    override fun onStart() {
        super.onStart()
        mToolbar = mBiAuthBinding.authToolBar
        setSupportActionBar(mToolbar)
        title = getString(R.string.auth_toolbar_title)
        replaceFragment(EnterPhoneNumFragment(), R.id.AuthDataContainer, false)
    }
}