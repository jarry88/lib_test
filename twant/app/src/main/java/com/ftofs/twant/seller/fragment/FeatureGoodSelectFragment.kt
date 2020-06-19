package com.ftofs.twant.seller.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ftofs.twant.R
import com.ftofs.twant.databinding.SellerEditFeaturesLayoutBinding
import com.ftofs.twant.fragment.AboutFragment
import com.ftofs.twant.kotlin.BaseKotlinFragment
import com.ftofs.twant.kotlin.HomeVM
import com.ftofs.twant.kotlin.KotlinInterfaceApi
import com.ftofs.twant.kotlin.adapter.FeatureGoodAdapter
import com.ftofs.twant.kotlin.extension.viewModel
import com.ftofs.twant.log.SLog
import com.ftofs.twant.util.User

//在Fragment中使用，引入资源文件，直接使用id访问视图有一点特别注意：
// 在onCreateView中不直接访问视图，因为视图没有加载完成，容易引起空指针，需要在onViewCreated中访问视图，代码如下：
class FeatureGoodSelectFragment: BaseKotlinFragment<SellerEditFeaturesLayoutBinding>() {
    override val layoutId: Int
        get() = R.layout.seller_edit_features_layout
    val vm by viewModel<HomeVM>()
    private val adapter = FeatureGoodAdapter()

    companion object{
        fun newInstance(): FeatureGoodSelectFragment {
            val args = Bundle()
            val fragment = FeatureGoodSelectFragment()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.vm = vm
        binding.executePendingBindings()
        binding.refreshLayout.autoRefresh()
        initRecyclerView()
        vm.back.observe(this, Observer {
            if (it) {
                hideSoftInputPop()
            }
        })
        val bannerList = KotlinInterfaceApi.get().bannerList()
        bannerList.observe(this, Observer {
            SLog.info("${it?.datas?.appIndexNavigationLinkType},${it?.datas.toString()}")
        })
    }
    private fun initRecyclerView() {
        binding.rvFeaturesGoodsList.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(activity)
        }

        vm.goodsItems.observe(this, Observer {
            if (it != null) {
                SLog.info("${it.goodsList}")
                adapter.addAll(it.goodsList, it.pageEntity.curPage == 1)
            }
        })
    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
////        var saleGoodsList :MutableList<SellerGoodsItem> = MutableList()
//        val btnBack =view.findViewById(R.id.btn_back) as ScaledButton
//        btnBack.setOnClickListener { hideSoftInputPop() }
//        tv_title.text="出售中商品列表"
//
//        val saleGoodsList =ArrayList<SellerGoodsItem>()
//        val adapter= KotlinBaseAdapter.Builder<SellerGoodsItem>()
//                .setData(saleGoodsList)
//                .setLayoutId(R.layout.seller_goods_item)
//                .addBindView { itemView, itemData ->
//                    itemView.tv_goods_name.text=itemData.goodsName?:"未设置"
//                }.create()
//        for (i in 0..10) {
//            saleGoodsList.add(SellerGoodsItem())
//        }
//        adapter.notifyDataSetChanged()
//        rv_features_goods_list.layoutManager=LinearLayoutManager(context)
//        rv_features_goods_list.adapter=adapter
//
//        getSellerList()
//    }
//    companion object{
//        fun newInstance(): FeatureGoodSelectFragment {
//            return FeatureGoodSelectFragment()
//        }
//    }
//
//    override fun onBackPressedSupport(): Boolean {
//        hideSoftInputPop()
//        return true
//    }
//
//    private fun getSellerList(){
//        User.getToken()?:return
//        val saleList = KotlinInterfaceApi.get().sellerGoodsList(User.getToken())
//        saleList.observe(this, Observer {
//            SLog.info("res$it")
//        })
//        val goodsItems: LiveData<List<SellerGoodsItem>> = Transformations.map(saleList) {
//            it.data ?: ArrayList()
//        }
//    }
//    class ClickProxy(){
//        fun print() {
//            SLog.info("dsf")
//        }
//    }
}