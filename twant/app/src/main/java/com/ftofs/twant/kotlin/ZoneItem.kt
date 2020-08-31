package com.ftofs.twant.kotlin

import cn.snailpad.easyjson.EasyJSONObject

data class ZoneItem(val zoneId:Int,val zoneName:String,val webLogo:String,val appLoge:String) {
    companion object {
        @JvmStatic
        fun parase(easyJSONObject: EasyJSONObject): ZoneItem {
            return ZoneItem(easyJSONObject.getInt("zoneId")
                    ,easyJSONObject.getSafeString("zoneName")
                    ,easyJSONObject.getSafeString("webLogo")
                    ,easyJSONObject.getSafeString("appLoge"))
        }
    }
}
