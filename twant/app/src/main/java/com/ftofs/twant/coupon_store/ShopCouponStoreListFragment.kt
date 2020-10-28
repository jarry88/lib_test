package com.ftofs.twant.coupon_store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftofs.lib_net.model.CouponDetailVo
import com.ftofs.lib_net.model.CouponItemVo
import com.ftofs.twant.R
import com.ftofs.twant.BR
import com.ftofs.twant.config.Config
import com.ftofs.twant.databinding.CouponStoreItemBinding
import com.ftofs.twant.databinding.ShopCouponStoreListFragmentBinding
import com.ftofs.twant.fragment.ShopActivityFragment
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.util.Util
import com.gzp.lib_common.base.BaseTwantFragmentMVVM

private const val COUPON_ID ="couponId"

/**
 * 【】
 */
class ShopCouponStoreListFragment:BaseTwantFragmentMVVM<ShopCouponStoreListFragmentBinding,CouponStoreViewModel>() {
    var shopActivityFragment:ShopActivityFragment?=null
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.shop_coupon_store_list_fragment
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun onBackPressedSupport(): Boolean {
        return shopActivityFragment?.onBackPressedSupport()?:super.onBackPressedSupport()
    }
    val mAdapter by lazy { object :DataBoundAdapter<CouponItemVo, CouponStoreItemBinding> (){
        override val layoutId: Int
            get() = R.layout.coupon_store_item

        override fun initView(binding: CouponStoreItemBinding, item: CouponItemVo) {
//
        }

    }.apply { showEmptyView(true) }}

    override fun initData() {
//        shopActivityFragment?.apply {
//            viewModel.getShopCouponStoreList(storeId)
////            hideCouponStoreView()
//        }
        binding.listView.apply {
            config<CouponDetailVo, CouponStoreItemBinding>(R.layout.coupon_store_item,viewModel.couponStoreList){ b, d->
                b.vo=d
                b.root.setOnClickListener{Util.startFragment(CouponStoreDetailFragment.newInstance(d.id))}
            }
            setLoadMoreListener { shopActivityFragment?.let { viewModel.getActivityList(it.storeId)
//                if(Config.USE_DEVELOPER_TEST_DATA) viewModel.couponStoreList.postValue(listOf(CouponItemVo(null,null,null,null,null,null,null,null,null,null,null,null,null)))
            } }
            setRefreshListener { viewModel.currPage=0
                shopActivityFragment?.let { viewModel.getActivityList(it.storeId) } }
        }
        binding.listView.autoRefresh()
    }

    companion object {
        @JvmStatic
        fun newInstance(storeId: Int,parent:ShopActivityFragment):ShopCouponStoreListFragment {
            val args = Bundle()
            args.putInt(COUPON_ID,storeId)
            return ShopCouponStoreListFragment().apply {
                arguments=args
                shopActivityFragment=parent
            }
        }
    }

    override fun initViewObservable() {
        viewModel.stateLiveData.stateEnumMutableLiveData.observe(this){
            binding.listView.endLoadingUi()
        }
    }
}