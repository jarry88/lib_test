package com.ftofs.twant

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.ftofs.twant.fragment.BaseFragment
import com.ftofs.twant.log.SLog
import com.google.android.material.button.MaterialButton
import com.lyrebirdstudio.aspectratiorecyclerviewlib.aspectratio.model.AspectRatio
import com.lyrebirdstudio.croppylib.Croppy
import com.lyrebirdstudio.croppylib.main.CropRequest
import com.lyrebirdstudio.croppylib.main.CroppyTheme
import com.lyrebirdstudio.croppylib.main.StorageType
import com.lyrebirdstudio.croppylib.util.file.FileCreator
import com.lyrebirdstudio.croppylib.util.file.FileOperationRequest

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val  imageViewCropped by lazy {
        view?.findViewById<AppCompatImageView>(R.id.imageViewCropped)
    }
    private val  buttonChoose by lazy {
        view?.findViewById<Button>(R.id.buttonChoose)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                BlankFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
        private const val RC_CROP_IMAGE = 102
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        buttonChoose?.setOnClickListener{
            startCroppy()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_CROP_IMAGE) {
            data?.data?.let {
                SLog.info("TEST Croppy Data:[%s]",data.toString())

                imageViewCropped?.setImageURI(it)
            }
        }
    }

    private fun startCroppy() {
        val uri = Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(R.drawable.aa))
                .appendPath(resources.getResourceTypeName(R.drawable.aa))
                .appendPath(resources.getResourceEntryName(R.drawable.aa))
                .build()

        //Saves to external and return uri
        val externalCropRequest = CropRequest.Auto(
                sourceUri = uri,
                requestCode = RC_CROP_IMAGE
        )

        //Saves to cache and return uri
        val cacheCropRequest = CropRequest.Auto(
                sourceUri = uri,
                requestCode = RC_CROP_IMAGE,
                storageType = StorageType.CACHE
        )

        // Save to given destination uri.
        val destinationUri =
                FileCreator
                        .createFile(FileOperationRequest.createRandom(), _mActivity)
                        .toUri()

        val manualCropRequest = CropRequest.Manual(
                sourceUri = uri,
                destinationUri = destinationUri,
                requestCode = RC_CROP_IMAGE
        )

        val excludeAspectRatiosCropRequest = CropRequest.Manual(
                sourceUri = uri,
                destinationUri = destinationUri,
                requestCode = RC_CROP_IMAGE,
                excludedAspectRatios = arrayListOf(AspectRatio.ASPECT_FREE)
        )

        val themeCropRequest = CropRequest.Manual(
                sourceUri = uri,
                destinationUri = destinationUri,
                requestCode = RC_CROP_IMAGE,
                croppyTheme = CroppyTheme(R.color.blue)
        )

        activity?.let { Croppy.start(it, themeCropRequest) }
    }

}