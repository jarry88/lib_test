package com.ftofs.twant.kotlin

import android.app.Application
import com.ftofs.lib_net.BaseRepository
import com.ftofs.lib_net.DemoApiService
import com.ftofs.twant.config.Config
import com.ftofs.twant.kotlin.adapter.LiveDataCallAdapterFactory
import com.gzp.lib_common.base.BaseViewModel
import com.gzp.lib_common.constant.Result
import com.gzp.lib_common.utils.SLog
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class TestViewModel(application: Application):BaseViewModel(application) {
    val repository by lazy {object : BaseRepository(){} }
    val retrofit = retrofit()
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

    fun getTest() {
        launch(stateLiveData,
                {
                    Result.Success(tApi.doZoneCategoryList(12).datas)
//                    repository.run {
//                        simpleGet(
//                                tApi.doZoneCategoryList(12)
//                        )
//                    }
                },
                { SLog.info(it.toString()) }
        )
    }

}
