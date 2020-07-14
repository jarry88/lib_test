package com.ftofs.twant.kotlin

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ftofs.twant.entity.SellerGoodsItem
import com.ftofs.twant.kotlin.base.BaseViewModel


class LinkageContainerViewModel(application:Application) :BaseViewModel(application){
//    private val viewModel: LinkageContainerViewModel = LinkageContainerViewModel()
    var pageNum = 1
    //    var liveData: MutableLiveData<MutableList<ItemsEntity?>> = MutableLiveData()
    val liveData: MutableLiveData<ArrayList<SellerGoodsItem>> by lazy {
        MutableLiveData<ArrayList<SellerGoodsItem>>()
    }
}
