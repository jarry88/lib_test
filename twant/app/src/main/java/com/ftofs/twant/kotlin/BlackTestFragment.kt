package com.ftofs.twant.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.fastjson.JSON
import com.ftofs.twant.R
import com.ftofs.twant.BR
import com.ftofs.twant.databinding.TestBlackFragmentBinding
import com.ftofs.twant.go853.Go853HouseListFragment
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

    override fun initData() {
        binding.button1.setOnClickListener {
            viewModel.getData()
        }
        binding.button2.setOnClickListener {
            ToastUtil.success(context,"打开")
            KLog.init(true)
        }
        binding.button3.setOnClickListener {
            viewModel.getTest()
        }
        binding.button4.setOnClickListener { start(Go853HouseListFragment()) }
    }
    inline fun <reified T:Any> getJsonData(jsonStr:String):T{
        SLog.info(jsonStr)
//    GsonConverterFactory.create().
        return  JSON.parseObject(jsonStr,T::class.java)
    }
    inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)
}