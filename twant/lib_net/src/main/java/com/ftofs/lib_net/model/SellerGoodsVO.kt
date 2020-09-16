package com.ftofs.lib_net.model

data class SellerGoodsVO (
        var commonId: Int ,
        var goodsModal: Int ,
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
        var isCommend: Int//是否為鎮店之寶 默認0 2-鎮店之寶
)