package com.ftofs.twant.kotlin.ui

import android.app.Application
import com.gzp.lib_common.net.BaseRepository
import com.gzp.lib_common.utils.SLog
import com.wzq.mvvmsmart.base.BaseViewModelMVVM
import com.wzq.mvvmsmart.event.StateLiveData

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
    val repository by lazy {object: BaseRepository(){}}
    var errorMessage :String?=null

    fun<T,D:Any> launch(liveData: StateLiveData<T>,
                        result: suspend () -> Result<D>,
                        success:(d:D)->Unit,
                        error:(d:D)->Unit={},
                        others:()->Unit ={liveData.postNoNet()},
                        catchError: suspend (Throwable) -> Unit={ e:Throwable-> SLog.info("catch exception : $e")},
                        isShowLoading:Boolean = true) = viewModelScope.launch {
        try {
            if(isShowLoading){
                liveData.postLoading()
            }
            when (val re=result()) {
                is Result.Success ->success(re.datas)
                is Result.DataError->{
                    errorMessage="数据加载失败"
                    error(re.datas)
//                    errorMessage=re
                    stateLiveData.postError()

                }
                else ->others()
            }
        } catch (e: Throwable) {
            liveData.postError()
            catchError(e)
        }finally {
            liveData.postSuccess()
        }
    }
}