package com.ftofs.lib_net.model

data class BannerVO(
        var id: Int,
        var title: String,
        var desc: String,
        var type: Int,
        var url: String,
        var appIndexNavigationLinkType:String
)