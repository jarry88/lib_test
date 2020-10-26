package com.ftofs.lib_net.model

data class PageEntity(
    var total: Int,
    var totalPage: Int,
    var hasMore: Boolean,
    
    var curPage: Int=1,
    var offset: Int=1,
    var over: Boolean=false,
    var pageCount: Int=1,
    var size: Int=20

)