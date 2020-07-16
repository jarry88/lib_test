package com.ftofs.twant.kotlin

data class ZoneCategory(val categoryId:Int,val zoneId:Int,val categoryName:String,val nextList:List<ZoneCategory>)
