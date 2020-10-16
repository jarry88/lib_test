package com.ftofs.twant.coupon_store

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ftofs.lib_net.BaseRepository
import com.ftofs.lib_net.DemoApiService
import com.ftofs.lib_net.model.CouponDetailVo
import com.ftofs.lib_net.model.CouponItemVo
import com.ftofs.twant.util.Util
import com.gzp.lib_common.base.BaseViewModel
import com.gzp.lib_common.utils.SLog
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CouponStoreViewModel(application: Application):BaseViewModel(application) {
    val currCouponDetail by lazy { MutableLiveData<CouponDetailVo>() }
    val repository by lazy { BaseRepository() }
    val retrofit = (Retrofit.Builder()).client(OkHttpClient.Builder().build()).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).baseUrl("http://192.168.5.32:8100/").build()
    val testApi=retrofit.create(DemoApiService::class.java)
    val finalApi =if(Util.inDev()) testApi else repository.api
    var currPage =0
    val couponStoreList by lazy { MutableLiveData<List<CouponItemVo>>() }

    /**
     * 獲取券倉詳情
     */
    fun getCouponDetail(couponId:Int){
        launch(stateLiveData,
                {repository.run { simpleGet(finalApi.getCouponDetail(couponId)).apply { SLog.info(couponId.toString()) } }},
                {currCouponDetail.postValue(it)}
        )
    }
    /**
     * 獲取券倉列表
     */
    fun getShopCouponStoreList(storeId: Int) {
        launch(stateLiveData,
                {
                    val params=mapOf("page" to currPage+1,"storeId" to storeId)
                    SLog.info(params.toString())
                    repository.run { simpleGet(finalApi.getCouponList(params)) }},
                {
                    it.list?.let { list ->  couponStoreList.postValue(list)
                        if(list.isNotEmpty()) currPage++
                    }
                }
        )
    }

}
