package com.ftofs.twant.go853

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ftofs.lib_net.BaseRepository
import com.ftofs.lib_net.DemoApiService
import com.ftofs.lib_net.model.GoeftInfo
import com.ftofs.lib_net.model.PropertyVo
import com.ftofs.lib_net.net.TwantResponse
import com.ftofs.twant.util.AssetsUtil
import com.ftofs.twant.util.Util
import com.gzp.lib_common.base.BaseViewModel
import com.gzp.lib_common.constant.Result
import com.gzp.lib_common.utils.SLog
import com.wzq.mvvmsmart.net.net_utils.OkHttpUtil
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class GoHouseViewModel(application: Application):BaseViewModel(application) {
    private val repository by lazy { object :BaseRepository(){} }
    val propertyList by lazy { MutableLiveData<GoeftInfo>() }
    val retrofit = (Retrofit.Builder()).client(OkHttpClient.Builder().build()).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).baseUrl("http://192.168.241.39:8080/api/").build()
    val testApi=retrofit.create(DemoApiService::class.java)
    val currPropertyInfo by lazy { MutableLiveData<PropertyVo>() }
    fun getPropertyDetail(pid: Int) {//獲取房產詳情
        launch(stateLiveData, {
            repository.run { simpleGet(api.getPropertyInfo(pid)) }
        }, {
            currPropertyInfo.postValue(it)
        })
    }
    fun getPropertyList() {
        launch(stateLiveData, {
            if (Util.inDev()) {
//                delay(2000)
//                val response = repository.getMockJsonData<TwantResponse<GoeftInfo>>(AssetsUtil.loadText(getApplication(), "json/go853.json"))
//                Result.Success(response.datas)
                repository.run { simpleGet(testApi.getPropertyInfoList(mapOf("page" to 1 ,"size" to 20))) }

            } else repository.run { simpleGet(api.getPropertyInfoList(mapOf())) }
        }, {
            propertyList.postValue(it).apply { SLog.info("拿到數據${it.propertyList?.size}") }
        })
    }

}
