package com.ftofs.twant.kotlin.bean

import com.ftofs.twant.entity.Goods
import com.ftofs.twant.entity.StoreItem
import com.ftofs.twant.kotlin.CategoryItem
import com.ftofs.twant.kotlin.ZoneCategory
import com.ftofs.twant.kotlin.ZoneGoods
import com.ftofs.twant.kotlin.vo.PageVO
import com.ftofs.twant.kotlin.vo.ZoneVO
import com.ftofs.twant.vo.goods.GoodsVo
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

data class ZoneInfo(
        var zoneGoodsList:ArrayList<Goods>?,
        var zoneGoodsCategoryList: List<ZoneCategory>,
        var pageEntity: PageVO,
        var zoneStoreList: ArrayList<StoreItem>?,
        var zoneVo:ZoneVO,
        val error:String,
        val checkedCategory:String
): Serializable
