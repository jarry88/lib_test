package com.ftofs.twant.hot_zone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ftofs.lib_net.model.HotZoneInfo
import com.ftofs.lib_net.model.HotZoneVo
import com.ftofs.twant.R
import com.ftofs.twant.BR
import com.ftofs.twant.databinding.FragmentHotzoneBinding
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.utils.SLog
import com.gzp.lib_common.smart.event.StateLiveData
import retrofit2.http.GET

class HotZoneFragment(private val hotId:Int) :BaseTwantFragmentMVVM<FragmentHotzoneBinding,HotZoneViewModel> (){
    private val mAdapter by lazy {
        object : BaseQuickAdapter<HotZoneVo, BaseViewHolder>(R.layout.item_hot_zone_vo) {
            override fun convert(helper: BaseViewHolder, item: HotZoneVo?) {
                val hotItem = helper.getView<HotView>(R.id.hot_zone_item)
            }
        }
    }
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.fragment_hotzone
    }
    
    override fun initVariableId(): Int {
        return BR.viewModel
    }
    interface TestHotApi{
        @GET("hotzone/index?hotId=12")
        fun getPopularMovie(): HotZoneInfo
    }

    override fun initData() {
        SLog.info("here")
        binding.rvList.adapter=mAdapter
        binding.title.setLeftLayoutClickListener{hideSoftInputPop()}
        binding.test.setOnClickListener {
//            GlobalScope.launch {
////                val re=testApi.getHotZoneIndex(12)
////                SLog.info(re.toString())
////                SLog.info(re.datas.toString())
//
//            }
            SLog.info("测试")

            viewModel.getHotTestZoneData(hotId)
//            viewModel.getHotZoneData(hotId)


        }

        viewModel.getHotZoneData(hotId)
    }

    override fun initViewObservable() {
        viewModel.hotZoneInfo.observe(this){
            SLog.info("观测到hotZoneInfo数据")
            binding.title.text=it.hotName
            SLog.info(it.hotZoneVoList.toString())
//            it.hotZoneVoList.takeIf { it.isNotEmpty() }?.apply {
//                mAdapter.setNewData(this)
//            }
        }
        viewModel.stateLiveData.stateEnumMutableLiveData.observe(this){
            when(it){
                StateLiveData.StateEnum.Error -> SLog.info("异常")
            }
        }
    }
}