package com.ftofs.lib_net

import com.alibaba.fastjson.JSON
import com.gzp.lib_common.constant.Result
import com.ftofs.lib_net.net.TwantResponse
import com.google.gson.JsonParser
import com.gzp.lib_common.utils.PathUtil
import com.gzp.lib_common.utils.SLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import retrofit2.converter.gson.GsonConverterFactory
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
            SLog.info(response.toString())
            if (response.code == -1) {
                errorBlock?.let { it() }
                Result.Error(IOException(response.message))
            }else if((response.code==400 )or(response.code==401) ){
                Result.DataError(response.datas)
            } else {
                successBlock?.let { it() }
                Result.Success(response.datas)
            }
        }
    }
    open suspend fun <T : Any> simpleGet(response: TwantResponse<T>, successBlock: (suspend CoroutineScope.() -> Unit)? = null,
                                         errorBlock: (suspend CoroutineScope.() -> Unit)? = null): Result<T> {
        SLog.info("response:"+response.toString())
        return safeApiCall(call = {executeResponse(response)})
    }

}