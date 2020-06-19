package com.ftofs.twant.kotlin.vo

data class SellerGoodsVO(
        var commonId: Int ,
        var goodsName: String ,
        var imageName: String,
        var goodsSpecNames: String,
        var batchPrice0: Double ,
        var appPriceMin: Double ,
        var goodsStorage: Int,
        var goodsSaleNum: Int,
        var isVirtual: Int ,
        var appUsable: Int ,
        var goodsState: Int ,
        var tariffEnable: Int ,
        var isCommend: Int
)