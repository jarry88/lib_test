package com.ftofs.twant.kotlin.vo

data class ZoneVO(
    var zoneId: Int,
    var zoneName: String,
    var zoneType: Int,
    var over: Boolean,
    var pageCount: Int,
    var size: Int,

    var total: Int,
    var totalPage: Int,
    var hasMore: Boolean
)