package com.ftofs.twant.kotlin

import com.ftofs.twant.R
import com.ftofs.twant.constant.Constant
import com.ftofs.twant.databinding.ZoneGoodsListItemBinding
import com.ftofs.lib_net.model.Goods
import com.ftofs.twant.fragment.GoodsDetailFragment
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.util.User
import com.ftofs.twant.util.Util
import com.ftofs.twant.widget.SpecSelectPopup
import com.lxj.xpopup.XPopup
import com.gzp.lib_common.smart.utils.ToastUtils

/**
 * author : 谷志鹏
 * date   : 2020/07/09 11:34
 */
class BuyerGoodsListAdapter: DataBoundAdapter<Goods, ZoneGoodsListItemBinding>() {
//        helper.setText(R.id.iv2, item.news_title)
override val layoutId: Int
    get()=R.layout.zone_goods_list_item

    override fun initView(binding: ZoneGoodsListItemBinding, item: Goods) {
        binding.vo = item
        binding.ivGoodsAdd.setOnClickListener{
            if(item.goodsStatus==0){
                ToastUtils.showShort("商品已下架");
                return@setOnClickListener
            }
            if (item.goodsStorage == 0) {
                ToastUtils.showShort("該產品已售罄，看看其他的吧")
                return@setOnClickListener

            }

            if (User.getUserId() > 0) {
                XPopup.Builder(context).
                        moveUpToKeyboard(false)
                        .asCustom(SpecSelectPopup(context, Constant.ACTION_ADD_TO_CART, item.commonId , null, null, null, 1, null, null, 0, 2, null))
                        .show()
            } else {
                Util.showLoginFragment(context);
            }
        }
        binding.root.setOnClickListener{
            Util.startFragment(GoodsDetailFragment.newInstance(item.commonId, 0))
        }
    }

    fun clear() {
        mData.clear()
    }
}