package com.ftofs.lib_net

import com.ftofs.lib_net.model.*
import com.ftofs.lib_net.net.TwantResponse
import com.ftofs.lib_net.smart.base.BaseResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * created 志鵬 2020.09.30
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
    //post接口测试用
    @POST("member/resume/info")
    suspend fun testPost(@Body body: RequestBody): TwantResponse<ZoneInfo>
    @POST("member/resume/info")
    suspend fun testPost1(@Query("token") token: String): TwantResponse<ZoneInfo>

    @GET("shoppingzone/info/{zoneId}")
    suspend fun doTest(@Path("zoneId") zoneId: Int ): BaseResponse<ZoneInfo>
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
//    suspend fun doZoneCategoryList(@Path("zoneId") zoneId: Int ): TwantResponse<ZoneInfo>

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

//【登錄模塊】
    //賬號密碼登錄
    @POST("v2/login")
    suspend fun getLogin(@Query("mobile")mobile:String,@Query("password")password:String,@Query("clientType")clientType:String):TwantResponse<LoginInfo>
    //發送動態驗證嘛
    @POST("v2/smscode/send")
    suspend fun getSmsCodeSend(@Query("mobile")mobile:String):TwantResponse<AuthCodeInfo>
    @POST("v2/mobile/findpwd")
    suspend fun getLoginFindPwd(@Query("mobile")mobile:String):TwantResponse<AuthCodeInfo>
    //【找回密碼】密碼設置
    @POST("v2/mobile/findpwd/second")
    suspend fun getPwdSet(@QueryMap queryParams: Map<String, String?>):TwantResponse<CommonInfo>

    //驗證碼登錄
    @POST("v2/mobile/login")
    suspend fun getMessage(@QueryMap queryParams: Map<String, String?>): TwantResponse<LoginInfo>
    //阿里雲一鍵登錄
    @POST("v2/mobile/loginOne")
    suspend fun getLoginOne(@Query("aliYunToken")aliYunToken:String,@Query("clientType")clientType:String?,@Query("invite")invite:String?): TwantResponse<LoginInfo>
    @GET("app/mobile/zone")
    suspend fun getMobileZoneList(): TwantResponse<AdminMobileAreaList>
    @POST("loginconnect/facebook/login")
    suspend fun doFaceLogin(@Query("accessToken")accessToken:String,@Query("userId")userId:String,@Query("clientType")clientType:String): TwantResponse<LoginInfo>
    @POST("v2/login")
    suspend fun passwordLogin(@QueryMap queryParams: Map<String, String?>): TwantResponse<LoginInfo>
    @POST("loginconnect/new/weixin/login")
    suspend fun getWXLoginStepOne(@QueryMap queryParams: Map<String, String?>): TwantResponse<LoginInfo>
//【登錄模塊】結束

//【熱區模塊】 熱區首頁
    @GET("hotzone/index")
    suspend fun getHotZoneIndex(@Query("hotId")hotId: Int?): TwantResponse<HotZoneInfo>
//【熱區模塊】 熱區結束

//【GOEFT】房產信息模塊

    @GET("goeft/property/{propertyId}")//房產詳情
    suspend fun getPropertyInfo(@Path("propertyId")propertyId: Int): TwantResponse<GoeftInfo>
    @GET("goeft/property/list")//房產信息列表
    suspend fun getPropertyInfoList(@QueryMap queryParams: @JvmSuppressWildcards Map<String, Any?>):TwantResponse<GoeftInfo>//问题出在参数map的value类型Any.对于java来说,这个value的类型是Object,可以被Retrofit识别,但对于kotlin来说,retrofit会把Any识别成 ?,就报出了错误.
    @GET("goeft/user/{uid}")//房產用戶列表
    suspend fun getGoeftUserUid(@Path("uid") uid:Int,@QueryMap queryParams: @JvmSuppressWildcards Map<String, Any?>): TwantResponse<GoeftInfo>
    //【GOEFT】房產信息模塊 結束

    //【券倉】券倉模塊
    @Headers("clientType: android")
    @GET("coupon/list")
    suspend fun getCouponList(@QueryMap queryParams: @JvmSuppressWildcards Map<String, Any?>): TwantResponse<CouponInfo>
    @Headers("clientType: android")
    @GET("coupon/{id}")
    suspend fun getCouponDetail(@Path ("id")id: Int): TwantResponse<CouponDetailVo>
    //獲取訂單列表-分頁
    @Headers("clientType: android")
    @GET("orders/list")
    suspend fun getCouponOrdersList(@QueryMap queryParams: @JvmSuppressWildcards Map<String, Any?>): TwantResponse<CouponOrdersListInfo>
    @GET("orders/{id}")
    suspend fun getCouponOrderDetail(@Path("id") id:Int): TwantResponse<CouponOrderDetailInfo>
    @PUT("orders/cancel/{id}")//取消订单
    suspend fun putCouponOrderDetail(@Path("id") id:Int): TwantResponse<CouponOrderDetailInfo>
//    @DELETE("orders/{id}") //删除订单
    @HTTP(method = "DELETE",path = "orders/{id}", hasBody = false)
    suspend fun deleteCouponOrderDetail(@Path("id") id:Int): TwantResponse<CommonInfo>
    //
    @POST("buy/step1")
    @Headers("Content-Type:application/json; charset=UTF-8")
    suspend fun getTcBuyStep1( @Body queryParams:@JvmSuppressWildcards Map<String, Any?>): TwantResponse<BuyStep1Vo>
    @POST("exchange")
    @Headers("Content-Type:application/json; charset=UTF-8")
    suspend fun postExchange( @Body queryParams:@JvmSuppressWildcards Map<String, Any?>): TwantResponse<BuyStep1Vo>
    @POST("buy/step2")
    @Headers("Content-Type:application/json; charset=UTF-8")
    suspend fun getTcBuyStep2(@Body queryParams:@JvmSuppressWildcards Map<String, Any?>): TwantResponse<BuyStep2Vo>
    //mpay支付下單
    @POST("pay/mpay")
    @Headers("Content-Type:application/json; charset=UTF-8")
    suspend fun postPayMpay(@Body queryParams:@JvmSuppressWildcards Map<String, Any?>?): TwantResponse<MpayVo>
    //兌換
    @POST("exchange")
    @Headers("Content-Type:application/json; charset=UTF-8")
    suspend fun postExchange(@Body body:String?): TwantResponse<MpayVo>
    //mpay支付下單  app不用
    @POST("pay/mpayNotify")
    @Headers("Content-Type:application/json; charset=UTF-8")
    suspend fun postPayMpayNotify(params: Map<String, Any>?): TwantResponse<MpayVo>
//【GOEFT】券倉模塊結束



//@Field
}