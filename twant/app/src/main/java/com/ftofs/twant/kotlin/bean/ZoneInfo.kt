package com.ftofs.twant.kotlin.bean

import com.ftofs.twant.entity.Goods
import com.ftofs.twant.entity.StoreItem
import com.ftofs.twant.kotlin.ZoneCategory
import com.ftofs.twant.kotlin.vo.PageEntity
import com.ftofs.twant.kotlin.vo.ZoneVO
import java.io.Serializable
import kotlin.collections.ArrayList

data class ZoneInfo(
        var zoneGoodsList:ArrayList<Goods>?,
        var zoneGoodsCategoryList: List<ZoneCategory>?,
        var pageEntity: PageEntity,
        var zoneStoreList: ArrayList<StoreItem>?,
        var zoneVo:ZoneVO,
        val error:String,
        val message:String,
        val checkedCategory:String
): Serializable
