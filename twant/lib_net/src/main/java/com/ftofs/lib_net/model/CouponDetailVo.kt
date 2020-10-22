package com.ftofs.lib_net.model

import java.io.Serializable

data class CouponDetailVo(
    val appointmentNum: Int,//appointmentNum提前（多少）單位預約
    val appointmentNumType: Int,//提前多少（單位）預約，0->小時，1->天
    val consumptionType: Int,//消費類型：0->抵用券，1->套餐券
    val cover: String,
    val createTime: String,
    val des: String,
    val freeWifi: Boolean,
    val id: Int,
    val limitBuy: Boolean,
    val limitBuyNum: Int,
    val limitPay: Boolean,
    val limitPayNum: Int,
    val limitStock: Boolean,
    val limitUse: Boolean,
    val limitUseNum: Int,
    val limitUsePeople: Boolean,
    val limitUsePeopleNum: Int,
    val needAppointment: Boolean,
    val originalPrice: Double,
    val parkingType: Int,
    val picIds: String,
    val picList: List<String>?,
    val pkgList: List<Pkg>,
    val platformId: Int,
    val platformStoreId: Int,
    val price: Double,
    val publishStatus: Int,
    val sale: Int,
    val sceneId: Int,
    val sceneName: String,
    val sort: Int,
    val stock: Int,
    val store: Store,
    val storeId: Int,
    val storeName: String,
    val subTitle: String,
    val supportRefund: Boolean,
    val tips: String,
    val title: String,
    val typeId: Int,
    val typeName: String,
    val updateTime: String,
    val usageTimeType: Int,
    val useTimeText: String,
    val validityDate: String,
    val validityDay: Int,
    val validityEndDate: String,
    val validityStartDate: String,
    val validityType: Int
):Serializable{
    fun getParkingTypeString():String= when(parkingType){
        0->"無停车位"
        1->"提供免費停車位"
        2->"提供收費停車位"
        else ->"停車位信息咨詢商戶"
    }
    fun getFreeWifiString():String= if(freeWifi) "WIFI免費" else "WIFI需收費"
    fun getImgSize():String= picList?.size?.toString() ?:"0"
    fun getShowPicList():Boolean= picList?.isNotEmpty() ?:false
}

data class Pkg(
    val childItems: List<ChildItem>,
    val title: String
):Serializable

data class Store(
    val address: String,
    val avatar: String,
    val contact: String,
    val id: Int,
    val industry: String,
    val lat: Double?,
    val lnt: Double?,
    val name: String,
    val platformName: String,
    val platformStoreId: Int
):Serializable

data class ChildItem(
    val price: String,
    val title: String,
    val unit: String
):Serializable