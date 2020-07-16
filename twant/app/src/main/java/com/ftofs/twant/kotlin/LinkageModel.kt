package com.ftofs.twant.kotlin

import com.ftofs.twant.constant.ResponseCode
import com.ftofs.twant.entity.SellerGoodsItem
import com.ftofs.twant.kotlin.net.BaseRepository
import com.ftofs.twant.kotlin.net.MRequest
import com.ftofs.twant.kotlin.vo.SellerPageVO
import com.ftofs.twant.kotlin.net.Result


class LinkageModel : BaseRepository(){
    suspend fun getShoppingGoodsList1(zoneId:Int): Result<SellerPageVO<SellerGoodsItem>> {

        return safeApiCall(call ={requestSearch1(zoneId)},errorMessage = "网络错误")
    }
    private suspend fun requestSearch1(zoneId:Int): Result<SellerPageVO<SellerGoodsItem>> =
            executeResponse(MRequest.getInstance().service.getShoppingZone(zoneId))

    fun getShoppingZone(zoneId: Int): Respo<ArrayList<ZoneCategory>> {
        TODO("Not yet implemented")
    }
}
