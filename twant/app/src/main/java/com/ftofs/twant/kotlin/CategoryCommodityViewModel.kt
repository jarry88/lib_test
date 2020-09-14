package com.ftofs.twant.kotlin

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.gzp.lib_common.base.BaseViewModel
import com.ftofs.twant.kotlin.net.CategoryNavVoInfo
import com.gzp.lib_common.utils.SLog
import com.ftofs.twant.vo.CategoryNavVo


class CategoryCommodityViewModel(application: Application): BaseViewModel(application){
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
