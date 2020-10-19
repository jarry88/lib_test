package com.ftofs.twant.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.fastjson.JSON
import com.ftofs.lib_net.model.PropertyVo
import com.ftofs.twant.R
import com.ftofs.twant.BR
import com.ftofs.twant.databinding.ItemHouseVoBinding
import com.ftofs.twant.databinding.SearchSuggestionItemBinding
import com.ftofs.twant.databinding.TestBlackFragmentBinding
import com.ftofs.twant.go853.Go853HouseListFragment
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

class BlackTestFragment :BaseTwantFragmentMVVM<TestBlackFragmentBinding,TestViewModel>(){
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.test_black_fragment
    }

    override fun initVariableId(): Int {
        return BR.viewModel
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
        binding.button1.setOnClickListener {
            viewModel.getData()
        }
        binding.button2.setOnClickListener {
            ToastUtil.success(context,"打开")
            KLog.init(true)
        }
        binding.button3.setOnClickListener {
            viewModel.getPropertyList()
        }
        binding.button4.setOnClickListener { start(Go853HouseListFragment()) }
        binding.rlList.adapter=suggestAdapter
        suggestAdapter.addAll(SearchHistoryUtil.loadSearchHistory(GoSearchType.All.ordinal).map { it.keyword },true)

    }
    inline fun <reified T:Any> getJsonData(jsonStr:String):T{
        SLog.info(jsonStr)
//    GsonConverterFactory.create().
        return  JSON.parseObject(jsonStr,T::class.java)
    }
    inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)
}