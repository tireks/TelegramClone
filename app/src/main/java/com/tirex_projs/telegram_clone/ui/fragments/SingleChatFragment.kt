package com.tirex_projs.telegram_clone.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.children
import com.tirex_projs.telegram_clone.databinding.FragmentSingleChatBinding
import com.tirex_projs.telegram_clone.models.CommonModel
import com.tirex_projs.telegram_clone.utilits.APP_ACTIVITY

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SingleChatFragment(contact: CommonModel) : BaseFragmentFromDrawer<FragmentSingleChatBinding>() {

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSingleChatBinding {
        return FragmentSingleChatBinding.inflate(inflater, container, false)
    }

    override fun onResume() {
        super.onResume()
        /*APP_ACTIVITY.title = ""*/
        //APP_ACTIVITY.mToolbar.     ///////here's error
    }
}