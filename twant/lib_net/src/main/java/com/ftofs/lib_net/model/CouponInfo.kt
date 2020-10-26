package com.ftofs.lib_net.model

import java.io.Serializable

data class CouponInfo(
        val pageEntity:PageEntity,
        val list:List<CouponDetailVo>?,
        val error:String?,
        val couponDetailVo: CouponDetailVo?,
        val message:String?
):Serializable