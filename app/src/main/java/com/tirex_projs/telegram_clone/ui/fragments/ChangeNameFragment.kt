package com.tirex_projs.telegram_clone.ui.fragments

import android.view.*
import com.tirex_projs.telegram_clone.R
import com.tirex_projs.telegram_clone.databinding.FragmentChangeNameBinding
import com.tirex_projs.telegram_clone.utilits.*

class ChangeNameFragment : BaseFragmentSettingsChanges<FragmentChangeNameBinding>() {
    override fun onResume() {
        super.onResume()
        initFullnameList()
    }

    private fun initFullnameList() {
        val fullnameList = USER.fullname.split(" ")
        if (fullnameList.size > 1) {
            binding.settingsInputName.setText(fullnameList[0])
            binding.settingsInputSurname.setText(fullnameList[1])
        } else {
            binding.settingsInputName.setText(fullnameList[0])
        }
    }

    override fun change() {
        super.change()
        val name = binding.settingsInputName.text.toString()
        val surname = binding.settingsInputSurname.text.toString()
        if (name.isEmpty()){
            showToastFrag(getString(R.string.settings_changeName_toast_emptyName))
        } else {
            val fullname = "$name $surname"
            REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(NODE_USERS_CHILD_FULLNAME)
                .setValue(fullname).addOnCompleteListener{
                    if (it.isSuccessful){
                        showToastFrag(getString(R.string.settings_changeName_toast_success))
                        USER.fullname = fullname
                        APP_ACTIVITY.mAppDrawer.updateHeader()
                        parentFragmentManager.popBackStack()
                    }
                }
        }
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChangeNameBinding {
        return FragmentChangeNameBinding.inflate(inflater, container, false)
    }


}