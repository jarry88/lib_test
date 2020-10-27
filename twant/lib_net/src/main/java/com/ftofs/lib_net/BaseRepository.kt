package com.ftofs.lib_net

import com.alibaba.fastjson.JSON
import com.ftofs.lib_net.net.TwantResponse
import com.gzp.lib_common.constant.Result
import com.gzp.lib_common.utils.SLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import java.io.IOException

/**
 * Created by luyao
 * on 2019/4/10 9:41
 *
 * Edited by gzp
 * on 2020/7/14
 */
open class BaseRepository() {

//    suspend fun <T : Any> apiCall(call: suspend () -> WanResponse<T>): WanResponse<T> {
//        return call.invoke()
//    }
    inline fun <reified T:Any> getMockJsonData(jsonStr:String):T{
    SLog.info(jsonStr)
//    GsonConverterFactory.create().
        return  JSON.parseObject(jsonStr,T::class.java)
    }
    val api by lazy { MRequest.getInstance().service }
    fun getBase()=MRequest.getInstance().retrofit.baseUrl()
    suspend fun <T : Any> safeApiCall(call: suspend () -> Result<T>, errorMessage: String="网络错误"): Result<T> {
        return try {
            call()
        } catch (e: Exception) {
            // An exception was thrown when calling the API so we're converting this to an IOException
            Result.Error(IOException(errorMessage, e))
        }
    }

    suspend fun <T : Any> executeResponse(response: TwantResponse<T>, successBlock: (suspend CoroutineScope.() -> Unit)? = null,
                                          errorBlock: (suspend CoroutineScope.() -> Unit)? = null): Result<T> {
        return coroutineScope {
            when {
                response.code == -1 -> {
                    errorBlock?.let { it() }.apply { SLog.info("io 異常") }
                    Result.Error(IOException(response.message?:response.msg))
                }
                (response.code==400 )or(response.code==401) -> {
                    Result.DataError(response.run { datas?:data!! })
                }
                response.code==200 -> {
                    successBlock?.let { it() }
                    Result.Success(response.run { datas?:data!! })
                }
                else -> {
                    Result.Msg(response.msg).apply { SLog.info("網絡庫其他錯誤消息") }
                }
            }
        }
    }
    open suspend fun <T : Any> simpleGet(response: TwantResponse<T>, successBlock: (suspend CoroutineScope.() -> Unit)? = null,
                                         errorBlock: (suspend CoroutineScope.() -> Unit)? = null): Result<T> {
        return safeApiCall(call = {executeResponse(response)})
    }

}