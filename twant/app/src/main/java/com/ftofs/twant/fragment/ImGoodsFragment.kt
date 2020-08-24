package com.ftofs.twant.fragment

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.databinding.ImGoodsLayoutBinding
import com.ftofs.twant.databinding.ZoneGoodsListItemBinding
import com.ftofs.twant.entity.Goods
import com.ftofs.twant.interfaces.OnSelectedListener
import com.ftofs.twant.kotlin.BaseTwantFragmentMVVM
import com.ftofs.twant.kotlin.ImGoodsViewModel
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.kotlin.ui.ImGoodsSearch.ImGoodsEnum
import com.ftofs.twant.log.SLog
import com.ftofs.twant.widget.ScaledButton
import com.ftofs.twant.widget.TestCenterPopup
import com.google.android.material.tabs.TabLayoutMediator
import com.lxj.xpopup.XPopup

class ImGoodsFragment(val targetName:String,val sendGoods: OnSelectedListener):BaseTwantFragmentMVVM <ImGoodsLayoutBinding, ImGoodsViewModel>(){
    private val pageList = arrayListOf<ImGoodsListPage>()
    private val tabTextList by lazy {
        enumValues<ImGoodsEnum>().apply { forEach { SLog.info(it.toString()) } }
    }
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.im_goods_layout
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    companion object {
        @JvmStatic
        fun newInstance(targetName: String,sendGoods: OnSelectedListener): ImGoodsFragment {
            return ImGoodsFragment(targetName,sendGoods )
        }
    }

    val adapter by lazy {
        object :DataBoundAdapter<Goods, ZoneGoodsListItemBinding>(){
            override val layoutId: Int
                get() = R.layout.zone_goods_list_item

            override fun initView(binding: ZoneGoodsListItemBinding, item: Goods) {
                binding.tvGoodsName.text =item.goodsName
            }

        }
    }
    private val fragmentStateAdapter by lazy {
        object :FragmentStateAdapter(this){
            val NUM_PAGES= pageList.size
            override fun getItemCount(): Int {
                return NUM_PAGES
            }

            override fun createFragment(position: Int): Fragment {
                return pageList[position]
            }

        }
    }
    @SuppressLint("SourceLockedOrientationActivity")
    override fun initParam() {
        //获取列表传入的实体
        super.initParam()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    }
    override fun initData() {
        binding.viewModel=viewModel
        tabTextList.forEach { pageList.add(ImGoodsListPage(it,this)) }
        binding.rlTitleContainer.tvTitle.apply {
            text="商品"
            setOnClickListener{
                XPopup.Builder(context).asCustom(TestCenterPopup(context!!) ).show()
            }
        }
        binding.rlTitleContainer.btnBack.setOnClickListener { hideSoftInputPop() }
        binding.viewPager.adapter =fragmentStateAdapter
        TabLayoutMediator(binding.tabs,binding.viewPager){tab, position ->
            tab.text = tabTextList[position].title
        }.attach()
//        binding.tabs.setSelectedTabIndicator(R.drawable.tab_indicator)

        binding.rvRightList.adapter=adapter

    }

    override fun onBackPressedSupport(): Boolean {
        hideSoftInputPop()
        return true
    }
}