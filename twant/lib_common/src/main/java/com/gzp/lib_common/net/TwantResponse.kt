package com.gzp.lib_common.net

data class TwantResponse<out T>(val code: Int, val errorMsg: String,val message:String,val datas: T)