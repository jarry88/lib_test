package com.ftofs.twant.kotlin

import android.app.Application
import com.ftofs.lib_net.BaseRepository
import com.gzp.lib_common.base.BaseViewModel
import com.gzp.lib_common.utils.SLog

class TestViewModel(application:Application):BaseViewModel(application) {
    val repository by lazy {object : BaseRepository(){} }

    fun getData() {
        launch(stateLiveData,
                {repository.run {
                    simpleGet(api.getShoppingZone(12))}},
                    {SLog.info(it.toString())}
        )
    }

}
