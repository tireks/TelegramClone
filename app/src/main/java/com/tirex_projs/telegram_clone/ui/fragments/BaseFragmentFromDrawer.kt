package com.tirex_projs.telegram_clone.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.tirex_projs.telegram_clone.MainActivity
import com.tirex_projs.telegram_clone.utilits.APP_ACTIVITY
import com.tirex_projs.telegram_clone.utilits.hideKeyboard


abstract class BaseFragmentFromDrawer <VB : ViewBinding> : BaseFragmentPrior<VB>() {
    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.mAppDrawer.disableDrawer()
        setHasOptionsMenu(true)
        hideKeyboard()
    }

    override fun onStop() {
        super.onStop()
        APP_ACTIVITY.mAppDrawer.enableDrawer()
    }
}