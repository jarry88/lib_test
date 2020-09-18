package com.ftofs.lib_net

import com.ftofs.lib_net.model.*
import com.ftofs.lib_net.net.TwantResponse
import com.wzq.mvvmsmart.net.base.BaseResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

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
//
//    //  获取网络数据
//    @POST("AppNews/getNewsList/type/1/p/1")
//    fun doPostServerNews(@Body requestBody: RequestBody): Observable<BaseResponse<ArrayList<NewsData>>>

    //  获取商家商品列表
    @GET("member/seller/goods/list")
    fun doSellerGoodsList(@QueryMap map:@JvmSuppressWildcards Map<String ,Any> ): Observable<BaseResponse<SellerPageVO<SellerGoodsItem>>>
    //  获取商家商品列表
    @GET("member/seller/goods/list")
    suspend fun doSellerFeaturesGoodsList(@QueryMap map:@JvmSuppressWildcards Map<String ,Any> ):BaseResponse<SellerPageVO<SellerGoodsItem>>

    //  获取购物专场数据
    @GET("shoppingzone/info/{zoneId}")
    suspend fun getShoppingZone(@Path("zoneId") zoneId: Int ): TwantResponse<SellerPageVO<SellerGoodsItem>>

    //  获取购物专场数据
    @GET("shoppingzone/info/{zoneId}")
    suspend fun doZoneCategoryList(@Path("zoneId") zoneId: Int ): TwantResponse<ZoneInfo>

    //  获取购物专场商品数据
    //  shoppingzone
    @GET("shoppingzone/goods/{categoryId}")
    suspend fun getShoppingZoneGoods(@Path("categoryId")categoryId: String, @Query("page")page: Int): TwantResponse<ZoneInfo>

    //  获取购物专场商店数据
    @GET("shoppingzone/store/{zoneId}")
    suspend fun getShoppingZoneStore(@Path("zoneId")zoneId: Int, @Query("page")page:Int): TwantResponse<ZoneInfo>
    //  想要食核銷接口
    //    token: String?, ordersId: Int, goodsId: Int, count: Int, verificationCode: String

    @POST("member/orders/ifoodmacau/verify")
    suspend fun getIfoodmacauVerify(@Query("token")token:String,
                                    @Query("verificationCode")verificationCode:String?,
                                    @Query("ordersId")ordersId:Int,
                                    @Query("goodsId")goodsId:Int,
                                    @Query("count")count:Int): TwantResponse<ZoneInfo>
//    @FormUrlEncoded  表单用注释
    @POST("member/im/goods/search")
    suspend fun getImGoodsSearch(@QueryMap queryParams: Map<String, String?>): TwantResponse<ImGoodsSearch>
    @GET("member/im/orders/list")
    suspend fun getImOrdersSearch(@QueryMap queryParams: Map<String, String?>): TwantResponse<ImGoodsSearch>
    //新版三级分类商品种类信息
    @GET("app/home/goods_class_nav/v2")
    suspend fun getGoodsClassNavV2(): TwantResponse<CategoryNavVoInfo>
//    @Query("token") token:String,
    @POST("member/voucher/command/receive")
    suspend fun getVoucherCommandReceive(@Query("token")token:String,
                                         @Query("templateId")templateId:String?): TwantResponse<CommonInfo>

//【首頁】誠友列表
    @GET("app/home/random/member/list")
    suspend fun getRandomMemberList(@Query("token")token:String?): TwantResponse<RandomFriendInfo>


    //post接口测试用
    @POST("member/resume/info")
    suspend fun testPost(@Body body: RequestBody): TwantResponse<ZoneInfo>
    @POST("member/resume/info")
    suspend fun testPost1(@Query("token") token: String): TwantResponse<ZoneInfo>

//@Field
}