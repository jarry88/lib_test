package com.ftofs.lib_net.model

data class SellerPageVO<T>(
        var goodsList: ArrayList<T>,
        var pageEntity: PageEntity,
        val error:String
)