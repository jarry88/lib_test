package com.ftofs.lib_net.model

data class PageEntity(
    var total: Int,
    var totalPage: Int,
    var hasMore: Boolean,
    
    var curPage: Int,
    var offset: Int,
    var over: Boolean,
    var pageCount: Int,
    var size: Int

)