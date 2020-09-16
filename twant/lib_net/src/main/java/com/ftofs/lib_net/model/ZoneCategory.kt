package com.ftofs.lib_net.model

import java.io.Serializable
data class ZoneCategory(
        val categoryId:String,
        val zoneId:Int,
        var categoryName:String,
        val nextList:ArrayList<ZoneCategory>,
        var fold:Int  //json解析到的无字段不会使用默认初始值
): Serializable