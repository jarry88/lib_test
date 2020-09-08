package com.ftofs.twant.tangram

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.constant.Constant
import com.ftofs.twant.databinding.RandomFriendListItemBinding
import com.ftofs.twant.databinding.RandomFriendListLayoutBinding
import com.ftofs.twant.fragment.ChatFragment
import com.ftofs.twant.kotlin.BaseTwantFragmentMVVM
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.kotlin.ui.RandomFriendViewModel
import com.ftofs.twant.kotlin.vo.RandomMemberVo
import com.ftofs.twant.log.SLog
import com.ftofs.twant.orm.FriendInfo
import com.ftofs.twant.util.ChatUtil
import com.ftofs.twant.util.Util


class RandomFriendListFragment:BaseTwantFragmentMVVM<RandomFriendListLayoutBinding, RandomFriendViewModel>() {

    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.random_friend_list_layout

    }
    override fun initVariableId(): Int {
        return BR.viewModel

    }
    val mAdapter by lazy { object : DataBoundAdapter<RandomMemberVo,RandomFriendListItemBinding>(){
        override val layoutId: Int
            get() = R.layout.random_friend_list_item

        override fun initView(binding: RandomFriendListItemBinding, item: RandomMemberVo) {
            binding.vo=item
            binding.root.setOnClickListener {
                try {
                    val friendInfo = FriendInfo()
                    friendInfo.memberName = item.memberName
                    friendInfo.nickname = item.nickName
                    friendInfo.avatarUrl = item.avatar
                    friendInfo.role = if(item.isSeller==Constant.TRUE_INT) ChatUtil.ROLE_CS_AVAILABLE else ChatUtil.ROLE_MEMBER
                    val conversation = ChatUtil.getConversation(item.memberName,
                            item.nickName, item.avatar,friendInfo.role )

                    Util.startFragment(ChatFragment.newInstance(conversation,friendInfo))
                }catch (e:Exception){
                    SLog.info(e.toString())
                }

        }
        }

    }}
    override fun initData() {
        binding.rvList.adapter= mAdapter
        binding.rlTools.tvTitle.text="城友列表"
        binding.rlTools.btnBack.setOnClickListener { onBackPressedSupport() }
        viewModel.getMemberList()
    }

    override fun initViewObservable() {
        viewModel.randomMemberList.observe(this, Observer {
            SLog.info(it.size.toString())
            mAdapter.addAll(it,true) })
    }
}