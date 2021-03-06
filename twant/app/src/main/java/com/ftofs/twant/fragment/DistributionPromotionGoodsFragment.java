package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.lib_net.model.SellerGoodsItem;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.DistributionMemberAdapter;
import com.ftofs.twant.adapter.DistributionOrderAdapter;
import com.ftofs.twant.adapter.DistributionPromotionGoodsAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.CustomAction;
import com.ftofs.twant.entity.CustomActionData;
import com.ftofs.twant.entity.DistributionMember;
import com.ftofs.twant.entity.DistributionOrderItem;
import com.ftofs.twant.entity.DistributionPromotionGoods;
import com.ftofs.twant.entity.DistributionWithdrawRecord;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SharePopup;
import com.ftofs.twant.widget.lmz.LmzNestedScrollingBaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.lxj.xpopup.XPopup;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import retrofit2.http.POST;

public class DistributionPromotionGoodsFragment extends LmzNestedScrollingBaseFragment implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    DistributionPromotionGoodsAdapter adapter;
    List<DistributionPromotionGoods> distributionPromotionGoodsList = new ArrayList<>();

    // 當前要加載第幾頁(從1開始）
    int currPage = 0;
    boolean hasMore;

    double cnyExchangeRate;
    int selectedCount = 0; // 選中的商品個數
    SimpleCallback simpleCallback;

    public static DistributionPromotionGoodsFragment newInstance(SimpleCallback simpleCallback) {
        DistributionPromotionGoodsFragment fragment = new DistributionPromotionGoodsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.simpleCallback = simpleCallback;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_distribution_promotion_goods, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvList = view.findViewById(R.id.rv_list);
        SLog.info("rvList[%s]", rvList);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new DistributionPromotionGoodsAdapter(_mActivity, distributionPromotionGoodsList);
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(this, rvList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DistributionPromotionGoods promotionGoods = distributionPromotionGoodsList.get(position);
                promotionGoods.selected = !promotionGoods.selected;
                if (promotionGoods.selected) {
                    selectedCount++;
                } else {
                    selectedCount--;
                }

                List<Integer> selectedCommonIdList = new ArrayList<>();
                for (DistributionPromotionGoods item : distributionPromotionGoodsList) {
                    if (item.selected) {
                        selectedCommonIdList.add(item.commonId);
                    }
                }

                if (simpleCallback != null) {
                    CustomActionData customActionData = new CustomActionData();
                    customActionData.action = CustomAction.CUSTOM_ACTION_SHARE_MULTIPLE_GOODS.ordinal();
                    customActionData.data = selectedCommonIdList;
                    simpleCallback.onSimpleCall(customActionData);
                }

                adapter.notifyItemChanged(position);
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                if (id == R.id.btn_share) {
                    DistributionPromotionGoods goods = distributionPromotionGoodsList.get(position);
                    double cnyPrice = 0;
                    if (cnyExchangeRate > 0) {
                        cnyPrice = goods.batchPrice2 / cnyExchangeRate;
                    }

                    EasyJSONObject posterData = EasyJSONObject.generate(
                            "commonId", goods.commonId,
                            "goodsName", goods.goodsName,
                            "goodsImageUrl", goods.imageName,
                            "mopPrice", goods.batchPrice2,
                            "cnyPrice", cnyPrice
                    );
                    // Util.startFragment(GeneratePosterFragment.newInstance(Constant.POSTER_TYPE_GOODS, posterData));

                    new XPopup.Builder(_mActivity)
                            // 如果不加这个，评论弹窗会移动到软键盘上面
                            .moveUpToKeyboard(false)
                            .asCustom(new SharePopup(_mActivity, SharePopup.generateGoodsShareLink(goods.commonId, 0), goods.goodsName,
                                    "", goods.imageName, EasyJSONObject.generate("shareType", SharePopup.SHARE_TYPE_GOODS,
                                    "commonId", goods.commonId, "goodsName", goods.goodsName,
                                    "goodsImage", goods.imageName, "goodsPrice", goods.batchPrice2,
                                    "cnyPrice", cnyPrice, "goodsModel", Constant.GOODS_TYPE_CROSS_BORDER)))
                            .show();
                }
            }
        });
        rvList.setAdapter(adapter);

        loadData(currPage + 1);
    }


    private void loadData(int page) {
        String url = Api.PATH_MEMBER_MARKETING_GOODS;
        EasyJSONObject params = EasyJSONObject.generate(
                "page", page
        );
        SLog.info("url[%s], params[%s]", url, params.toString());
        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
                adapter.loadMoreFail();
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        adapter.loadMoreFail();
                        return;
                    }

                    if (cnyExchangeRate < Constant.DOUBLE_ZERO_THRESHOLD) {
                        cnyExchangeRate = responseObj.optDouble("datas.cnyExchangeRate");
                    }

                    hasMore = responseObj.getBoolean("datas.pageEntity.hasMore");
                    SLog.info("hasMore[%s]", hasMore);
                    if (!hasMore) {
                        adapter.loadMoreEnd();
                        adapter.setEnableLoadMore(false);
                    }

                    if (page == 1) {
                        distributionPromotionGoodsList.clear();
                    }
                    EasyJSONArray goodsList = responseObj.getArray("datas.goodsList");
                    for (Object object : goodsList) {
                        DistributionPromotionGoods distributionPromotionGoods = (DistributionPromotionGoods) EasyJSONBase.jsonDecode(DistributionPromotionGoods.class, object.toString());
                        distributionPromotionGoods.itemType = Constant.ITEM_TYPE_NORMAL;
                        distributionPromotionGoodsList.add(distributionPromotionGoods);
                    }

                    if (page == 1 && !hasMore && distributionPromotionGoodsList.size() == 0) {
                        distributionPromotionGoodsList.add(new DistributionPromotionGoods(Constant.ITEM_TYPE_NO_DATA));
                    }

                    adapter.loadMoreComplete();
                    adapter.setNewData(distributionPromotionGoodsList);
                    currPage++;
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    /**
     * 上滑加載更多
     */
    @Override
    public void onLoadMoreRequested() {
        SLog.info("onLoadMoreRequested");

        if (!hasMore) {
            adapter.setEnableLoadMore(false);
            return;
        }
        loadData(currPage + 1);
    }

    @Override
    public void onClick(View v) {

    }
}
