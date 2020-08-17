package com.ftofs.twant.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.ftofs.twant.BlankFragment
import com.ftofs.twant.R
import com.lyrebirdstudio.aspectratiorecyclerviewlib.aspectratio.model.AspectRatio
import com.lyrebirdstudio.croppylib.main.CropRequest
import com.lyrebirdstudio.croppylib.main.CroppyTheme
import com.lyrebirdstudio.croppylib.main.StorageType
import com.lyrebirdstudio.croppylib.util.file.FileCreator
import com.lyrebirdstudio.croppylib.util.file.FileOperationRequest

fun croppyExampleParams(context:Context,requestCode:Int,uri: Uri): CropRequest {

    //Saves to external and return uri
    val externalCropRequest = CropRequest.Auto(
            sourceUri = uri,
            requestCode = requestCode
    )

    //Saves to cache and return uri
    val cacheCropRequest = CropRequest.Auto(
            sourceUri = uri,
            requestCode = requestCode,
            storageType = StorageType.CACHE
    )

    // Save to given destination uri.
    val destinationUri =
            FileCreator
                    .createFile(FileOperationRequest.createRandom(), context)
                    .toUri()

    val manualCropRequest = CropRequest.Manual(
            sourceUri = uri,
            destinationUri = destinationUri,
            requestCode = requestCode
    )

    val excludeAspectRatiosCropRequest = CropRequest.Manual(
            sourceUri = uri,
            destinationUri = destinationUri,
            requestCode = requestCode,
            excludedAspectRatios = arrayListOf(AspectRatio.ASPECT_FREE)
    )

    val themeCropRequest = CropRequest.Manual(
            sourceUri = uri,
            destinationUri = destinationUri,
            requestCode = requestCode,
            croppyTheme = CroppyTheme(R.color.blue)
    )
    return CropRequest.Manual(
            sourceUri = uri,
            destinationUri = destinationUri,
            requestCode = requestCode,
            croppyTheme = CroppyTheme(R.color.blue)
    )
}