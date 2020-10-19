package com.ftofs.lib_net.net

data class TwantResponse<out T>(val code: Int, val errorMsg: String,val message:String,val datas: T?=null,val data:T?=null)