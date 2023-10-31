package com.tirex_projs.telegram_clone.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.tirex_projs.telegram_clone.MainActivity
import com.tirex_projs.telegram_clone.R
import com.tirex_projs.telegram_clone.activities.AuthControlActivity
import com.tirex_projs.telegram_clone.databinding.FragmentEnterPhoneNumBinding
import com.tirex_projs.telegram_clone.utilits.*
import java.util.concurrent.TimeUnit

class EnterPhoneNumFragment : BaseFragmentPrior<FragmentEnterPhoneNumBinding>() {
    private lateinit var mPhoneNumber: String
    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    //private lateinit var mAuth : FirebaseAuth
    override fun onStart() {
        super.onStart()
        binding.authBtnNext.setOnClickListener { sendCode() }
        hideKeyboard()
        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        showToastFrag("welCUM")
                        (activity as AuthControlActivity).replaceActivity(MainActivity())
                    } else {
                        showToastFrag(task.exception?.message.toString())
                    }
                }
            }
            override fun onVerificationFailed(p0: FirebaseException) {
                showToastFrag(p0.message.toString())
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                replaceFragment(EnterAuthPhoneCodeFragment(mPhoneNumber, id), R.id.AuthDataContainer)
            }
        }
    }

    /*override fun onStart() {
        super.onStart()
        binding.authBtnNext.setOnClickListener { sendCode() }
    }*/

    private fun sendCode() {
        if (binding.authInputPhoneNumEditText.text.toString().isEmpty()){
            showToastFrag(getString(R.string.auth_passcode_top_label))
        } else {
           authUser()
            //replaceFragment(EnterAuthPhoneCodeFragment(mPhoneNumber, id), R.id.AuthDataContainer)
        }
    }

    private fun authUser() {
        mPhoneNumber = binding.authInputPhoneNumEditText.text.toString()
        var authOptions = PhoneAuthOptions.newBuilder(/*FirebaseAuth.getInstance()*/)
            .setPhoneNumber(mPhoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity as AuthControlActivity)
            .setCallbacks(mCallback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(authOptions)
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEnterPhoneNumBinding {
        return FragmentEnterPhoneNumBinding.inflate(inflater, container, false)    }
}