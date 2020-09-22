package com.ftofs.lib_net.model

import java.io.Serializable

data class LoginInfo (
    val nickName:String?,
    val imToken:String?,
    val memberName:String?,
    val memberVo:MemberVo?,
    val memberToken:String?,
    val memberId:Int?,
    val token:String?,
    val isBind:Int?
):Serializable
