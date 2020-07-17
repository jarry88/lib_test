package com.ftofs.twant.kotlin

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
data class ZoneCategory(
        val categoryId:String,
        val zoneId:Int,
        val categoryName:String,
        val nextList:ArrayList<ZoneCategory>
): Serializable