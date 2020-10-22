package com.ftofs.lib_net.model

import java.io.Serializable
data class CouponOrdersListInfo(
        val list: List<CouponOrderBase>?,
        val pageEntity:PageEntity
):Serializable
data class CouponOrderItemVo(
    val consumptionType: Int?,//消費類型：0->抵用券，1->套餐券
    val cover: String?,
    val id: Int?,
    val goodsId: Int?,//商品ID
    val orderId: Int?,//商品ID
    val userId: Int?,//商品ID
    val num: Int?,//購買數量
    val originalPrice: Double?,
    val price: Double?,
    val sale: Int?,
    val stock: Int?,//庫存
    val subTitle: String?,//副標題
    val title: String?,//標題
    val validityDay: Int?,
    val validityEndDate: String?,
    val validityStartDate: String?,
    val validityType: Int?,//有效期類型：0->相對時間，1->指定時間
    val coupon:CouponItemVo?,
    val extractCode:OrderCodeVo?
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
data class CouponOrderBase(
    val createTime: String?,
    val id: Int?,
    val itemList: List<CouponOrderItemVo>?,
    val orderPrice: Double?,
    val orderSn: String?,
    val orderStatus: Int?,//庫存
    val payStatus: Int?,//付款狀態，10->未支付，20->已付款
    val userId: Int?,//標題

):Serializable
data class OrderCodeVo(//核銷碼列表
    val code: String?,//兌換
    val used: Boolean?,
    val useTime: String?

):Serializable