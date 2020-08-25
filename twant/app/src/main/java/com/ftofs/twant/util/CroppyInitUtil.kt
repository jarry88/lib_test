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
import java.io.File

fun File.toUri(): Uri = Uri.fromFile(this)
//Saves to external and return uri

//fun externalCropRequest  (context:Context,requestCode:Int,uri: Uri) = CropRequest.Auto(
//        sourceUri = uri,
//        requestCode = requestCode
//)
////Saves to cache and return uri
//
//fun cacheCropRequest  (context:Context,requestCode:Int,uri: Uri) = CropRequest.Auto(
//        sourceUri = uri,
//        requestCode = requestCode,
//        storageType = StorageType.CACHE
//)
//fun manualCropRequest  (context:Context,requestCode:Int,uri: Uri) = CropRequest.Manual(
//        sourceUri = uri,
//        // Save to given destination uri.
//
//        destinationUri = FileCreator
//                .createFile(FileOperationRequest.createRandom(), context)
//                .toUri(),
//        requestCode = requestCode
//)
//fun excludeAspectRatiosCropRequest (context:Context,requestCode:Int,uri: Uri) = CropRequest.Manual(
//        sourceUri = uri,
//        // Save to given destination uri.
//
//        destinationUri = FileCreator
//                .createFile(FileOperationRequest.createRandom(), context)
//                .toUri(),
//        requestCode = requestCode,
//        croppyTheme = CroppyTheme(R.color.blue)
//)
//fun themeCropRequest(context:Context,requestCode:Int,uri: Uri) = CropRequest.Manual(
//        sourceUri = uri,
//        // Save to given destination uri.
//
//        destinationUri = FileCreator
//                .createFile(FileOperationRequest.createRandom(), context)
//                .toUri(),
//        requestCode = requestCode,
//        croppyTheme = CroppyTheme(R.color.blue)
//)
