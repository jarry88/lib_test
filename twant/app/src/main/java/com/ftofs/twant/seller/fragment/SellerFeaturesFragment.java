package com.ftofs.twant.seller.fragment;

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
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.entity.ListPopupItem;
import com.gzp.lib_common.base.BaseFragment;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.seller.adapter.SellerFeaturesGoodsAdapter;
import com.ftofs.twant.seller.api.SellerApi;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ListPopup;
import com.lxj.xpopup.XPopup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 商家鎮店之寶添加編輯頁面 需求913
 * @author gzp
 */
public class SellerFeaturesFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    private List<Goods> goodsCommonList =new ArrayList<>();
    private SellerFeaturesGoodsAdapter adpter;


    void back() {
        hideSoftInputPop();
    }


    TextView tvTitle;


    void gotoGoodsList() {
//        Util.startFragment(SetFeatureListFragment.newInstance());
    }

    RecyclerView rvList;

    public static SellerFeaturesFragment newInstance() {
        return new SellerFeaturesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seller_edit_features_layout, container, false);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        rvList = (RecyclerView) view.findViewById(R.id.rv_features_goods_list);
        view.findViewById(R.id.icon_add_goods).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoGoodsList();
            }
        });
        view.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        view.findViewById(R.id.btn_menu).setOnClickListener((v)->{
                List<ListPopupItem> itemList = new ArrayList<>();
                itemList.add(new ListPopupItem(0, "商品管理", null));
                itemList.add(new ListPopupItem(1, "商品發布", null));
                itemList.add(new ListPopupItem(2, "商品規格", null));
                itemList.add(new ListPopupItem(3, "鎮店之寶", null));

                hideSoftInput();
                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new ListPopup(_mActivity, "請選擇操作",
                                PopupType.MENU, itemList, -1, this)).show();
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Util.setOnClickListener(view,R.id.icon_add_goods,this);
        initView();
    }

    private void initView() {
//        tvTitle.setText("鎮店之寶");
        TextView view = getView().findViewById(R.id.tv_title);
        view.setText("镇店之宝");
        getView().findViewById(R.id.icon_add_goods).setOnClickListener(this);
        rvList = getView().findViewById(R.id.rv_features_goods_list);
        adpter = new SellerFeaturesGoodsAdapter(_mActivity,0, R.layout.seller_features_good_layout, goodsCommonList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
        rvList.setLayoutManager(linearLayoutManager);
        rvList.setAdapter(adpter);
        adpter.setOnItemChildClickListener((adapter, view1, position) -> {
            int id =view1.getId();
            int commonId = ((Goods) adapter.getItem(position)).id;
            String commonName = ((Goods) adapter.getItem(position)).imageUrl;

            if (id == R.id.btn_view_all_sku) {
                //添加跳转商品详情页逻辑
                SLog.info("跳转至商品详情页");
                Util.startFragment(SellerGoodsSkuListFragment.newInstance(((Goods)adapter.getItem(position)).id));
            } else if (id == R.id.btn_more) {
//                Integer[] arrayList = new Integer[]{((Goods)adapter.getItem(position)).id};
                EasyJSONArray array = new EasyJSONArray();
                array.append(commonId);
                // 发送同步请求
                try {
                    String url = Api.REMOVE_FEATURES_GOODS;
                     EasyJSONObject params =EasyJSONObject.generate(
                             "token", User.getToken(),
                                "commonId", commonId);
//                    params.set("commonId[]", commonId);
                      SLog.info("params[%s]", params);
                      Api.postUI(url, params, new UICallback() {
                         @Override
                         public void onFailure(Call call, IOException e) {
                             LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                             ToastUtil.showNetworkError(_mActivity, e);
                         }

                         @Override
                         public void onResponse(Call call, String responseStr) throws IOException {
                             try {
                                 SLog.info("responseStr[%s]", responseStr);

                                 EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                                 if (ToastUtil.checkError(_mActivity, responseObj)) {
                                     LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                                     return;
                                 }
                                 goodsCommonList.remove(position);
                                 adapter.notifyDataSetChanged();
                             } catch (Exception e) {
                                 SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                             }
                         }
                      });
//                    uiCall.execute();
//                    uiCall.enqueue(new Callback<ApiResponse<Objects>>() {
//                        @Override
//                        public void onResponse(retrofit2.Call<ApiResponse<Objects>> call, Response<ApiResponse<Objects>> response) {
//
//                            // 主线程
//                            String responseStr = response.body().toString();
//                            SLog.info("result", responseStr);
//                        }
//
//                        @Override
//                        public void onFailure(retrofit2.Call<ApiResponse<Objects>> call, Throwable t) {
//
//                            // 主线程
//                            SLog.info("result", t.getMessage());
//                        }
//
//                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
// 发送异步请求
            }else if(id== R.id.ll_swipe_content){
                Util.startFragment(SellerGoodsDetailFragment.newInstance(commonId, commonName));
            }
        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        loadDate();
    }

    private void loadDate() {
        String url = SellerApi.PATH_SELLER_GOODS_FEATURES_LIST;
         EasyJSONObject params = EasyJSONObject.generate("token", User.getToken());
          SLog.info("params[%s]", params);
          Api.getUI(url, params, new UICallback() {
             @Override
             public void onFailure(Call call, IOException e) {
                 LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                 ToastUtil.showNetworkError(_mActivity, e);
             }

             @Override
             public void onResponse(Call call, String responseStr) throws IOException {
                 try {
                     SLog.info("responseStr[%s]", responseStr);

                     EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                     if (ToastUtil.checkError(_mActivity, responseObj)) {
                         LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.icon_add_goods) {
//            Util.startFragment(FeatureGoodSelectFragment.Companion.newInstance());
            Util.startFragment(SelectFeatureGoodsFragment.Companion.newInstance());
        }
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        SLog.info("onSelected, type[%s], id[%d], extra[%s]", type, id, extra);
        if (type == PopupType.MENU) {
            if (id == 1) {
                start(AddGoodsFragment.newInstance());
                hideSoftInputPop();
            } else if (id == 2) { // 商品規格
                hideSoftInputPop();
                start(SellerSpecFragment.newInstance());
            } else if (id == 0) {//商品管理
                try {
                    popTo(SellerGoodsListFragment.class,false);

                } catch (Exception e) {
                    start(SellerGoodsListFragment.newInstance());
                    hideSoftInputPop();
                }
            }
        }
    }
}

