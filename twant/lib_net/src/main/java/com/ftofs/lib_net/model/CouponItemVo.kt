package com.ftofs.lib_net.model

import java.io.Serializable

data class CouponItemVo(
    val consumptionType: Int?,
    val cover: String?,
    val id: Int?,
    val originalPrice: Double?,
    val price: Double?,
    val sale: Int?,
    val stock: Int?,//庫存
    val subTitle: String?,//副標題
    val title: String?,//標題
    val validityDay: Int?,
    val validityEndDate: String?,
    val validityStartDate: String?,
    val validityType: Int?//有效期類型：0->相對時間，1->指定時間
):Serializable{
    fun getValidityString():String =validityType?.let {
        when(it){
            0 -> "$validityDay 天内有效"
            1 -> validityStartDate+"至" +validityEndDate
            else -> ""
        }
    }?:""
    fun getTypeString():String =consumptionType?.let {//	消費類型：0->抵用券，1->套餐券
        "团"
    }?:""
}