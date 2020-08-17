package com.ftofs.twant.fragment

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.databinding.ImGoodsLayoutBinding
import com.ftofs.twant.databinding.ZoneGoodsListItemBinding
import com.ftofs.twant.entity.Goods
import com.ftofs.twant.kotlin.BaseTwantFragmentMVVM
import com.ftofs.twant.kotlin.ImGoodsViewModel
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.widget.ScaledButton
import com.google.android.material.tabs.TabLayoutMediator

class ImGoodsFragment:BaseTwantFragmentMVVM <ImGoodsLayoutBinding, ImGoodsViewModel>(){
    private  val pageList = arrayListOf<ImGoodsListPage>()
    private val  tabTextList = arrayOf(
            "推薦商品","最近瀏覽","我的關注","購物袋","本店商品"
    )
    private val pageAdapter by lazy {
        object :FragmentStateAdapter(this){
            override fun getItemCount(): Int {
                TODO("Not yet implemented")
            }

            override fun createFragment(position: Int): Fragment {
                TODO("Not yet implemented")
            }

        }
    }
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.im_goods_layout
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    companion object {
        @JvmStatic
        fun newInstance(): ImGoodsFragment {
            return ImGoodsFragment()
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun initParam() {
        //获取列表传入的实体
        super.initParam()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    }
    val adapter=object :DataBoundAdapter<Goods, ZoneGoodsListItemBinding>(){
        override val layoutId: Int
            get() = R.layout.zone_goods_list_item

        override fun initView(binding: ZoneGoodsListItemBinding, item: Goods) {
            binding.tvGoodsName.text =item.goodsName
        }

    }
    override fun initData() {
        binding.rlTitleContainer.findViewById<TextView>(R.id.tv_title).text="商品"
        binding.rlTitleContainer.findViewById<ScaledButton>(R.id.btn_back).setOnClickListener { hideSoftInputPop() }
        TabLayoutMediator(binding.tabs,binding.viewPager){tab, position ->
            tab.text = tabTextList[position]
        }.attach()

        binding.rvRightList.adapter=adapter

        viewModel.getImGoodsSearch()
    }

    override fun onBackPressedSupport(): Boolean {
        hideSoftInputPop()
        return true
    }
}