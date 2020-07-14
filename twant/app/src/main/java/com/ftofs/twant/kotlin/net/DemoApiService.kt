package com.ftofs.twant.kotlin.net

import com.ftofs.twant.entity.SellerGoodsItem
import com.ftofs.twant.kotlin.bean.NewsData
import com.ftofs.twant.kotlin.bean.TwantResponse
import com.ftofs.twant.kotlin.vo.SellerPageVO
import com.wzq.mvvmsmart.net.base.BaseResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*
import java.time.ZoneId
import kotlin.collections.ArrayList

/**
 * created 王志强 2020.04.30
 */
interface DemoApiService {
//    @GET("action/apiv2/banner")
//    fun demoGet(@Query("catalog") pageNum: Int): Observable<BaseResponse<SellerGoodsItem>>
//
//    //  获取网络数据
//    @GET("AppNews/getNewsList/type/1/p/{pageNum}")
//    fun doGetServerNews(@Path("pageNum") newsId: Int): Observable<BaseResponse<ArrayList<SellerGoodsItem>>>

    //  获取网络数据
    @POST("AppNews/getNewsList/type/1/p/1")
    fun doPostServerNews(@Body requestBody: RequestBody): Observable<BaseResponse<ArrayList<NewsData>>>

    //  获取商家商品列表
    @GET("member/seller/goods/list")
    fun doSellerGoodsList(@QueryMap map:@JvmSuppressWildcards Map<String ,Any> ): Observable<BaseResponse<SellerPageVO<SellerGoodsItem>>>

    //  获取购物专场数据
    @GET("shoppingzone/{zoneId}")
    suspend fun getShoppingZone(@Path("zoneId") zoneId: Int ): TwantResponse<SellerPageVO<SellerGoodsItem>>

//@Field
}