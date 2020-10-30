package com.ftofs.twant.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.fastjson.JSON
import com.ftofs.lib_net.model.CouponItemVo
import com.ftofs.lib_net.model.CouponOrdersListInfo
import com.ftofs.lib_net.model.PropertyVo
import com.ftofs.twant.R
import com.ftofs.twant.BR
import com.ftofs.twant.coupon_store.CouponOrderListFragment
import com.ftofs.twant.coupon_store.CouponPayResultFragment
import com.ftofs.twant.databinding.CouponListItemWighetBinding
import com.ftofs.twant.databinding.ItemHouseVoBinding
import com.ftofs.twant.databinding.SearchSuggestionItemBinding
import com.ftofs.twant.databinding.TestBlackFragmentBinding
import com.ftofs.twant.dsl.customer.factoryAdapter
import com.ftofs.twant.go853.Go853HouseListFragment
import com.ftofs.twant.go853.GoDropdownMenu
import com.ftofs.twant.go853.GoSearchType
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.util.SearchHistoryUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.wzq.mvvmsmart.base.BaseFragmentMVVM
import com.wzq.mvvmsmart.utils.KLog
import com.wzq.mvvmsmart.utils.ToastUtils
import com.gzp.lib_common.utils.SLog
import com.gzp.lib_common.utils.ToastUtil
import com.gzp.lib_common.utils.Util
import com.lxj.xpopup.XPopup

class BlackTestFragment :BaseTwantFragmentMVVM<TestBlackFragmentBinding,TestViewModel>(){
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.test_black_fragment
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }
    val mAdapter=
    factoryAdapter<CouponItemVo, CouponListItemWighetBinding>(R.layout.coupon_list_item_wighet){ b, d ->
        b.vo=d
    }
    private val suggestAdapter by lazy {
        object : DataBoundAdapter<String, ItemHouseVoBinding>(){
//            val parent =parentFragment as? Go853HouseListFragment
            override val layoutId: Int
                get() = R.layout.item_house_vo

            override fun initView(binding: ItemHouseVoBinding, item: String) {
//                binding.tvSuggestionKeyword.text=item
//                binding.root.setOnClickListener {
//                    SLog.info("s")
////                    parent?.apply {
////                        doSearchKeyword(item)
////                    }
//                }
            }

        }
    }

    override fun initData() {
        binding.button1.apply {
            text="koin"
            setOnClickListener {
                viewModel.getData()
                com.ftofs.twant.util.Util.startFragment(CouponPayResultFragment.newInstance(4105,success = false))

            }
        }
        binding.button2.apply {
            text="klog"
            setOnClickListener {
                ToastUtil.success(context,"打开")
                KLog.init(true)
                com.ftofs.twant.util.Util.startFragment(CouponPayResultFragment.newInstance(4105,success = true))

            }

        }
        binding.button3.apply{
            text="下拉彈窗"
            setOnClickListener {
            XPopup.Builder(_mActivity)
                    .moveUpToKeyboard(false)
                    .hasShadowBg(false)
                    .atView(this)
                    .asCustom(
                    GoDropdownMenu(context, listOf("1","2","3","4"),"2")
            ).show()
        }}
        binding.button5.apply{
            text="訂單"
            setOnClickListener {start(CouponOrderListFragment.newInstance())
        }
        binding.button6.apply{
            text="Mpay 参数"
            setOnClickListener {start(CouponOrderListFragment.newInstance())
        }}
        }
        binding.button4.setOnClickListener { start(Go853HouseListFragment()) }
        binding.rlList.adapter=mAdapter
//        mAdapter.addAll(listOf(CouponItemVo(null,null,null,null,null,null,null,null,"null",null,null,null,null)),true)
        suggestAdapter.addAll(SearchHistoryUtil.loadSearchHistory(GoSearchType.All.ordinal).map { it.keyword },true)

    }
    inline fun <reified T:Any> getJsonData(jsonStr:String):T{
        SLog.info(jsonStr)
//    GsonConverterFactory.create().
        return  JSON.parseObject(jsonStr,T::class.java)
    }
    inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)
}