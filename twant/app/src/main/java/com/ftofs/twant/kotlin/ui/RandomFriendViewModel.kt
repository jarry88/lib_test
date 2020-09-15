package com.ftofs.twant.kotlin.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ftofs.lib_net.BaseRepository
import com.gzp.lib_common.base.BaseViewModel
import com.ftofs.lib_net.model.RandomMemberVo
import com.ftofs.twant.util.User


class RandomFriendViewModel(application: Application) : BaseViewModel(application){
    private val repository by lazy {object : BaseRepository(){} }

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
