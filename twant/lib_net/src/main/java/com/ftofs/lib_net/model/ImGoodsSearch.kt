package com.ftofs.lib_net.model

import java.io.Serializable
import kotlin.collections.ArrayList

data class ImGoodsSearch(
        var goodsList:ArrayList<Goods>?,
        var storeLabelList: ArrayList<StoreLabel>?,
        var ordersList: ArrayList<ImStoreOrderItem>?,
        var pageEntity: PageEntity,
        val error:String
//        val message:String,
): Serializable
