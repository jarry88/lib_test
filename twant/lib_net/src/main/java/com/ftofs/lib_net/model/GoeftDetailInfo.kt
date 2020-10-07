package com.ftofs.lib_net.model

import java.io.Serializable

data class GoeftDetailInfo(
        val url:String?,
        val xProportion:String?,
        val yProportion:String?,
        val originalWidth:String?,
        val originalHeight	:String?,
        val hotZoneList:List<HotZone>?,
):Serializable

