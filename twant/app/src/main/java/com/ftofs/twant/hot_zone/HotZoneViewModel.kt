package com.ftofs.twant.hot_zone

import com.ftofs.lib_net.BaseRepository
import com.ftofs.twant.TwantApplication
import com.gzp.lib_common.base.BaseViewModel

class HotZoneViewModel(application: TwantApplication):BaseViewModel(application) {
    private val repository by lazy { BaseRepository() }
    fun getHotZoneData(hotId:Int){
        launch(stateLiveData,
                {repository.run { simpleGet(api.) }}
        )
    }

}
