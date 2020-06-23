package com.ftofs.twant.kotlin.util

import com.ftofs.twant.util.StringUtil

object ConverterUtil {
    @JvmStatic
    fun rangePrice(min: Double, batch: Double) :String{
        return String.format("%s MOP - %s MOP", StringUtil.formatFloat(min), StringUtil.formatFloat(batch))
    }
}