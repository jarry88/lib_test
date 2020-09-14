package com.ftofs.twant.kotlin

import com.ftofs.twant.entity.SellerGoodsItem
import com.ftofs.twant.kotlin.bean.ZoneInfo
import com.gzp.lib_common.net.BaseRepository
import com.gzp.lib_common.net.MRequest
import com.ftofs.twant.kotlin.vo.SellerPageVO
import com.gzp.lib_common.net.Result
class LinkageModel : BaseRepository(){
    suspend fun getShoppingGoodsList1(zoneId:Int): Result<SellerPageVO<SellerGoodsItem>> {

        return safeApiCall(call ={requestSearch1(zoneId)},errorMessage = "网络错误")
    }
    private suspend fun requestSearch1(zoneId:Int): Result<SellerPageVO<SellerGoodsItem>> =
            executeResponse(MRequest.getInstance().service.getShoppingZone(zoneId))


    suspend fun getZoneCategoryList(zoneId: Int): Result<ZoneInfo> {
        return safeApiCall(call ={requestZoneCategory(zoneId)},errorMessage = "网络错误")
    }
    suspend fun requestZoneCategory(zoneId: Int): Result<ZoneInfo> =
        executeResponse(MRequest.getInstance().service.doZoneCategoryList(zoneId))

    suspend fun getShoppingZoneGoods(categoryId: String, page: Int): Result<ZoneInfo> {
        return safeApiCall(call = {requestShoppingZoneGoods(categoryId,page)})
    }

    private suspend fun requestShoppingZoneGoods(categoryId: String,page: Int): Result<ZoneInfo> =
            executeResponse(api.getShoppingZoneGoods(categoryId,page))

    suspend fun getZoneStoreList(zoneId: Int,page: Int): Result<ZoneInfo> {
        return safeApiCall(call = {requestShoppingZoneStore(zoneId,page)})
    }

    private suspend fun requestShoppingZoneStore(zoneId: Int, page: Int): Result<ZoneInfo> =
        executeResponse(api.getShoppingZoneStore(zoneId,page))
}


