package com.ftofs.twant.kotlin

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ftofs.twant.TwantApplication
import com.ftofs.twant.entity.SellerGoodsItem
import com.ftofs.twant.kotlin.`interface`.ApiIMP
import com.ftofs.twant.kotlin.bean.ZoneInfo
import com.ftofs.twant.kotlin.net.BaseRepository
import com.ftofs.twant.kotlin.net.Result
import com.ftofs.twant.kotlin.vo.SellerPageVO
import com.ftofs.twant.kotlin.vo.ZoneVO
import com.ftofs.twant.log.SLog
import com.ftofs.twant.util.User
import com.wzq.mvvmsmart.base.BaseViewModelMVVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
class ImGoodsViewModel(application:Application):BaseViewModelMVVM(application){


    suspend fun api()=object :BaseRepository(){
        suspend fun getZoneStoreList(zoneId: Int,page: Int): Result<ZoneInfo> {
            return safeApiCall(call = { executeResponse(api.getShoppingZoneStore(zoneId,page)) })
        }
    }
    companion object{
        val b=object :BaseRepository(){
            suspend fun getZoneStoreList(zoneId: Int,page: Int): Result<ZoneInfo> {
                return safeApiCall(call = { executeResponse(api.getShoppingZoneStore(zoneId,page)) })
            }
        }
    }
}


