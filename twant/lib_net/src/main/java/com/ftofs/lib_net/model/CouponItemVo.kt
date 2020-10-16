package com.ftofs.lib_net.model

import java.io.Serializable

data class CouponItemVo(
    val consumptionType: Int?,
    val cover: String?,
    val id: Int?,
    val originalPrice: Int?,
    val price: Int?,
    val sale: Int?,
    val stock: Int?,
    val subTitle: String?,
    val title: String?,
    val validityDay: Int?,
    val validityEndDate: String?,
    val validityStartDate: String?,
    val validityType: Int?
):Serializable