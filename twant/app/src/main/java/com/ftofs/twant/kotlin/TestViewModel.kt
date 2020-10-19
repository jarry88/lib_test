package com.ftofs.twant.kotlin

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ftofs.lib_net.BaseRepository
import com.ftofs.lib_net.DemoApiService
import com.ftofs.lib_net.model.PropertyVo
import com.ftofs.twant.config.Config
import com.ftofs.twant.kotlin.adapter.LiveDataCallAdapterFactory
import com.ftofs.twant.util.Util
import com.gzp.lib_common.base.BaseViewModel
import com.gzp.lib_common.constant.Result
import com.gzp.lib_common.utils.SLog
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class TestViewModel(application: Application):BaseViewModel(application) {
    val repository by lazy {object : BaseRepository(){} }

    val retrofit = (Retrofit.Builder()).client(OkHttpClient.Builder().build()).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).baseUrl("http://192.168.5.19:8080/api/").build()
    val testApi=retrofit.create(DemoApiService::class.java)
    val finalApi =if(Util.inDev()) testApi else repository.api

    var currPage =0
    var hasMore =true
    val toastError by lazy { MutableLiveData<String>() }

    val list by lazy {  MutableLiveData<List<PropertyVo>> ()}
    fun retrofit(): Retrofit = Retrofit.Builder().
    client(OkHttpClient().newBuilder().addInterceptor{ chain->
        val newUrl = chain.request().url()
                .newBuilder()
//                        .addQueryParameter("api_key", AppConstants.tmdbApiKey)
                .build()

        val newRequest = chain.request()
                .newBuilder()
                .url(newUrl)
                .build()

        chain.proceed(newRequest)

    }.run { build() })
            .baseUrl(Config.API_BASE_URL+"/")
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
    .addConverterFactory(GsonConverterFactory.create()).build()
    val tApi =retrofit.create(DemoApiService::class.java)
    fun getData() {
        launch(stateLiveData,
                {
                    repository.run {
                        simpleGet(
                                tApi.doZoneCategoryList(12)
                        )
                    }
                },
                { SLog.info(it.toString()) }
        )
    }
    fun getPropertyList(
            page:Int=currPage+1,
            size:Int=20,
            search:String?=null,

    ) {
        launch(stateLiveData, {
            val params = mapOf<String,Any?>("page" to page, "size" to size)
                    .run {
                        if(search.isNullOrEmpty()) this
                        else this.plus("search" to search)
                    }
            repository.run {
                simpleGet(finalApi.getPropertyInfoList(params.apply { SLog.info(this.toString()) }))
            }
        }, {
            hasMore = it.pageEntity.hasMore
            list.postValue(it.propertyList).apply {
                SLog.info("拿到數據${it.propertyList?.size}")
                it.propertyList?.let {l->
                    if (l.isNotEmpty()) {
                        currPage=page
                    }
                }
            }
        },error={
            it.error?.let {s->
                if(s.isNotEmpty()){
                    toastError.postValue(s)
                }
            }
        })
    }
//    fun getTest() {
//        launch(stateLiveData,
//                {
//                    Result.Success(tApi.doZoneCategoryList(12).datas)
////                    repository.run {
////                        simpleGet(
////                                tApi.doZoneCategoryList(12)
////                        )
////                    }
//                },
//                { SLog.info(it.toString()) }
//        )
//    }

}
