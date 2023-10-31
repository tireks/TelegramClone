package com.tirex_projs.telegram_clone.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.canhub.cropper.*
//import com.example.croppersample.databinding.FragmentCameraBinding
//import timber.log.Timber
import com.tirex_projs.telegram_clone.activities.SampleResultScreen
import com.tirex_projs.telegram_clone.databinding.FragmentChangeFotoBinding
import com.tirex_projs.telegram_clone.R
import com.tirex_projs.telegram_clone.utilits.*


class ChangeFotoFragment : Fragment() {

    private var _binding: FragmentChangeFotoBinding? = null
    private val binding get() = _binding!!

    private var outputUri: Uri? = null
    /*private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            startCameraWithUri()
        } else {
            showErrorMessage("taking picture failed")
        }
    }*/

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        when {
            result.isSuccessful -> {
                handleCropImageResult(result.uriContent.toString())
            }
            result is CropImage.CancelledResult -> showErrorMessage("cropping image was cancelled by the user")
            else -> showErrorMessage("cropping image failed")
        }
    }

    private val customCropImage = registerForActivityResult(CropImageContract()) {
        if (it !is CropImage.CancelledResult) {
            handleCropImageResult(it.uriContent.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentChangeFotoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*binding.takePictureBeforeCallLibraryWithUri.setOnClickListener {
            setupOutputUri()
            takePicture.launch(outputUri)
        }*/
        startCameraWithoutUri(includeCamera = true, includeGallery = true)
        /*binding.callLibraryWithoutUri.setOnClickListener {
        }*/
        /*binding.callLibraryWithoutUriCameraOnly.setOnClickListener {
            startCameraWithoutUri(includeCamera = true, includeGallery = false)
        }*/
        /*binding.callLibraryWithoutUriGalleryOnly.setOnClickListener {
            startCameraWithoutUri(includeCamera = false, includeGallery = true)
        }*/
    }

    private fun startCameraWithoutUri(includeCamera: Boolean, includeGallery: Boolean) {
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

    /*private fun startCameraWithUri() {
        cropImage.launch(
            CropImageContractOptions(
                uri = outputUri,
                cropImageOptions = CropImageOptions(),
            ),
        )
    }*/

    private fun showErrorMessage(message: String) {
        /*Timber.tag("AIC-Sample").e("Camera error: $message")*/
        Toast.makeText(activity, "Crop failed: $message", Toast.LENGTH_SHORT).show()
    }

    private fun handleCropImageResult(uri: String) {
        var uriLocal = Uri.parse(uri.replace("file:", ""))
        val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)
            .child(CURRENT_UID)
        path.putFile(uriLocal).addOnCompleteListener{ task1 ->
            if (task1.isSuccessful){
                path.downloadUrl.addOnCompleteListener { task2 ->
                    if(task2.isSuccessful){
                        val photoUri = task2.result.toString()
                        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
                            .child(NODE_USERS_CHILD_PHOTOURL).setValue(photoUri)
                            .addOnCompleteListener {task3 ->
                                if (task3.isSuccessful){
                                    /*Picasso.get()
                                        .load(photoUri)
                                        .placeholder(R.drawable.default_photo)
                                        .into(binding.header)*/
                                    showToastFrag(getString(R.string.Universal_toast_info_upgraded_successfully))
                                    USER.photoURL = photoUri
                                }
                            }

                    }
                }
            }
        }
        SampleResultScreen.start(this, null, uriLocal, null)
    }

    /*private fun setupOutputUri() {
        if (outputUri == null) {
            context?.let { ctx ->
                val authorities = "${ctx.applicationContext?.packageName}$AUTHORITY_SUFFIX"
                outputUri = FileProvider.getUriForFile(ctx, authorities, createImageFile())
            }
        }
    }*/

    /*private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "$FILE_NAMING_PREFIX${timeStamp}$FILE_NAMING_SUFFIX",
            FILE_FORMAT,
            storageDir,
        )
    }*/

    companion object {
        const val DATE_FORMAT = "yyyyMMdd_HHmmss"
        const val FILE_NAMING_PREFIX = "JPEG_"
        const val FILE_NAMING_SUFFIX = "_"
        const val FILE_FORMAT = ".jpg"
        const val AUTHORITY_SUFFIX = ".cropper.fileprovider"
    }

}