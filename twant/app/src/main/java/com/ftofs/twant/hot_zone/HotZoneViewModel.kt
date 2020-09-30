package com.ftofs.twant.hot_zone

import android.app.Application
import android.util.JsonReader
import androidx.lifecycle.MutableLiveData
import com.alibaba.fastjson.JSON
import com.ftofs.lib_net.BaseRepository
import com.ftofs.lib_net.DemoApiService
import com.ftofs.lib_net.model.HotZoneInfo
import com.ftofs.lib_net.model.HotZoneVo
import com.ftofs.lib_net.net.TwantResponse
import com.ftofs.twant.TwantApplication
import com.ftofs.twant.config.Config
import com.ftofs.twant.constant.Constant
import com.ftofs.twant.kotlin.adapter.LiveDataCallAdapterFactory
import com.ftofs.twant.util.AssetsUtil
import com.gzp.lib_common.base.BaseViewModel
import com.gzp.lib_common.utils.BaseContext
import com.gzp.lib_common.utils.SLog
import com.ftofs.lib_net.smart.net_utils.RetrofitUtil
import okhttp3.OkHttpClient
import org.koin.dsl.koinApplication
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HotZoneViewModel(application: Application = BaseContext.instance.getContext() as Application):BaseViewModel(application) {
    private val repository by lazy { BaseRepository() }
    val hotZoneInfo by lazy { MutableLiveData<HotZoneInfo>() }
    val testApi by lazy { retrofit().create(DemoApiService::class.java) }

    fun retrofit(): Retrofit = Retrofit.Builder().
    client(OkHttpClient().newBuilder().addInterceptor{ chain->
        val newUrl = chain.request().url
                .newBuilder()
//                        .addQueryParameter("api_key", AppConstants.tmdbApiKey)
                .build()

        val newRequest = chain.request()
                .newBuilder()
                .url(newUrl)
                .build()

        chain.proceed(newRequest)

    }.run { build() }).
    baseUrl(Config.getBaseApi()).
    addCallAdapterFactory(LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create()).build()
    inline fun <reified T:Any> getJsonData(jsonStr:String):T{
        SLog.info(jsonStr)
//    GsonConverterFactory.create().
        return  JSON.parseObject(jsonStr,T::class.java)
    }
    fun getTestData():TwantResponse<HotZoneInfo>{
        GsonConverterFactory.create()
        val json= JSON.parseObject(AssetsUtil.loadText(getApplication(),"json/hotzone.json"))
        val a =json.getObject("datas",HotZoneInfo::class.java)
        val hotZoneInfo=HotZoneInfo(
                hotName = a.hotName.toString(),
                hotZoneVoList= a.hotZoneVoList,
                hotId = a.hotId,

        )

        return TwantResponse(hotZoneInfo)

    }

    private fun <T> TwantResponse(hotZoneInfo: T): TwantResponse<T> {
        return TwantResponse(200,"","",hotZoneInfo)
    }

    fun getHotTestZoneData(hotId:Int){
        launch(stateLiveData,
                { SLog.info("base Url ${repository.getBase()}拉取测试热区數據 $hotId")
                    repository.run {
                        SLog.info("test1")
//                        val a=getMockJsonData<TwantResponse<HotZoneInfo>>(AssetsUtil.loadText(getApplication(),"json/hotzone.json"))
//                        SLog.info(a.code.toString())
//                        testApi.getHotZoneIndex(hotId))
                        simpleGet(getTestData()) }
//                    repository.run { simpleGet(api.passwordLogin(mapOf("mobile" to "61234567","password" to "password", Constant.CLIENT_TYPE_PAIR)))}
//                    repository.run { simpleGet(api.getHotZoneIndex(hotId)) }
                    },

                {
                    SLog.info("拉取專場數據 成功${it.toString()}")
                    hotZoneInfo.value=it
                },
        )
    }
    fun getHotZoneData(hotId:Int) {
        launch(
                stateLiveData,
                {
                    SLog.info("base Url ${repository.getBase()}拉取热区數據 $hotId")
                    repository.run {
                        SLog.info("pro")
                        simpleGet(api.getHotZoneIndex(hotId)) }
                },

                {
                    SLog.info("拉取專場數據 成功${it.toString()}")
//                    hotZoneInfo.postValue(it)
                },
        )
    }

}
