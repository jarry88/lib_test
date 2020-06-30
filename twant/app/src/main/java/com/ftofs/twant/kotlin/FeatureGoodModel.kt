package com.ftofs.twant.kotlin

import android.app.Application
import androidx.databinding.ObservableField
import com.ftofs.twant.entity.SellerGoodsItem
import com.wzq.mvvmsmart.base.BaseViewModelMVVM

class FeatureGoodModel(application: Application) : BaseViewModelMVVM(application) {
    var entity: ObservableField<SellerGoodsItem?>? = ObservableField()
    fun setDemoEntity(entity: SellerGoodsItem?) {
        this.entity!!.set(entity)
    }

    override fun onDestroy() {
        super.onDestroy()
        entity = null
    }
}