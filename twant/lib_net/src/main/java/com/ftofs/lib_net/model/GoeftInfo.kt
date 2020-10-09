package com.ftofs.lib_net.model

import java.io.Serializable

data class GoeftInfo(
        val pageEntity:PageEntity,
        val propertyList:List<PropertyVo>?,
        val user:GoeftUser?
):Serializable