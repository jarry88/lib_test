package com.ftofs.twant.go853

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.ftofs.lib_net.model.PropertyVo
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.databinding.GoPropertyDetailFragmentBinding
import com.ftofs.twant.entity.WebSliderItem
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.util.Util
import com.ftofs.twant.view.BannerViewHolder
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.config.Config
import com.gzp.lib_common.utils.SLog
import com.gzp.lib_common.utils.pushUmengEvent
import com.zhouwei.mzbanner.holder.MZHolderCreator

class GoPropertyDetailFragment @JvmOverloads constructor(private val pid: Int = -1, private val propertyVo: PropertyVo? = null) : BaseTwantFragmentMVVM<GoPropertyDetailFragmentBinding, GoHouseViewModel>() {
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.go_property_detail_fragment
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        if(pid==-1)hideSoftInputPop()
        binding.title.apply {
            text ="房產詳情頁"
            setLeftImageResource(R.drawable.icon_back)
            setLeftLayoutClickListener{onBackPressedSupport()}
        }
//        binding.banner.apply {
////            mei
//        }

        binding.btnAboutOwner.setOnClickListener{
            Util.startFragment(GoIntermediaryListFragment(viewModel.currPropertyInfo.value?.uid))
        }
        binding.btnMobile.setOnClickListener{
            viewModel.currPropertyInfo.value?.mobile?.let {
                pushUmengEvent(com.ftofs.twant.config.Config.PROD, GO853_CALL_MOBILE, hashMapOf("mobile" to it,"pid" to viewModel.currPropertyInfo.value?.pid))
                Util.dialPhone(activity, it)
            }?:ToastUtil.error(context, getString(R.string.text_seller_phone_not_set)).apply {
                pushUmengEvent(com.ftofs.twant.config.Config.PROD, GO853_CALL_MOBILE, hashMapOf("mobile" to "無","pid" to viewModel.currPropertyInfo.value?.pid))
            }
        }
        binding.btnLink.setOnClickListener{
            viewModel.currPropertyInfo.value?.mobile?.let {
                pushUmengEvent(com.ftofs.twant.config.Config.PROD, GO853_CALL_MOBILE, hashMapOf("mobile" to it,"pid" to viewModel.currPropertyInfo.value?.pid))

                SLog.info(it)
                Util.dialPhone(activity, it)
            }?:ToastUtil.error(context, getString(R.string.text_seller_phone_not_set)).apply {
                pushUmengEvent(com.ftofs.twant.config.Config.PROD, GO853_CALL_MOBILE, hashMapOf("mobile" to "無","pid" to viewModel.currPropertyInfo.value?.pid))

            }
        }
        propertyVo?.let{
            viewModel.currPropertyInfo.postValue(it)
        }
        viewModel.getPropertyDetail(pid)

    }

    override fun initViewObservable() {
        viewModel.currPropertyInfo.observe(this){
            binding.vo=it
            it.photoList.takeIf { l-> l.isNotEmpty() }?.let { list ->
                list.map { p -> WebSliderItem(p.title, "none", "", "", "[]") }
                        .let {
                            binding.banner.setPages(it, MZHolderCreator<BannerViewHolder> { BannerViewHolder(it) })
                            binding.banner.setIndicatorRes(R.drawable.white_banner_indicator_normal, R.drawable.gray_banner_indicator_normal)
                            val padding = Util.dip2px(context, 10f)
                            val linearLayout: LinearLayout = binding.banner.indicatorContainer
                            for (i in 0 until linearLayout.childCount) {
                                SLog.info("第%d個", i)
                                val imageView = linearLayout.getChildAt(i) as ImageView
                                val layoutParams = imageView.layoutParams as LinearLayout.LayoutParams
                                imageView.setPadding(padding, 0, padding, 0)
                            }
//                            binding.banner.start()
                        }
            }
        }
    }
}
