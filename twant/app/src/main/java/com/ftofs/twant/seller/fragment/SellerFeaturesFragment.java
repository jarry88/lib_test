package com.ftofs.twant.seller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.seller.adapter.SellerFeaturesGoodsAdapter;
import com.ftofs.twant.seller.api.SellerApi;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ScaledButton;
import com.ftofs.twant.widget.SimpleTabManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 商家鎮店之寶添加編輯頁面 需求913
 * @author gzp
 */
public class SellerFeaturesFragment extends BaseFragment {
    private Unbinder unbinder;
    private List<Goods> goodsCommonList =new ArrayList<>();
    private SellerFeaturesGoodsAdapter adpter;

    @OnClick(R.id.btn_back)
    void back() {
        hideSoftInputPop();
    }

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @OnClick(R.id.icon_add_goods)
    void gotoGoodsList() {
        Util.startFragment(SetFeatureListFragment.newInstance());
    }
    @BindView(R.id.rv_features_goods_list)
    RecyclerView rvList;

    public static SellerFeaturesFragment newInstance() {
        return new SellerFeaturesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seller_edit_features_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        tvTitle.setText("鎮店之寶");

        adpter = new SellerFeaturesGoodsAdapter(_mActivity,0, R.layout.seller_features_good_layout, goodsCommonList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
        rvList.setLayoutManager(linearLayoutManager);
        rvList.setAdapter(adpter);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        loadDate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    private void loadDate() {
         EasyJSONObject params = EasyJSONObject.generate("token", User.getToken());
          SLog.info("params[%s]", params);
          Api.getUI(SellerApi.PATH_SELLER_GOODS_FEATURES_LIST, params, new UICallback() {
             @Override
             public void onFailure(Call call, IOException e) {
                 ToastUtil.showNetworkError(_mActivity, e);
             }

             @Override
             public void onResponse(Call call, String responseStr) throws IOException {
                 try {
                     SLog.info("responseStr[%s]", responseStr);

                     EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                     if (ToastUtil.checkError(_mActivity, responseObj)) {
                         return;
                     }
                     updateView(responseObj);
                 } catch (Exception e) {
                     SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                 }
             }
          });
    }

    private void updateView(EasyJSONObject responseObj) throws Exception{
        EasyJSONArray list = responseObj.getSafeArray("datas.goodsCommonList");
        goodsCommonList.clear();
        for (Object object : list) {
            goodsCommonList.add(Goods.parse((EasyJSONObject) object));
        }
        adpter.setNewData(goodsCommonList);
    }

    @Override
    public boolean onBackPressedSupport() {
    SLog.info("onBackPressedSupport");
    hideSoftInputPop();
    return true;
    }
}

