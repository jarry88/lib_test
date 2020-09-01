package com.ftofs.twant.kotlin

import android.app.Application
import com.ftofs.twant.entity.CategoryCommodity
import com.ftofs.twant.kotlin.base.BaseViewModel
import com.ftofs.twant.kotlin.net.BaseRepository
import com.ftofs.twant.kotlin.net.Result
import com.ftofs.twant.log.SLog
import com.wzq.mvvmsmart.event.StateLiveData


class CategoryCommodityViewModel(application: Application):BaseViewModel(application){
    val liveData =StateLiveData<String>()
    fun getCategoryData() {
//        val result =repository.run { simpleGet(api.getGoodsClassNavV2()) }
        launch(stateLiveData
            ,{repository.run { simpleGet(api.getGoodsClassNavV2())}}
            ,success = {d: List<CategoryCommodity> ->
            d.forEach{ SLog.info(it.categoryName)}
        })

    }

}
