package com.ftofs.twant.kotlin

import androidx.lifecycle.LiveData
import com.ftofs.twant.BuildConfig
import com.ftofs.twant.config.Config
import com.ftofs.twant.entity.SellerGoodsItem
import com.ftofs.twant.kotlin.adapter.LiveDataCallAdapterFactory
import com.ftofs.twant.kotlin.vo.PageVO
import com.ftofs.twant.log.SLog
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
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
//            }
            return Retrofit.Builder()
                    .baseUrl(Config.API_BASE_URL+"/")
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
    ): LiveData<ApiResponse<PageVO<SellerGoodsItem>>>
    /**
     * 商家商家列表选择页
     */
    @GET("member/seller/isSeller?token={token}")
    fun isSeller(
            @Path("token")token:String
    ): LiveData<ApiResponse<PageVO<String>>>

}