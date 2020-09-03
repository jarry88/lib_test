package com.ftofs.twant.kotlin.net

import com.ftofs.twant.kotlin.bean.TwantResponse
import com.ftofs.twant.log.SLog
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
open class BaseRepository {

//    suspend fun <T : Any> apiCall(call: suspend () -> WanResponse<T>): WanResponse<T> {
//        return call.invoke()
//    }
    val api by lazy { MRequest.getInstance().service }
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
            }else if(response.code==400 or 401){
                Result.DataError(response.datas)
            } else {
                successBlock?.let { it() }
                Result.Success(response.datas)
            }
        }
    }
    open suspend fun <T : Any> simpleGet(response: TwantResponse<T>, successBlock: (suspend CoroutineScope.() -> Unit)? = null,
                                          errorBlock: (suspend CoroutineScope.() -> Unit)? = null): Result<T> {
        return safeApiCall(call = {executeResponse(response)})
    }

}