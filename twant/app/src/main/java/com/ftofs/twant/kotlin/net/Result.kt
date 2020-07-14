package com.ftofs.twant.kotlin.net

/**
 * Created by luyao
 * on 2019/10/12 11:08
 */
sealed class Result<out T : Any> {

    data class Success<out T : Any>(val datas: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[datas=$datas]"
            is Error -> "Error[exception=$exception]"
        }
    }
}