package com.ftofs.twant.coupon_store

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ftofs.lib_net.BaseRepository
import com.ftofs.lib_net.DemoApiService
import com.ftofs.lib_net.model.BuyGoodsDTO
import com.ftofs.lib_net.model.BuyStep1Vo
import com.ftofs.lib_net.model.CouponDetailVo
import com.ftofs.lib_net.model.CouponItemVo
import com.ftofs.twant.util.AssetsUtil
import com.ftofs.twant.util.User
import com.ftofs.twant.util.Util
import com.gzp.lib_common.base.BaseViewModel
import com.gzp.lib_common.utils.SLog
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CouponStoreViewModel(application: Application):BaseViewModel(application) {
    val currCouponDetail by lazy { MutableLiveData<CouponDetailVo>() }
    val couponStoreList by lazy { MutableLiveData<List<CouponItemVo>>() }
    val buyStep1Vo by lazy { MutableLiveData<BuyStep1Vo>() }

    val repository by lazy { BaseRepository() }
//    { chain ->
//        val original: okhttp3.Request = chain.request()
//        val request: okhttp3.Request = original.newBuilder()
//                .header("clientType", "android")
//                .header("Authorization", com.ftofs.twant.util.User.getToken())
//                .header("Content-Type", "application/json")
//                .method(original.method().apply {
//                    com.gzp.lib_common.utils.SLog.info(this)
//                }, original.body())
//                .build()
//        chain.proceed(request)
//    }
    val logInterceptor = HttpLoggingInterceptor { message -> SLog.info(message) }.apply { level=HttpLoggingInterceptor.Level.BODY };//创建拦截对象

    val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("http://192.168.5.32:8100/")
            .client(OkHttpClient.Builder().addInterceptor { chain ->
                val request: Request = chain.request()
                val startTime = System.currentTimeMillis()
                val response: Response = chain.proceed(chain.request())
                val endTime = System.currentTimeMillis()
                val duration = endTime - startTime
                val mediaType = response.body()!!.contentType()
                val content = response.body()!!.string()
                SLog.info( "\n")
                SLog.info("----------Start----------------")
                SLog.info( "| ${request.headers()}")
                SLog.info( "| $request")
                val method = request.method()
                if ("POST" == method) {
                    val sb = StringBuilder()
                    if (request.body() is FormBody) {
                        val body: FormBody? = request.body() as FormBody?
                        body?.let {
                            for (i in 0 until body.size()) {
                                sb.append(body.encodedName(i).toString() + "=" + body.encodedValue(i) + ",")
                            }
                        }
                        sb.delete(sb.length - 1, sb.length)
                        SLog.info( "| RequestParams:{$sb}")
                    }
                }
                SLog.info("| Response:$content")
                SLog.info("----------End:" + duration + "毫秒----------")
               response.newBuilder()
                       .addHeader("Authorzation", User.getToken())
                        .body(ResponseBody.create(mediaType, content))
                        .build()
            }.build())
            .build()
    val testApi=retrofit.create(DemoApiService::class.java)
    val finalApi =if(Util.inDev()) testApi else repository.api
    var currPage =0

    /**
     * 购买第一步  顯示票券信息
     */
    fun getTcBuyStep1(goodsList: List<BuyGoodsDTO>, goodsType: Int = 0, remark: String? = null){ //顯示票券信息
        //clientType 请求的客户端类型：android,ios,wap,wechat,web
        //goodsList  goodsId	商品ID  goodsNum	商品數量
        //goodsType  商品類型：0->商品券，1->自定義表單，2->個人比賽，3->個人比賽
        //remark  備註
        val params = mapOf<String, Any?>("goodsList" to goodsList, "goodsType" to goodsType)
                .run {
                    remark?.let { this.plus("remark" to it) } ?:this
                }
        launch(stateLiveData,
                { repository.run { simpleGet(
                        finalApi.getTcBuyStep1("application/json", params.apply { SLog.info(this.toString()) })) } },
                { buyStep1Vo.postValue(it) }
        )
    }
    /**
     * 購買第二步：下單
     */
    fun getTcBuyStep2(goodsList: List<BuyGoodsDTO>, goodsType: Int = 0, remark: String? = null){ //顯示票券信息
        //clientType 请求的客户端类型：android,ios,wap,wechat,web
        //goodsList  goodsId	商品ID  goodsNum	商品數量
        //goodsType  商品類型：0->商品券，1->自定義表單，2->個人比賽，3->個人比賽
        //remark  備註
        val params = mapOf<String, Any?>("goodsList" to goodsList, "goodsType" to goodsType)
                .run {
                    remark?.let { this.plus("remark" to it) } ?:this
                }
        launch(stateLiveData,
                { repository.run { simpleGet(finalApi.getTcBuyStep2("application/json", params.apply { SLog.info(this.toString()) })) } },
                {
                    buyStep1Vo.postValue(it)

                }
        )
    }
    /**
     * 獲取券倉詳情
     */
    fun getCouponDetail(couponId: Int){
        launch(stateLiveData,
                { repository.run { simpleGet(finalApi.getCouponDetail(couponId)).apply { SLog.info(couponId.toString()) } } },
                { currCouponDetail.postValue(it) }
        )
    }
    /**
     * 獲取券倉列表
     */
    fun getShopCouponStoreList(storeId: Int) {
        launch(stateLiveData,
                {
                    val params = mapOf("page" to currPage + 1, "storeId" to storeId)
                    SLog.info(params.toString())
                    repository.run { simpleGet(finalApi.getCouponList(params)) }
                },
                {
                    it.list?.let { list ->
                        couponStoreList.postValue(list)
                        if (list.isNotEmpty()) currPage++
                    }
                }
        )
    }

}
