package com.tirex_projs.telegram_clone.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.tirex_projs.telegram_clone.MainActivity
import com.tirex_projs.telegram_clone.activities.AuthControlActivity
import com.tirex_projs.telegram_clone.databinding.FragmentEnterAuthPhoneCodeBinding
import com.tirex_projs.telegram_clone.utilits.*

class EnterAuthPhoneCodeFragment(private val phoneNumber: String, val id: String) : BaseFragmentPrior<FragmentEnterAuthPhoneCodeBinding>() {
    private lateinit var mAuth: FirebaseAuth
    override fun onStart() {
        super.onStart()
        (activity as AuthControlActivity).title = phoneNumber
        binding.authInputPassCodeEditText.addTextChangedListener(AppTextWatcher {
            val string = binding.authInputPassCodeEditText.text.toString()
            if (string.length == 6) {
                enterCode()
            }
        })
        hideKeyboard()
    }

    private fun enterCode() {
        val code = binding.authInputPassCodeEditText.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful){
                val uid = AUTH.currentUser?.uid.toString()

                val dataMap = mutableMapOf<String, Any>()
                dataMap[NODE_USERS_CHILD_ID] = uid
                dataMap[NODE_USERS_CHILD_PHONE] = phoneNumber
                //dataMap[NODE_USERS_CHILD_USERNAME] = uid// placeholder for starter, user could change it
                REF_DATABASE_ROOT.child(NODE_PHONES).child(phoneNumber).setValue(uid)
                    .addOnFailureListener{ showToastFrag(it.message.toString()) }
                    .addOnSuccessListener {
                        REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dataMap)
                            .addOnSuccessListener {
                                showToastFrag("welCUM")
                                (activity as AuthControlActivity).replaceActivity(MainActivity())
                            }
                            .addOnFailureListener{showToast(it.message.toString())}
                    }
            } else {
                showToastFrag(task.exception?.message.toString())
            }
        }
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEnterAuthPhoneCodeBinding {
        return FragmentEnterAuthPhoneCodeBinding.inflate(inflater, container, false)
    }


}