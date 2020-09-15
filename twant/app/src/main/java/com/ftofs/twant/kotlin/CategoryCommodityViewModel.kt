package com.ftofs.twant.kotlin

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ftofs.lib_net.BaseRepository
import com.gzp.lib_common.base.BaseViewModel
import com.ftofs.lib_net.model.CategoryNavVoInfo
import com.gzp.lib_common.utils.SLog
import com.ftofs.lib_net.model.CategoryNavVo


class CategoryCommodityViewModel(application: Application): BaseViewModel(application){
    val repository by lazy {object : BaseRepository(){} }

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
