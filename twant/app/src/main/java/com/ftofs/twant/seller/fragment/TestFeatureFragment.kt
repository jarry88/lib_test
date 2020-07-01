package com.ftofs.twant.seller.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ftofs.twant.R
import com.ftofs.twant.databinding.SellerEditFeaturesLayoutBinding
import com.ftofs.twant.entity.SellerGoodsItem
import com.ftofs.twant.kotlin.BR
import com.ftofs.twant.kotlin.BaseTwantFragmentMVVM
import com.ftofs.twant.kotlin.FeatureGoodViewModel
import com.ftofs.twant.kotlin.SellerGoodsListAdapter
import com.wzq.mvvmsmart.utils.KLog
import com.wzq.mvvmsmart.utils.LoadingUtil
import com.wzq.mvvmsmart.utils.ToastUtils
import java.util.ArrayList

/**
 * 详情界面
 */
class TestFeatureFragment : BaseTwantFragmentMVVM<SellerEditFeaturesLayoutBinding, FeatureGoodViewModel>(){
    private lateinit var sellerGoodsListAdapter: SellerGoodsListAdapter
    private var goodsList: List<SellerGoodsItem> = ArrayList()
    private var loadingUtil: LoadingUtil? = null

    companion object{
        fun newInstance(): TestFeatureFragment {
            val args = Bundle()
            val fragment = TestFeatureFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun initParam() {
        //获取列表传入的实体
        super.initParam()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    }

    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.seller_edit_features_layout
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        loadingUtil = LoadingUtil(activity)
        viewModel.doGetServerNews() //请求网络数据
        initRecyclerView()
    }
    private fun initRecyclerView() {
        sellerGoodsListAdapter = SellerGoodsListAdapter(R.layout.seller_goods_item_unswipe, goodsList)
        binding.layoutManager = LinearLayoutManager(activity)
        binding.adapter = sellerGoodsListAdapter
        sellerGoodsListAdapter.setOnItemClickListener { adapter, view, position -> ToastUtils.showShort("点击了条目--" + position) }
        sellerGoodsListAdapter.onItemLongClickListener = BaseQuickAdapter.OnItemLongClickListener { adapter, view, position ->
            ToastUtils.showShort("长按了条目--" + position)
            true
        }

        sellerGoodsListAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.goods_image) {
                KLog.e("点击了button")
                val goodsData = goodsList[position]
                //删除选择对话框
                val builder = AlertDialog.Builder(activity as Context)
                builder.setTitle("尊敬的用户")
                builder.setMessage("你真的要卸载我吗？")
                builder.setPositiveButton("残忍卸载") { dialog, which ->
                    viewModel.deleteItem(goodsData)
                    //                            mAdapter.remove(position);
                    sellerGoodsListAdapter.notifyItemRemoved(position)
                    builder.setNegativeButton("我再想想") { dialog, which ->
                    }
                    val alert = builder.create()
                    alert.show()
                }
            }
        }
    }
}