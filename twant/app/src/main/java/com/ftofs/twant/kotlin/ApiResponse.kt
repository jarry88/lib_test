package com.ftofs.twant.kotlin

class ApiResponse<T>(
    var data:T?,
    var errorCode:Int,
    var errorMsg:String
)