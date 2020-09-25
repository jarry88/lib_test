package com.ftofs.lib_net.model

data class HotZoneVo(
        val url:String?,
        val xProportion:String?,
        val yProportion:String?,
        val originalWidth:String?,
        val originalHeight	:String?,
        val hotZoneList:List<HotZone>?,
)

data class HotZone(
        val x:String?,
        val y:String?,
        val width:String?,
        val height:String?,
        val linkType	:String?,
        val linkValue:String?,
)
