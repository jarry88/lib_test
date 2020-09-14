package com.ftofs.twant.kotlin

import android.app.Application
import com.ftofs.twant.kotlin.bean.ZoneInfo
import com.ftofs.twant.kotlin.net.BaseRepository
import com.gzp.lib_common.net.Result
import com.wzq.mvvmsmart.base.BaseViewModelMVVM

class ImGoodsViewModel(application:Application):BaseViewModelMVVM(application){


    suspend fun api()=object : BaseRepository(){
        suspend fun getZoneStoreList(zoneId: Int,page: Int): Result<ZoneInfo> {
            return safeApiCall(call = { executeResponse(api.getShoppingZoneStore(zoneId,page)) })
        }
    }
    companion object{
        val b=object : BaseRepository(){
            suspend fun getZoneStoreList(zoneId: Int,page: Int): Result<ZoneInfo> {
                return safeApiCall(call = { executeResponse(api.getShoppingZoneStore(zoneId,page)) })
            }
        }
    }
}


