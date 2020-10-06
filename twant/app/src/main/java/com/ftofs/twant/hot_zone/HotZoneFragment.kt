package com.ftofs.twant.hot_zone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ftofs.lib_net.model.HotZoneInfo
import com.ftofs.lib_net.model.HotZoneVo
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.config.Config
import com.ftofs.twant.databinding.FragmentHotzoneBinding
import com.ftofs.twant.databinding.ItemHotZoneVoBinding
import com.ftofs.twant.dsl.onTouchEvent
import com.ftofs.twant.dsl.scale_fit_xy
import com.ftofs.twant.interfaces.SimpleCallback
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.util.StringUtil
import com.ftofs.twant.util.Util
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.utils.SLog
import com.wzq.mvvmsmart.event.StateLiveData
import retrofit2.http.GET

class HotZoneFragment(private val hotId: Int) :BaseTwantFragmentMVVM<FragmentHotzoneBinding, HotZoneViewModel> (){
    private val mAdapter by lazy {
        object : DataBoundAdapter<HotZoneVo, ItemHotZoneVoBinding>(parentFragment = this),SimpleCallback{
            override val layoutId: Int
                get() = R.layout.item_hot_zone_vo

            override fun initView(binding: ItemHotZoneVoBinding, item: HotZoneVo) {
                binding.vo=item
                Glide.with(context).load(StringUtil.normalizeImageUrl(item.url)).into(binding.hotImage)
                binding.hotImage.apply {
                    adjustViewBounds=true
                    scaleType= scale_fit_xy
                    onTouchEvent={ v, e->
                        when (e.action) {
                            MotionEvent.ACTION_UP -> {
//                                updateP(width, height)
//                                clickX = x
//                                clickY = y
//                                SLog.info("點擊了"+item.originalWidth +" h "+item.originalHeight + e.run { "x :$x,y: $y" })
                                e.run { onClickAction(item,x, y,width,height) }

//                            performClick()
                                true
                            }
                            else ->true
                        }
                    }
                }
            }


             fun onClickAction(hotZoneVo:HotZoneVo?,imgX: Float, imgY: Float,w:Int,h:Int) {
                hotZoneVo?.apply {
                    try {
                        var type:String?=null
                        var value:String?=null
                        hotZoneList?.forEach {
                            val xP= originalWidth?.let { it ->
                                w/it.toFloat()
                            }?:1f
                            val yP= originalHeight?.let { it ->
                                h/it.toFloat()
                            }?:1f
                            SLog.info("點擊   了${imgX/xP},   y:${imgY/yP}\n")
                            SLog.info(it.linkType + it.x + " " + it.width + "y," + it.y + " " + it.height)
                            if (during(imgX, it.x?.toFloat() ?: 0f, it.width?.toFloat() ?: 0f, xP) &&
                                    during(imgY, it.y?.toFloat() ?: 0f, it.height?.toFloat() ?: 0f, yP)) {
                                Util.handleClickLink(it.linkType, it.linkValue,false)
                                return@forEach
                            }

                        }
                    }catch (e: Exception){
                        SLog.info("有異常%s", e.toString())
                    }
                }
            }
            fun during(rawX: Float, x: Float, width: Float, p: Float)=(rawX/p).run {
                SLog.info(this.toString() + "  ${x}+ ${x + width}")
                this>=x&&this<=x+width}

            override fun onSimpleCall(data: Any?) {
                TODO("Not yet implemented")
            }

        }
//        object : DslAdapter<HotZoneVo, HotView>(HotView::class.primaryConstructor) {
//            override fun initView(view: HotView, item: HotZoneVo) {
//                view.hotZoneVo.value=item
//            }
//
//        }
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
    override fun initData() {
        SLog.info("here")
        binding.rvList.adapter=mAdapter
//        binding.title.setTitleClickListener{
//            Util.onLinkTypeAction("postId","2687")
//        }
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


