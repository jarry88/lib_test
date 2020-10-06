package com.ftofs.twant.hot_zone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftofs.lib_net.model.HotZoneInfo
import com.ftofs.lib_net.model.HotZoneVo
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.config.Config
import com.ftofs.twant.constant.Constant
import com.ftofs.twant.databinding.FragmentHotzoneBinding
import com.ftofs.twant.kotlin.adapter.DslAdapter
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.wzq.mvvmsmart.event.StateLiveData
import com.gzp.lib_common.utils.SLog
import com.wzq.mvvmsmart.net.net_utils.RetrofitUtil
import retrofit2.http.GET
import kotlin.reflect.full.primaryConstructor

class HotZoneFragment(private val hotId: Int) :BaseTwantFragmentMVVM<FragmentHotzoneBinding, HotZoneViewModel> (){
    private val mAdapter by lazy {
//        object :DataBoundAdapter<HotZoneVo,ItemHotZoneVoBinding>(){
//            override val layoutId: Int
//                get() = R.layout.item_hot_zone_vo
//
//            override fun initView(binding: ItemHotZoneVoBinding, item: HotZoneVo) {
//                binding.hotZoneItem.apply {
//                    hotZoneVo.value=item
//                }
//                Glide.with(context).load(item.url).centerCrop().into(binding.test)
//            }
//
//        }
        object : DslAdapter<HotZoneVo, HotView>(HotView::class.primaryConstructor) {
            override fun initView(view: HotView, item: HotZoneVo) {
                view.hotZoneVo.value=item
            }

        }
//        object : BaseQuickAdapter<HotZoneVo, BaseViewHolder>(R.layout.item_hot_zone_vo) {
//            override fun convert(helper: BaseViewHolder, item: HotZoneVo?) {
//                val hotItem = helper.getView<HotView>(R.id.hot_zone_item)
//            }
//        }
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
//        if (Config.DEVELOPER_MODE) {
//            binding.title.setTitleClickListener{
//                viewModel.getHotTestZoneData(12)
//            }
//        }
        viewModel.getHotZoneData(hotId)
    }

    override fun onSupportInvisible() {
        super.onSupportInvisible()
        if (Config.DEVELOPER_MODE) {
//            if (Config.currEnv == Config.ENV_28) {
//                RetrofitUtil.getInstance().changeBaseApiUrl(Config.BASE_URL_API_28)
//
//            } else {
//                RetrofitUtil.getInstance().changeBaseApiUrl(Config.BASE_URL_API_29)
//            }
            SLog.info("測試環境api切換到28,29環境")
        }
    }

    override fun initViewObservable() {
        viewModel.hotZoneInfo.observe(this){
            SLog.info("观测到hotZoneInfo数据")
            binding.title.text=it.hotName
            SLog.info(it.hotZoneVoList.toString())
            it.hotZoneVoList.takeIf { it.isNotEmpty() }?.apply {
                mAdapter.addAll(this, true)
            }
        }
        viewModel.stateLiveData.stateEnumMutableLiveData.observe(this){
            when(it){
                StateLiveData.StateEnum.Error -> SLog.info("异常")
            }
        }
    }
}