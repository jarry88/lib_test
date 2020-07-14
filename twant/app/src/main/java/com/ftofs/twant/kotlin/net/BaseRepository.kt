package com.ftofs.twant.kotlin.net

import com.ftofs.twant.kotlin.bean.TwantResponse
import com.ftofs.twant.log.SLog
import com.wzq.mvvmsmart.net.base.BaseRequest
import com.wzq.mvvmsmart.net.base.BaseResponse
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

    suspend fun <T : Any> safeApiCall(call: suspend () -> Result<T>, errorMessage: String): Result<T> {
        return try {
            SLog.info("here")
            call()
        } catch (e: Exception) {
            // An exception was thrown when calling the API so we're converting this to an IOException
            Result.Error(IOException(errorMessage, e))
        }
    }

    suspend fun <T : Any> executeResponse(response: TwantResponse<T>, successBlock: (suspend CoroutineScope.() -> Unit)? = null,
                                          errorBlock: (suspend CoroutineScope.() -> Unit)? = null): Result<T> {
        SLog.info("response.toString()")

        return coroutineScope {
            SLog.info(response.toString())
            if (response.code == -1) {
                errorBlock?.let { it() }
                Result.Error(IOException(response.message))
            } else {
                successBlock?.let { it() }
                Result.Success(response.datas)
            }
        }
    }


}