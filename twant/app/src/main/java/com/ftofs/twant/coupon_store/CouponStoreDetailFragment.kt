package com.ftofs.twant.coupon_store

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.databinding.CouponStoreDetailFragmentBinding
import com.ftofs.twant.dsl.*
import com.ftofs.twant.kotlin.extension.removeParent
import com.ftofs.twant.util.ToastUtil
import com.gzp.lib_common.base.BaseTwantFragmentMVVM

private const val COUPON_ID ="couponId"
class CouponStoreDetailFragment():BaseTwantFragmentMVVM<CouponStoreDetailFragmentBinding,CouponStoreViewModel>() {
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.coupon_store_detail_fragment
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }
    val id=arguments?.getInt(COUPON_ID)

    override fun initData() {
        binding.title.apply {
            setLeftImageResource(R.drawable.icon_back)
            setRightImageResource(R.drawable.icon_coupon_share)
            setRightLayoutClickListener{ToastUtil.success(context,"分享")}
            setLeftLayoutClickListener{onBackPressedSupport()}
        }
        id?.let {
            viewModel.getCouponDetail(it)
        }?:viewModel.getCouponDetail(42)
    }

    companion object {
        @JvmStatic
        fun newInstance(couponId: Int?):CouponStoreDetailFragment {
            val args = Bundle()
            couponId?.let {
                args.putInt(COUPON_ID,it)
            }
            val  fragment =CouponStoreDetailFragment()
            fragment.arguments=args
            return fragment
        }
    }

    override fun initViewObservable() {
        viewModel.currCouponDetail.observe(this){
            binding.vo=it
            binding.llPkgContainer.apply {
                removeAllViews()
                addView(TextView {
                    layout_width= wrap_content
                    layout_height= wrap_content
                    textStyle= bold
                    setTextColor(resources.getColor(R.color.tw_black))
                    textSize=18f
                    margin_bottom=10
                    text ="店内套餐"
                }.let {t ->  (t.parent as? ViewGroup)?.removeView(t)
                    t
                }, android.widget.LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
                it.pkgList.forEach {pkg ->
                    addView(RelativeLayout {
                        layout_width= match_parent
                        layout_height= wrap_content
                        TextView {
                            layout_width= wrap_content
                            layout_height= wrap_content
                            alignParentStart=true
                            layout_gravity = gravity_center_vertical
                            text = pkg.title
                            textSize =14f
                            textStyle= bold
                            setTextColor(resources.getColor(R.color.tw_black))
                            id=R.id.item_title
                            margin_start=28

                            isSingleLine =true
                        }
                        TextView {
                            layout_width= wrap_content
                            layout_height= wrap_content
                            end_toStartOf =R.id.item_title.toString()
                            margin_start=15
                            textStyle= bold
                            colorId=R.color.tw_black
                            gravity= gravity_center
                            textSize=24f
                            text="·"
                            padding_bottom=4
                        }
                    }.let { r ->( r.parent as? ViewGroup)?.removeView(r)
                        r })

                    pkg.childItems.forEach {child ->
                        addView(LinearLayout {
                            layout_height = wrap_content
                            layout_width= wrap_content
                            orientation = horizontal
                            padding=6
                            padding_start=28
                            padding_end=14
                            TextView {
                                colorId =R.color.tw_black
                                layout_height= wrap_content
                                layout_width= wrap_content
                                textSize=14f
                                text =SpannableStringBuilder("${child.title}  ${child.unit}").also {s->
                                    s.setSpan(ForegroundColorSpan(resources.getColor(R.color.tw_grey_666)),s.length-child.unit.length,s.length-1,Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                                }
                            }
                            View{
                                layout_height= wrap_content
                                layout_width=0
                                weight=1f
                            }
                            TextView {
                                layout_height= wrap_content
                                layout_width= wrap_content
                                textSize=15f
                                text=SpannableStringBuilder("$${child.price}").also {s->
                                    s.setSpan(AbsoluteSizeSpan(11,true),0,1,Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                                }
                                textStyle= bold
                                colorId=R.color.tw_black
                            }

                        }.let { r ->( r.parent as? ViewGroup)?.removeView(r)
                            r })
                    }
                }
            }
        }
    }
}

