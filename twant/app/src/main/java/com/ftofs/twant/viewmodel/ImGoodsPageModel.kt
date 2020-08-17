package com.ftofs.twant.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.wzq.mvvmsmart.base.BaseViewModelMVVM
import kotlin.random.Random

class ImGoodsPageModel(application: Application) :BaseViewModelMVVM(application) {
     val showSearch by lazy {
        MutableLiveData<Boolean>()
    }

    fun changeSearch() {
        showSearch.value= Random.nextBoolean()
    }
}
