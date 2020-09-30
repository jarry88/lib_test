package com.gzp.lib_common.base

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.gzp.lib_common.smart.base.BaseViewModelMVVM
import com.wzq.mvvmsmart.event.StateLiveData
import com.gzp.lib_common.smart.utils.KLog
import kotlinx.coroutines.launch
import  com.gzp.lib_common.constant.Result
import com.gzp.lib_common.utils.SLog

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
open class BaseViewModel(application: Application) : BaseViewModelMVVM(application){
    var errorMessage :String?=null

    fun<T,D:Any> launch(liveData: StateLiveData<T>,
                        result: suspend () -> Result<D>,
                        success:(d:D)->Unit,
                        error:(d:D)->Unit={},
                        others:()->Unit ={liveData.postNoNet()},
                        catchError: suspend (Throwable) -> Unit={ e:Throwable-> KLog.e("catch exception : $e")},
                        isShowLoading:Boolean = true,
                        final:()->Unit={liveData.postSuccess()}

    ) = viewModelScope.launch {
        try {
            if(isShowLoading){
                liveData.postLoading()
            }
            when (val re=result()) {
                is Result.Success ->{//200
                    SLog.info("數據獲取成功 ${re.datas.toString()}")
                    success(re.datas)}
                is Result.DataError->{//400參數錯誤。401登陸錯誤
                    errorMessage="数据加载失败"
                    SLog.info(re.datas.toString())
                    error(re.datas)
//                    errorMessage=re
                    stateLiveData.postError()

                }
                else ->{
                    SLog.info("拉取專場數據 異常 ，405")
                    others()}
            }
        } catch (e: Throwable) {//檢查404
            SLog.info(e.toString())
            liveData.postError()
            catchError(e)
        }finally {
            final()
        }
    }
}