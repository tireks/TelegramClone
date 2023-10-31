package com.tirex_projs.telegram_clone.ui.fragments

import android.net.Uri
import android.view.*
import com.canhub.cropper.*
import com.tirex_projs.telegram_clone.MainActivity
import com.tirex_projs.telegram_clone.R
import com.tirex_projs.telegram_clone.activities.AuthControlActivity
import com.tirex_projs.telegram_clone.activities.SampleResultScreen
import com.tirex_projs.telegram_clone.databinding.FragmentSettingsBinding
import com.tirex_projs.telegram_clone.databinding.FragmentSettingsBinding.*
import com.tirex_projs.telegram_clone.utilits.*

class SettingsFragment : BaseFragmentFromDrawer<FragmentSettingsBinding>() {
    private val customCropImage = registerForActivityResult(CropImageContract()) {
        if (it !is CropImage.CancelledResult) {
            handleCropImageResult(it.uriContent.toString())
        }
    }
    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        initFields()
    }

    private fun initFields() {
        binding.settingsAccountBIOInfoTV.text = USER.bio
        binding.settingsHeaderNameTV.text = USER.fullname
        binding.settingsAccountNumberInfoTV.text = USER.phone
        binding.settingsHeaderStatusTV.text = USER.state
        binding.settingsAccountNicknameInfoTV.text = USER.username
        binding.settingsHeaderUserPhoto.downloadAndSetImage(USER.photoURL)
        binding.settingsAccountNicknameInfoBtn.setOnClickListener {
            replaceFragment(ChangeUsernameFragment(), R.id.dataContainer)
        }
        binding.settingsAccountBIOInfoBtn.setOnClickListener {
            replaceFragment(ChangeBioFragment(), R.id.dataContainer)
        }
        binding.settingsHeaderBtnChangeUserPhoto.setOnClickListener {
            changePhotoUser()
        }
    }

    private fun changePhotoUser() {
        fun startCameraWithoutUri(includeCamera: Boolean, includeGallery: Boolean) {
            customCropImage.launch(
                CropImageContractOptions(
                    uri = null,
                    cropImageOptions = CropImageOptions(
                        imageSourceIncludeCamera = includeCamera,
                        imageSourceIncludeGallery = includeGallery,
                        cropShape = CropImageView.CropShape.OVAL,
                        maxCropResultHeight = 600,
                        maxCropResultWidth = 600
                    ),
                ),
            )
        }
        startCameraWithoutUri(includeCamera = true, includeGallery = true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return inflate(inflater, container, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.settings_menu_logoff ->{
                AUTH.signOut()
                (activity as MainActivity).replaceActivity(AuthControlActivity())
            }
            R.id.settings_menu_change_name -> {
                replaceFragment(ChangeNameFragment(), R.id.dataContainer)
            }
        }
        return true
    }

    private fun handleCropImageResult(uri: String) {
        val uriLocal = Uri.parse(uri.replace("file:", ""))
        val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)
            .child(CURRENT_UID)
        putImageToStorage(uriLocal, path){
            getUrlFromStorage(path){
                putUrlToDB(it){
                    binding.settingsHeaderUserPhoto.downloadAndSetImage(it)
                    showToastFrag(getString(R.string.Universal_toast_info_upgraded_successfully))
                    USER.photoURL = it
                    APP_ACTIVITY.mAppDrawer.updateHeader()
                }
            }
        }
        SampleResultScreen.start(this, null, uriLocal, null)
    }
}