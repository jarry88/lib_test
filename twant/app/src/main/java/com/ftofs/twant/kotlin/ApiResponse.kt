package com.ftofs.twant.kotlin

class ApiResponse<T>(
    var datas:T?,
    var code:Int
//    var errorCode:Int,
//    var errorMsg:String
)