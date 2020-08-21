package com.ftofs.twant.kotlin.ui.ImGoodsSearch

enum class ImGoodsEnum(val title:String,val searchType:String) {
    //            ["推薦商品","recommend","最近瀏覽","我的關注","購物袋","本店商品"

    RECOMMEND("推薦商品","recommend"),
    HISTORY("最近瀏覽","history"),
    FAVORITE("我的關注","recommend"),
    CART("購物袋","recommend"),
    OWNER("本店商品","owner");

    fun toMap(): Map<String, Any?> {
        return mapOf(this::title.name to title,this::searchType.name to searchType)
    }
}