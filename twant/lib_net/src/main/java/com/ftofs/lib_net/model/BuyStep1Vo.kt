package com.ftofs.lib_net.model

import java.io.Serializable

data class BuyStep1Vo(
        val couponBaseList:List<CouponItemVo>?,
        val totalPrice:Double?
):Serializable
data class BuyStep2Vo(
        val orderId:Int?
):Serializable
data class BuyGoodsDTO(
        val goodsId:Int?,
        val goodsNum:Int?
):Serializable
data class MpayVo(
        val isPay:Boolean,
        val payData:Any?,
        val payId:Int?,
):Serializable