package com.ftofs.twant.kotlin.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ftofs.twant.kotlin.base.BaseViewModel
import com.ftofs.twant.kotlin.vo.RandomMemberVo
import com.ftofs.twant.log.SLog
import com.ftofs.twant.vo.CategoryNavVo


class RandomFriendViewModel(application: Application) : BaseViewModel(application){
    val randomMemberList by lazy { MutableLiveData<List<RandomMemberVo>>() }

    fun getMemberList(){
        launch(stateLiveData,
                {repository.run {
                    simpleGet(api.getRandomMemberList()) }},
                {
                    randomMemberList.value=it.randomMemberList
                }
        )
    }
}
