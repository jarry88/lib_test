package com.ftofs.twant.kotlin.net

import com.ftofs.twant.entity.SellerGoodsItem
import com.ftofs.twant.kotlin.bean.TwantResponse
import com.ftofs.twant.kotlin.vo.SellerPageVO
import com.wzq.mvvmsmart.net.net_utils.MetaDataUtil
import retrofit2.http.GET
import retrofit2.http.Path

interface TwantService {
    companion object {
        fun getBaseUrl(): String {
           return MetaDataUtil.getBaseUrl()
        }

        const val BASE_URL = "https://www.wanandroid.com"
    }
    //  获取购物专场数据
    @GET("shoppingzone/{zoneId}")
    suspend fun getShoppingZone(@Path("zoneId") zoneId: Int ): TwantResponse<SellerPageVO<SellerGoodsItem>>
}