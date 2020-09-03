package com.ftofs.twant.kotlin

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ftofs.twant.entity.CategoryCommodity
import com.ftofs.twant.kotlin.base.BaseViewModel
import com.ftofs.twant.kotlin.net.BaseRepository
import com.ftofs.twant.kotlin.net.CategoryNavVoInfo
import com.ftofs.twant.kotlin.net.Result
import com.ftofs.twant.log.SLog
import com.ftofs.twant.vo.CategoryNavVo
import com.wzq.mvvmsmart.event.StateLiveData


class CategoryCommodityViewModel(application: Application):BaseViewModel(application){
    val categoryNavVo by lazy { MutableLiveData<List<CategoryNavVo>>() }

    fun getCategoryData() {
//        val result =repository.run { simpleGet(api.getGoodsClassNavV2()) }
        launch(stateLiveData
            ,{repository.run { simpleGet(api.getGoodsClassNavV2())}}
            ,success = {d: CategoryNavVoInfo ->
                d.categoryNavVo.forEach{ SLog.info(it.categoryName) }
                categoryNavVo.postValue(d.categoryNavVo)
        })

    }

}
