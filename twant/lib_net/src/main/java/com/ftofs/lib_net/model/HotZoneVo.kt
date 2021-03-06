package com.ftofs.lib_net.model

import java.io.Serializable

data class HotZoneVo(
        val url:String?,
        val xProportion:String?,
        val yProportion:String?,
        val originalWidth:String?,
        val originalHeight	:String?,
        val hotZoneList:List<HotZone>?,
):Serializable

data class HotZone(
        val x:String?,
        val y:String?,
        val width:String?,
        val height:String?,
        val linkType	:String?,
        val linkValue:String?,
)
