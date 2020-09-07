package com.ftofs.twant.tangram

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.databinding.RandomFriendListItemBinding
import com.ftofs.twant.databinding.RandomFriendListLayoutBinding
import com.ftofs.twant.entity.ChatConversation
import com.ftofs.twant.kotlin.BaseTwantFragmentMVVM
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.kotlin.ui.RandomFriendViewModel

class RandomFriendListFragment:BaseTwantFragmentMVVM<RandomFriendListLayoutBinding, RandomFriendViewModel>() {


    override fun initVariableId(): Int {
        return R.layout.random_friend_list_layout
    }
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return BR.viewModel
    }

    val mAdapter by lazy { object : DataBoundAdapter<ChatConversation,RandomFriendListItemBinding>(){
        override val layoutId: Int
            get() = R.layout.random_friend_list_item

        override fun initView(binding: RandomFriendListItemBinding, item: ChatConversation) {
            binding.vo=item
        }

    }}
    override fun initData() {
        binding.rvList.adapter= mAdapter
    }
}