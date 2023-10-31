package com.tirex_projs.telegram_clone.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tirex_projs.telegram_clone.R
import com.tirex_projs.telegram_clone.databinding.FragmentChangeBioBinding
import com.tirex_projs.telegram_clone.utilits.*


class ChangeBioFragment : BaseFragmentSettingsChanges<FragmentChangeBioBinding>() {
    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChangeBioBinding {
        return FragmentChangeBioBinding.inflate(inflater, container, false)
    }

    override fun onResume() {
        super.onResume()
        binding.settingsInputBio.setText(USER.bio)
    }

    override fun change() {
        super.change()
        val newBio = binding.settingsInputBio.text.toString()
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(NODE_USERS_CHILD_BIO).setValue(newBio).addOnCompleteListener{
            if(it.isSuccessful){
                showToastFrag(getString(R.string.settings_changeName_toast_success))
                USER.bio = newBio
                parentFragmentManager.popBackStack()
            }
            else{
                //хз, кажется тут что-то должно бытть
            }
        }
    }

}