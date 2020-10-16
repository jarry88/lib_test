package com.ftofs.lib_net.model

import java.io.Serializable

data class CouponDetailVo(
    val appointmentNum: Int,
    val appointmentNumType: Int,
    val consumptionType: Int,
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
    val originalPrice: Int,
    val parkingType: Int,
    val picIds: String,
    val picList: List<String>?,
    val pkgList: List<Pkg>,
    val platformId: Int,
    val platformStoreId: Int,
    val price: Int,
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
):Serializable

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
    val lat: Double,
    val lnt: Double,
    val name: String,
    val platformName: String,
    val platformStoreId: Int
):Serializable

data class ChildItem(
    val price: String,
    val title: String,
    val unit: String
):Serializable