package com.ftofs.lib_net.model

import com.gzp.lib_common.utils.SLog
import java.io.Serializable
data class CouponOrdersListInfo(
        val list: List<CouponOrderBase>?,
        val pageEntity:PageEntity
):Serializable
data class CouponOrderDetailInfo(
        val buyerRemark: String?,//買家留言
        val cancelTime:Int?,//cancelTime
        val createTime:String?,//cancelTime
        val id:Int?,//cancelTime
        val itemList:List<OrderItem>?,//cancelTime
        val orderPrice:Double?,//訂單金額（包含優惠折扣）
        val orderSn:String?,//訂單號
        val orderStatus:Int?,//	訂單狀態：10待支付，20已支付（可使用），30已使用，40退款中，50已退款，60已取消
        val orderType:Int?,//後端  訂單類型
        val payPrice:Double?,//實際支付金額
        val payStatus:Int?,//付款狀態，10->未支付，20->已付款
        val payTime:Int?,//payTime 付款時間，10位時間戳
        val payType:Int?,//支付方式，10->mpay
        val storeId:Int?,//支付方式，10->mpay
        val userId:Int?,//支付方式，10->mpay
        val userName:String?,//下單用戶名
        val totalPrice:Double?,//商品總金額（不包含優惠折扣）
        val transactionId:String?,//外部訂單號（不包含優惠折扣）
        val updatePrice:Double?,//後台修改的價格
        val updateTime:String?,//更新時間
):Serializable

/**
 * 訂單VO
 */
data class OrderItem(
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
    val coupon:CouponDetailVo?,
    val extractCode:List<OrderCodeVo>?
):Serializable{
    fun getValidityString():String =validityType?.let {
        when(it){
            0 -> "$validityDay 天内有效"
            1 -> validityStartDate+"至" +validityEndDate
            else -> ""
        }
    }?:""
    fun getOrderStateString():String =validityType?.let {
        when(it){
            0 -> "$validityDay 天内有效"
            1 -> validityStartDate+"至" +validityEndDate
            else -> ""
        }
    }?:""
    fun getTypeString():String =consumptionType?.let {//	消費類型：0->抵用券，1->套餐券
        "团"
    }?:""
    fun getCouponItemVo():CouponItemVo = CouponItemVo(consumptionType,cover,id,originalPrice,price,sale,stock,subTitle,title,validityDay,validityEndDate,validityStartDate,validityType)//,1
}

/**
 * 訂單基類
 */
data class CouponOrderBase(
        val createTime: String?,
        val id: Int?,
        val itemList: List<OrderItem>?,
        val orderPrice: Double?,
        val orderSn: String?,
        val orderStatus: Int?,//10待支付，20可使用），30已使用，40退款中，50已退款，60已取消
        val payStatus: Int?,//付款狀態，10->未支付，20->已付款
        val userId: Int?,//標題

):Serializable{
    fun getOrderStatusString():String=orderStatus?.let {
        when(orderStatus){
            10 -> "待支付"
            20 -> "可使用"
            30 -> "已完成"
            40 -> "退款中"
            50 -> "已退款"
            60 -> "已取消"
            else ->"-"
        }.apply { SLog.info("OrderStatusString: $this") }
    }?:"-"
    fun getOrderRed():Boolean=orderStatus?.let {
        when(orderStatus){
            10 ,
            20 -> true
            else ->false
        }
    }?:false
}
data class OrderCodeVo(//核銷碼列表
    val code: String?,//兌換
    val used: Boolean?,
    val useTime: String?

):Serializable