package com.ftofs.twant.kotlin.vo

data class SellerPageVO<T>(
    var goodsList: List<T>,
    var pageEntity:PageVO
)