package com.ftofs.twant.kotlin.base

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wzq.mvvmsmart.base.BaseViewModelMVVM
import com.wzq.mvvmsmart.event.StateLiveData
import kotlinx.coroutines.launch

/**
 *
 * 作者：志鵬
 *
 *
 *
 * 创建时间：2020、8、28
 *
 *
 *
 * 文件描述：
 *
 *
 */
open class BaseViewModel(application: Application) :BaseViewModelMVVM(application){
    fun<T> launch(block: suspend () -> Unit, error: suspend (Throwable) -> Unit, liveData: StateLiveData<T>, isShowLoading:Boolean = true) = viewModelScope.launch {
        try {
            if(isShowLoading){
                liveData.postLoading()
            }
            block()
        } catch (e: Throwable) {
            liveData.postError()
            error(e)
        }finally {
            liveData.postSuccess()
        }
    }
}