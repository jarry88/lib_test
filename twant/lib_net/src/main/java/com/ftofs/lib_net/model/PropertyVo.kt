package com.ftofs.lib_net.model

import java.io.Serializable


data class PropertyVo(
        val additionalFeaturesType: String,
        val age: Int,
        val area: String,//City的細分，如：澳門再分：澳門半島、南灣、皇朝
        val brokeragePercent: Int,
        val brokerageType: Int,
        val building: String,
        val buildingArea: Int,
        val city: String,
        val contactHits: Int,
        val cooperation: Int,
        val cover: String,
        val coverkey: Int,
        val decoration: Int,
        val decorationDescription: String,//裝修描述
        val detail: String,
        val developers: String,
        val direction: Int,
        val editDate: String,
        val email: String,
        val expireDate: String,
        val floor: String?,
        val furniture: String,
        val goodPick: Int,
        val handPick: Int,
        val hits: Int,
        val intervalHall: Int,
        val intervalRoom: Int,
        val intervalStoreRooms: Int,
        val intervalTerrace: Int,
        val intervalToilet: Int,
        val isLift: Int,
        val isNew: Int,
        val isParkingSpaces: Int,
        val isStatus: Int,
        val isTop: Int,
        val isType: Int,
        val landscape: String,
        val landscapeDescription: String,
        val lease: Int,
        val linkMan: String,
        val loftDescribe: Int,//閣樓
        val mHandPick: Int,
        val map_X: String,
        val map_Y: String,
        val mobile: String,
        val other: Int,
        val parkingSpaces: String,
        val picAmount: Int,
        val pid: Int,
        val propertyCorp: String,//銷售商、地產公司
        val propertyName: String,
        val pubDate: String,
        val remark: String,
        val rentReservePrice: Int,
        val rentalPrice: Int,
        val roomType: Int,
        val salableArea: Int,
        val saleDate: Any,
        val saleType: Int,
        //todo selected 屬性問題
        val selected: Boolean,
        val sellReservePrice: Int,
        val sellingPrice: Int,
        val sequence: Int,
        val source: Int,
        val street: String?,
        val tel: String,
        val tid: Int,
        val ui_Other: Int,
        val ui_isStatus: Int,
        val uid: Int,
        val unit: String?,//單位：
        val unitPrice: Int,
        val photoList:List<GoPhoto>,
        val pingJunPrice:Double
):Serializable{
    fun getItemTitle():String=propertyName.plus(floor?.let { " $it" }?:"").plus(unit?.let { " $it" }?:"").plus(street?.let { " $it" }?:"").plus(street?.let { " $it" }?:"")
//    {
//        floor?.takeIf{it.isNotEmpty()}?.let { this.plus(" $it") }
//        unit?.takeIf{it.isNotEmpty()}?.let { this.plus(" $it") }
//        street?.takeIf{it.isNotEmpty()}?.let { this.plus(" $it") }
//    }
    fun getTypeName():String=when(isType){//房產類型（用整形數填寫）：1 : 住宅 2 : 商鋪 3 : 車位 4 : 工業 5 : 地皮 6 : 寫字樓 7 : 別墅
        1->"住宅"
        2->"商鋪"
        3->"車位"
        4->"工業"
        5->"地皮"
        6->"寫字樓"
        7->"別墅"
        else ->""
    }
    fun getEditTimeString():String =editDate.let { if(it.length>10)it.substring(0,10) else it }
    fun getLiftString():String=when(isLift){//是否有電梯，0：否，1：是
        0 ->"否"
        1 ->"是"
        else ->""
    }
    fun getLoftString():String=when(loftDescribe){//是否有閣樓，0：無，1：有
        0 ->"無"
        1 ->"有"
        else ->""
    }
    //todo 待確定補全的屬性
    fun getDirectionString():String=when(loftDescribe){//座向，0：未知，1：是
        0 ->"未知"
        1 ->"有"
        else ->""
    }
    fun getNewString():String=when(loftDescribe){//新舊，0：不詳，1：新裝
        0 ->"不詳"
        1 ->"新裝"
        else ->""
    }
    fun getAgeString():String="${age}年"
    fun getIntervalName():String=(if(intervalRoom>0)"${intervalRoom}房" else "").apply {
        plus(if(intervalRoom>0)"${intervalRoom}房" else "")
    }.apply {
        plus(if(intervalHall>0)"${intervalHall}廳" else "")
    }.apply {
        intervalStoreRooms.takeIf { it>0 }?.let { this.plus("${it}雜物") }
    }.apply {
        intervalTerrace.takeIf { it>0 }?.let { this.plus("${it}露臺") }
    }.apply {
        intervalToilet.takeIf { it>0 }?.let { this.plus("${it}衛") }
    }
    fun getSaleTypeString():String=when(saleType){//租售，1：租，2：售
        1 ->"出租"
        2 ->"出售"

        else ->""
    }
    //todo 待確定補全的屬性
    fun getParkingString():String=when(isParkingSpaces){//車位，0：無，1：有
        0 ->"無"
        1 ->"有"
        else ->""
    }
    fun getPriceText():String=when(saleType){
        1->"租 $"+getRentalPriceString()
        2->"售 $" +getSellingPriceString()
        else ->""
    }
    //todo 待確定補全的屬性
    fun getRentalPriceString():String=when(rentalPrice){//租價，0：面議，1：有
        0 ->"面議"
        else ->"${rentalPrice}元/月"
    }
    //todo 待確定補全的屬性
    fun getSellingPriceString():String=when(sellingPrice){//租價，0：面議，1：**萬
        0 ->"面議"
        else ->"${sellingPrice}萬"
    }
    //todo 待確定補全的屬性
    fun getLeaseString():String=when(lease){//帶租約：，0：不帶，1：帶
        0 ->"不帶"
        1 ->"帶"
        else ->""
    }
    //todo 待確定補全的屬性
    fun getDecorationString():String=when(decoration){//裝修：不詳：不帶，1：帶
        0 ->"不詳"
        1 ->"帶"
        else ->""
    }
    //todo 待確定補全的屬性
    fun getSourceString():String=when(source){//樓盤來源：：，0：不帶，1：帶
        0 ->" GO853 "
        1 ->"帶"
        else ->"GO853"
    }
    fun getBuildingAreaString():String=if(buildingArea>0) "$buildingArea 呎" else ""  //建築面積
    fun getSalableAreaString():String=if(salableArea>0) "$salableArea 呎" else ""  //實用面積
    fun getAveragePrice():String=if(pingJunPrice>0)"$pingJunPrice 元" else ""  //實用面積
}
data class GoPhoto(
        val isDefault: Int,
        val monthPic: Int,
        val photoId: Int,
        val remark: String,
        val rollShow: Int,
        val sortimg: Int,
        val title: String
):Serializable
data class GoeftUser(
        val userPhoto: String?,
        val tel: String?,
        val propertyCorp: String?,//銷售商、地產公司
        val mobile: String?,//聯繫手機號
        val email: String?,//聯繫郵箱
        val linkMan: String?//聯繫人
):Serializable

