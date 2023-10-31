package com.tirex_projs.telegram_clone.ui.fragments

import android.view.*
import com.tirex_projs.telegram_clone.R
import com.tirex_projs.telegram_clone.databinding.FragmentChangeUsernameBinding
import com.tirex_projs.telegram_clone.utilits.*
import java.util.*

class ChangeUsernameFragment : BaseFragmentSettingsChanges<FragmentChangeUsernameBinding>() {

    private lateinit var mNewUsername : String

    override fun onResume() {
        super.onResume()
        binding.settingsInputUsername.setText(USER.username)
    }

    override fun change() {
        super.change()
        mNewUsername = binding.settingsInputUsername.text.toString().lowercase(Locale.getDefault())
        if (mNewUsername.isEmpty()) {
            showToastFrag("Field is empty")
        } else {
            REF_DATABASE_ROOT.child(NODE_USERNAMES)
                .addListenerForSingleValueEvent(AppValueEventListener{
                    if (it.hasChild(mNewUsername)){
                        showToastFrag("Current username is unavailable")
                    } else {
                        changeUsername()
                    }
                })
        }
    }

    private fun changeUsername() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(mNewUsername).setValue(CURRENT_UID)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    updateCurrentUsername()
                }
            }
    }

    private fun updateCurrentUsername() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(NODE_USERS_CHILD_USERNAME)
            .setValue(mNewUsername)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    showToastFrag(getString(R.string.Universal_toast_info_upgraded_successfully))
                    deleteOldUsername()
                } else {
                    showToastFrag(it.exception?.message.toString())
                }
            }
    }

    private fun deleteOldUsername() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(USER.username).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    showToastFrag(getString(R.string.Universal_toast_info_upgraded_successfully))
                    parentFragmentManager.popBackStack()
                    USER.username = mNewUsername
                } else {
                    showToastFrag(it.exception?.message.toString())
                }
            }
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChangeUsernameBinding {
        return FragmentChangeUsernameBinding.inflate(inflater, container, false)
    }

}