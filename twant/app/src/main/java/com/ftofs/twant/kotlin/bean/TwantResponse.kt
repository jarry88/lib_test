package com.ftofs.twant.kotlin.bean

data class TwantResponse<out T>(val code: Int, val errorMsg: String,val message:String,val datas: T)