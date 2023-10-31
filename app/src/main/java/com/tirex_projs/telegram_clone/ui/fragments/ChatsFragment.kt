package com.tirex_projs.telegram_clone.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tirex_projs.telegram_clone.R
import com.tirex_projs.telegram_clone.databinding.FragmentChatsBinding
import com.tirex_projs.telegram_clone.databinding.FragmentEnterPhoneNumBinding

class ChatsFragment : BaseFragmentPrior<FragmentChatsBinding>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textView.text = "123Chats"
    }
    override fun onResume() {
        super.onResume()
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChatsBinding {
        return FragmentChatsBinding.inflate(inflater, container, false)
    }


}