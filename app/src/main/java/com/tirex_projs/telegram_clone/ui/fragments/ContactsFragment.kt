package com.tirex_projs.telegram_clone.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.tirex_projs.telegram_clone.databinding.ContactItemBinding
import com.tirex_projs.telegram_clone.databinding.FragmentContactsBinding
import com.tirex_projs.telegram_clone.models.CommonModel
import com.tirex_projs.telegram_clone.utilits.*
import de.hdodenhof.circleimageview.CircleImageView

class ContactsFragment : BaseFragmentFromDrawer<FragmentContactsBinding>() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: FirebaseRecyclerAdapter<CommonModel, ContactsHolder>
    private lateinit var mRefContacts : DatabaseReference
    private lateinit var mRefUsers: DatabaseReference
    private lateinit var mRefUsersListener: AppValueEventListener
    private var mapListeners = hashMapOf<DatabaseReference, AppValueEventListener>()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentContactsBinding {
        return FragmentContactsBinding.inflate(inflater, container, false)
    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Contacts"
        initRecycleView()
    }

    private fun initRecycleView() {
        mRecyclerView = binding.contactsRecyclerView
        mRefContacts = REF_DATABASE_ROOT.child(NODE_PHONES_CHILD_CONTACTS).child(CURRENT_UID)

        val options = FirebaseRecyclerOptions.Builder<CommonModel>()
            .setQuery(mRefContacts, CommonModel::class.java)
            .build()
        mAdapter = object: FirebaseRecyclerAdapter<CommonModel, ContactsHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
                val itemBinding = ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ContactsHolder(itemBinding)
            }

            override fun onBindViewHolder(
                holder: ContactsHolder,
                position: Int,
                model: CommonModel
            ) {
                mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS).child(model.id)
                mRefUsersListener = AppValueEventListener {
                    val contact = it.getCommonModdel()
                    holder.name.text = contact.fullname
                    holder.status.text = contact.state
                    holder.photo.downloadAndSetImage(contact.photoURL)
                    holder.itemView.setOnClickListener { SingleChatFragment(contact) }
                }
                mRefUsers.addValueEventListener(mRefUsersListener)
                mapListeners[mRefUsers] = mRefUsersListener

            }
        }
        mRecyclerView.adapter = mAdapter
        mAdapter.startListening()
    }


    class ContactsHolder(private val itemBinding: ContactItemBinding) : RecyclerView.ViewHolder(itemBinding.root){
        val name: TextView = itemBinding.contactFullname
        val status : TextView = itemBinding.contactStatus
        val photo : CircleImageView = itemBinding.contactPhoto
    }

    override fun onPause() {
        super.onPause()
        mAdapter.stopListening()
        println()
        mapListeners.forEach{
            it.key.removeEventListener(it.value)
        }
        println()
    }
}



