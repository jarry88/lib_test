package com.gzp.lib_common.constant

/**
 * Created by luyao
 * on 2019/10/12 11:08
 */
sealed class Result<out T : Any> {

    data class Success<out T : Any>(val datas: T?) : Result<T>()
    data class DataError<out T : Any>(val datas: T) : Result<T>()
    data class Msg(val msg: String?) : Result<Nothing>()
    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[datas=$datas]"
            is Error -> "Error[exception=$exception]"
            is DataError<*> ->"DataError[datas=$datas]"
            is Msg -> "msg[msg=$msg]"
        }
    }
}