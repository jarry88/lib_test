package com.ftofs.twant.go853

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ftofs.lib_net.BaseRepository
import com.ftofs.lib_net.model.GoeftDetailInfo
import com.ftofs.lib_net.model.GoeftInfo
import com.ftofs.lib_net.model.PropertyVo
import com.ftofs.lib_net.net.TwantResponse
import com.ftofs.twant.util.AssetsUtil
import com.ftofs.twant.util.Util
import com.gzp.lib_common.base.BaseViewModel
import kotlinx.coroutines.delay
import com.gzp.lib_common.constant.Result
import com.gzp.lib_common.utils.SLog

class GoHouseViewModel(application: Application):BaseViewModel(application) {
    private val repository by lazy { object :BaseRepository(){} }
    val currPropertyInfo by lazy { MutableLiveData<PropertyVo>() }
    val propertyList by lazy { MutableLiveData<GoeftInfo>() }
    fun getPropertyDetail(pid: Int) {//獲取房產詳情
        launch(stateLiveData, {
            if (Util.inDev()) {
                delay(2000)
                val response=repository.getMockJsonData<TwantResponse<PropertyVo>>(AssetsUtil.loadText(getApplication(),"json/go853.json"))
                Result.Success(response.datas)
            }else repository.run { simpleGet(api.getPropertyInfo(pid)) }
        }, {
            currPropertyInfo.postValue(it) })
    }

    fun getPropertyList() {
        launch(stateLiveData,{
            if (Util.inDev()) {
                delay(2000)
                val response=repository.getMockJsonData<TwantResponse<GoeftInfo>>(AssetsUtil.loadText(getApplication(),"json/go853.json"))
                Result.Success(response.datas)
            }else repository.run { simpleGet(api.getPropertyInfoList(mapOf())) }
        },{
            propertyList.postValue(it).apply { SLog.info("拿到數據${it.propertyList?.size}") }
        })
    }

}
