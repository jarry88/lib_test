package com.ftofs.twant.coupon_store

import android.app.Application
import androidx.lifecycle.MutableLiveData
import cn.snailpad.easyjson.EasyJSONObject
import com.ftofs.lib_net.BaseRepository
import com.ftofs.lib_net.DemoApiService
import com.ftofs.lib_net.model.*
import com.ftofs.twant.constant.SPField
import com.ftofs.twant.util.LogUtil
import com.ftofs.twant.util.User
import com.ftofs.twant.util.Util
import com.gzp.lib_common.base.BaseViewModel
import com.gzp.lib_common.utils.SLog
import com.orhanobut.hawk.Hawk
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CouponStoreViewModel(application: Application):BaseViewModel(application) {
    val currCouponDetail by lazy { MutableLiveData<CouponDetailVo>() }
    val couponStoreList by lazy { MutableLiveData<List<CouponItemVo>>() }
    val buyStep1Vo by lazy { MutableLiveData<BuyStep1Vo>() }
    val buyStep2Vo by lazy { MutableLiveData<BuyStep2Vo>() }
    val mPayVo by lazy { MutableLiveData<MpayVo>() }
    val error by lazy { MutableLiveData<String>() }

    val repository by lazy { BaseRepository() }

    val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("http://192.168.5.32:8100/tc/")
            .client(
                    OkHttpClient.Builder()
                            .addInterceptor() { chain ->
                                chain.request().run {
                                    newBuilder()
                                            .header("Authorization", User.getToken()?:"")
                                            .header("clientType", "android")
                                            .method(method(), body())
                                            .build()
                                            .let { chain.proceed(it) }
                                }
                            }
                            .addInterceptor { chain ->
                                val request: Request = chain.request()
                                val startTime = System.currentTimeMillis()
                                val response: Response = chain.proceed(chain.request())
                                val endTime = System.currentTimeMillis()
                                val duration = endTime - startTime
                                val mediaType = response.body()!!.contentType()
                                val content = response.body()!!.string()
                                SLog.info("\n")
                                SLog.info("----------Start----------------")
                                SLog.info("| ${request.headers()}")
                                SLog.info("| ${request.body()}")
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
                                        SLog.info("| RequestParams:{$sb}")
                                    }
                                }
                                SLog.info("| Response:$content")
                                SLog.info("----------End:" + duration + "毫秒----------")
                                response.newBuilder()
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
                {
                    repository.run {
                        simpleGet(
                                finalApi.getTcBuyStep1(params.apply { SLog.info(this.toString()) }))
                    }
                },
                { buyStep1Vo.postValue(it) }
        )
    }
    /**
     * 購買第二步：下單
     */
    fun getTcBuyStep2(goodsList: List<BuyGoodsDTO>, goodsType: Int = 0, remark: String? = null){ //顯示票券信息  goodsType商品類型：0->商品券，1->自定義表單，2->個人比賽，3->個人比賽
        //clientType 请求的客户端类型：android,ios,wap,wechat,web
        //goodsList  goodsId	商品ID  goodsNum	商品數量
        //goodsType  商品類型：0->商品券，1->自定義表單，2->個人比賽，3->個人比賽
        //remark  備註
        val params = mapOf<String, Any?>("goodsList" to goodsList, "goodsType" to goodsType)
                .run {
                    remark?.let { this.plus("remark" to it) } ?:this
                }
        launch(stateLiveData,
                { repository.run { simpleGet(finalApi.getTcBuyStep2(params.apply { SLog.info(this.toString()) })) } },
                {
                    buyStep2Vo.postValue(it)
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

    fun getActivityList(type: String) {
        launch(stateLiveData,
                {
                    val params = mapOf("page" to currPage + 1)
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

    fun loadMpay() {
        val params =buyStep2Vo.value?.orderId?.let {
            mapOf("orderId" to it)
        }.apply { SLog.info(this.toString()) }
        launch(stateLiveData, {
            repository.run { simpleGet(finalApi.postPayMpay(params)) }
        },
                {
                    it.payId?.let { p ->
                        Util.markPayId(SPField.FIELD_MPAY_PAY_ID, p)
                    }
                    mPayVo.postValue(it)
                },
                error = {
                    LogUtil.uploadAppLog("pay/mpay", params.toString(), it.toString(), "")

                    error.postValue(it.payData.toString())
                },
                others = {
                    LogUtil.uploadAppLog("pay/mpay", params.toString(), "", "")

                }
        )
    }

    fun postMpayNotify() {
        // MPay支付成功，通知服務器
        val userId = User.getUserId()
        val key = String.format(SPField.FIELD_MPAY_PAY_ID, userId)
        SLog.info("key[%s]", key)
        val payData = Hawk.get(key, "")
        SLog.info("payData[%s]", payData)
        val payDataObj = EasyJSONObject.parse<EasyJSONObject>(payData) ?: return
//        val timestampMillis = payDataObj.getLong("timestampMillis")
//        val payId = payDataObj.getInt("payId")
//        val now = System.currentTimeMillis()
//
//        if (now - timestampMillis > 60 * 1000) { // 如果超過1分鐘不通知
//            return
//        }
        val params =buyStep2Vo.value?.orderId?.let {
            mapOf("orderId" to it,"token" to User.getToken())
        }.apply { SLog.info(this.toString()) }
        launch(stateLiveData,{
            repository.run { simpleGet(finalApi.postPayMpayNotify(params)) }
        },{
            SLog.info("支付回调通知成功")
        })
    }

}
