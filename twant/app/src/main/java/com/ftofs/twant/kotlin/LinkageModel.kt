package com.ftofs.twant.kotlin

import com.ftofs.twant.entity.SellerGoodsItem
import com.ftofs.twant.kotlin.bean.TwantResponse
import com.ftofs.twant.kotlin.net.BaseRepository
import com.ftofs.twant.kotlin.net.MRequest
import com.ftofs.twant.kotlin.vo.SellerPageVO
import com.ftofs.twant.kotlin.net.Result
import com.ftofs.twant.kotlin.net.TwantRetrofitClient
import com.ftofs.twant.log.SLog


class LinkageModel : BaseRepository(){
    suspend fun getShoppingGoodsList(zoneId:Int): Result<SellerPageVO<SellerGoodsItem>> {

        return safeApiCall(call ={requestSearch(zoneId)},errorMessage = "网络错误")
    }
    suspend fun getShoppingGoodsList1(zoneId:Int): Result<SellerPageVO<SellerGoodsItem>> {

        return safeApiCall(call ={requestSearch1(zoneId)},errorMessage = "网络错误")
    }
    private suspend fun requestSearch(zoneId:Int): Result<SellerPageVO<SellerGoodsItem>> =
            executeResponse(TwantRetrofitClient.service.getShoppingZone(zoneId))
    private suspend fun requestSearch1(zoneId:Int): Result<SellerPageVO<SellerGoodsItem>> =
            executeResponse(MRequest.getInstance().service.getShoppingZone(zoneId))
}
