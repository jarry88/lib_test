package com.ftofs.twant.kotlin

import androidx.lifecycle.LiveData
import com.ftofs.twant.config.Config
import com.ftofs.twant.entity.SellerGoodsItem
import com.ftofs.twant.kotlin.adapter.LiveDataCallAdapterFactory
import com.ftofs.twant.kotlin.vo.BannerVO
import com.ftofs.twant.kotlin.vo.PageVO
import com.ftofs.twant.kotlin.vo.SellerGoodsVO
import com.ftofs.twant.kotlin.vo.SellerPageVO
import com.ftofs.twant.log.SLog
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*
import java.util.concurrent.TimeUnit

interface KotlinInterfaceApi {
    companion object {
        fun get(): KotlinInterfaceApi {
            val clientBuilder = OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
//            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
                    SLog.info("下一页网络日志", "Message:$message")
                })
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                clientBuilder.addInterceptor(loggingInterceptor)
////            }
            return Retrofit.Builder()
                    .baseUrl(Config.API_BASE_URL+"/")
//                    .baseUrl("https://www.wanandroid.com/")

                    .client(clientBuilder.build())
                    .addCallAdapterFactory(LiveDataCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(KotlinInterfaceApi::class.java)
        }
    }
    /**
     * 商家商家列表选择页
     */
    @GET("member/seller/goods/list")
    fun sellerGoodsList(
            @Query("token")token:String,
            @Query("page")page:Int
    ): LiveData<ApiResponse<SellerPageVO<SellerGoodsVO>>>
    /**
     * 商家商家列表选择页
     */
    @GET("member/seller/isSeller")
    fun isSeller(
            @Query("token")token:String
    ): LiveData<ApiResponse<Objects>>

    /**
     * 取消镇店之宝
     */
    @POST("member/seller/goods/cancel_features.json")
    fun cancelFeature(
            @Query("token")token: String,
            @Query("commonId")commonId:Int
    ):LiveData<ApiResponse<Objects>> /**
     * 取消镇店之宝
     */
    @POST("member/seller/goods/cancel_features.json")
    fun cancelFeatureCall(
            @Query("token")token: String,
            @Query("commonId")commonId:Array<Int>
    ): Call<ApiResponse<Objects>>
    /**
     * 首页banner
     */
    @GET("app/home/index")
    fun bannerList(): LiveData<ApiResponse<BannerVO>>
}