package com.ftofs.twant.seller.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftofs.twant.R
import com.ftofs.twant.databinding.SellerEditFeaturesLayoutBinding
import com.ftofs.twant.entity.SellerGoodsItem
import com.ftofs.twant.kotlin.BR
import com.ftofs.twant.kotlin.FeatureGoodModel
import com.wzq.mvvmsmart.base.BaseFragmentMVVM
import me.yokeyword.fragmentation.ExtraTransaction
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragmentDelegate
import me.yokeyword.fragmentation.anim.FragmentAnimator

/**
 * 详情界面
 */
class TestFeatureFragment : BaseFragmentMVVM<SellerEditFeaturesLayoutBinding, FeatureGoodModel>(), ISupportFragment{
    private var entity: SellerGoodsItem? = null

    companion object{
        fun newInstance(): TestFeatureFragment {
            val args = Bundle()
            val fragment = TestFeatureFragment()
            fragment.arguments = args
            return fragment
        }
    }
    override fun initParam() {
        //获取列表传入的实体
        val mBundle = arguments
        if (mBundle != null) {
            entity = mBundle.getParcelable("entity")
        }
    }

    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.seller_edit_features_layout
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.setDemoEntity(entity)
    }

    override fun setFragmentResult(resultCode: Int, bundle: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onSupportInvisible() {
        TODO("Not yet implemented")
    }

    override fun onNewBundle(args: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun extraTransaction(): ExtraTransaction {
        TODO("Not yet implemented")
    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        TODO("Not yet implemented")
    }

    override fun enqueueAction(runnable: Runnable?) {
        TODO("Not yet implemented")
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun setFragmentAnimator(fragmentAnimator: FragmentAnimator?) {
        TODO("Not yet implemented")
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun getFragmentAnimator(): FragmentAnimator {
        TODO("Not yet implemented")
    }

    override fun isSupportVisible(): Boolean {
        TODO("Not yet implemented")
    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onSupportVisible() {
        TODO("Not yet implemented")
    }

    override fun onBackPressedSupport(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getSupportDelegate(): SupportFragmentDelegate {
        TODO("Not yet implemented")
    }

    override fun putNewBundle(newBundle: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun post(runnable: Runnable?) {
        TODO("Not yet implemented")
    }
}