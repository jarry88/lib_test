package com.ftofs.twant.kotlin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ftofs.twant.entity.SellerGoodsItem
import com.ftofs.twant.log.SLog
import com.ftofs.twant.util.User

class HomeVM : BaseViewModel() {
//    var content: ObservableFiled<String> = ObservableFiled()
    private val _goodsItems = Transformations.switchMap(page) {
        //当refreshTrigger的值被设置时，bannerList
        api.sellerGoodsList(User.getToken(),it)
    }

    val goodsItems =mapPageT(_goodsItems)

    var back=MutableLiveData<Boolean>()
    fun backAction() {
        back.value =true
    }
    fun test() {
        api.isSeller(User.getToken())
    }

}