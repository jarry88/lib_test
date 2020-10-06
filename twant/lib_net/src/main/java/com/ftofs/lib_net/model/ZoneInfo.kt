package com.ftofs.lib_net.model

import java.io.Serializable
import kotlin.collections.ArrayList

data class ZoneInfo(
        var zoneGoodsList:ArrayList<Goods>?,
        var zoneGoodsCategoryList: List<ZoneCategory>?,
        var pageEntity: PageEntity,
        var zoneStoreList: ArrayList<StoreItem>?,
        var zoneVo: ZoneVO,
        val error:String,
        val message:String,
        val checkedCategory:String
): Serializable
