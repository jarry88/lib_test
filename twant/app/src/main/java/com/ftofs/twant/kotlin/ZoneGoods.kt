package com.ftofs.twant.kotlin

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
data class ZoneGoods(
        val commonId:Int,
        val goodsName:String,
        val goodsImage:String,
        val goodsFullSpecs:String
): Serializable