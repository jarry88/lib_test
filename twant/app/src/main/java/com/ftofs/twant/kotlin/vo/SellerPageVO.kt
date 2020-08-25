package com.ftofs.twant.kotlin.vo

data class SellerPageVO<T>(
        var goodsList: ArrayList<T>,
        var pageEntity:PageEntity,
        val error:String
)