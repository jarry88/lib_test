package com.ftofs.twant.hot_zone

import androidx.lifecycle.MutableLiveData
import com.ftofs.lib_net.BaseRepository
import com.ftofs.lib_net.model.HotZoneInfo
import com.ftofs.twant.TwantApplication
import com.gzp.lib_common.base.BaseViewModel

class HotZoneViewModel(application: TwantApplication):BaseViewModel(application) {
    private val repository by lazy { BaseRepository() }
    private val hotZoneInfo by lazy { MutableLiveData<HotZoneInfo>() }
    fun getHotZoneData(hotId:Int){
        launch(stateLiveData,
                {repository.run { simpleGet(api.getHotZoneIndex(hotId)) }},
                {hotZoneInfo.postValue(it)},
        )
    }

}
