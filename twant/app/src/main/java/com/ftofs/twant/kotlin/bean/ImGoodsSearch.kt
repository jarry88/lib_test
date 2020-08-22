package com.ftofs.twant.kotlin.bean

import com.ftofs.twant.domain.store.StoreLabel
import com.ftofs.twant.entity.Goods
import com.ftofs.twant.entity.StoreItem
import com.ftofs.twant.kotlin.ZoneCategory
import com.ftofs.twant.kotlin.vo.PageEntity
import com.ftofs.twant.kotlin.vo.ZoneVO
import java.io.Serializable
import kotlin.collections.ArrayList

data class ImGoodsSearch(
        var goodsList:ArrayList<Goods>?,
        var storeLabelList: ArrayList<StoreLabel>?,
        var pageEntity: PageEntity,
        val error:String
//        val message:String,
): Serializable
