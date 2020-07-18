package com.ftofs.twant.kotlin

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
data class ZoneCategory(
        val categoryId:String,
        val zoneId:Int,
        var categoryName:String,
        val nextList:ArrayList<ZoneCategory>,
        var fold:Int=1  //0展开 1合上
): Serializable