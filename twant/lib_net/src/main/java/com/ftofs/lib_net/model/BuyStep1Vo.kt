package com.ftofs.lib_net.model

import java.io.Serializable

data class BuyStep1Vo(
        val couponBaseList:List<CouponItemVo>?,
        val totalPrice:Double?
):Serializable
data class BuyGoodsDTO(
        val goodsId:Int?,
        val goodsNum:Int?
):Serializable