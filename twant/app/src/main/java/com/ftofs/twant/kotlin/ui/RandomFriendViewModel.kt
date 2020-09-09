package com.ftofs.twant.kotlin.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ftofs.twant.kotlin.base.BaseViewModel
import com.ftofs.twant.kotlin.vo.RandomMemberVo
import com.ftofs.twant.util.User


class RandomFriendViewModel(application: Application) : BaseViewModel(application){
    val randomMemberList by lazy { MutableLiveData<List<RandomMemberVo>>() }

    fun getMemberList(){
        launch(stateLiveData,
                {repository.run {
                    simpleGet(api.getRandomMemberList(User.getToken())) }},
                {
                    randomMemberList.value=it.randomMemberList
                }
        )
    }
}
