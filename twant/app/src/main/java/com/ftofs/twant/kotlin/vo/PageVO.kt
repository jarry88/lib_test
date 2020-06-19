package com.ftofs.twant.kotlin.vo

data class PageVO(
    var curPage: Int,
    var offset: Int,
    var over: Boolean,
    var pageCount: Int,
    var size: Int,

    var total: Int,
    var totalPage: Int,
    var hasMore: Boolean
)