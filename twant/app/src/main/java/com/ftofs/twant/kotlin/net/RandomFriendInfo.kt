package com.ftofs.twant.kotlin.net

import com.ftofs.twant.kotlin.vo.RandomMemberVo
import com.ftofs.twant.vo.CategoryNavVo
import java.io.Serializable

data class RandomFriendInfo(
    val randomMemberList:List<RandomMemberVo>): Serializable