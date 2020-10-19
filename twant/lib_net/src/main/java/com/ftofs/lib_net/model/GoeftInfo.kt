package com.ftofs.lib_net.model

import java.io.Serializable

data class GoeftInfo(
        val pageEntity:PageEntity,
        val propertyList:List<PropertyVo>?,
        val property:PropertyVo?,
        val user:GoeftUser?,
        val error:String?
):Serializable