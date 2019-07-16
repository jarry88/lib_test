package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.nex3z.flowlayout.FlowLayout;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 店鋪內商品搜索頁面
 * @author zwm
 */
public class ShopSearchFragment extends BaseFragment implements View.OnClickListener {
    int storeId;
    String extraData;

    EditText etKeyword;
    FlowLayout flNewGoodsContainer;
    FlowLayout flHotGoodsContainer;

    /**
     * 新建新實例
     * @param storeId
     * @param extraData 如果extraData為空，會從網絡從重新加載extraData
     * @return
     */
    public static ShopSearchFragment newInstance(int storeId, String extraData) {
        Bundle args = new Bundle();

        args.putInt("storeId", storeId);
        args.putString("extraData", extraData);
        ShopSearchFragment fragment = new ShopSearchFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_search, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        storeId = args.getInt("storeId");
        extraData = args.getString("extraData");

        etKeyword = view.findViewById(R.id.et_keyword);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_search, this);
        Util.setOnClickListener(view, R.id.btn_clear_all, this);

        flNewGoodsContainer = view.findViewById(R.id.fl_new_goods_container);
        flHotGoodsContainer = view.findViewById(R.id.fl_hot_goods_container);

        if (!StringUtil.isEmpty(extraData)) {
            populateData(extraData);
        } else {
            loadShopSearchData();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                pop();
                break;
            case R.id.btn_search:
                // 顯示搜索結果頁面
                String keyword = etKeyword.getText().toString().trim();
                if (keyword.length() < 1) {
                    ToastUtil.error(_mActivity, "請輸入搜索關鍵字");
                    return;
                }
                MainFragment mainFragment = MainFragment.getInstance();
                mainFragment.start(ShopCommodityFragment.newInstance(EasyJSONObject.generate("storeId", storeId, "keyword", keyword).toString()));
                break;
            case R.id.btn_clear_all:
                etKeyword.setText("");
                break;
            default:
                break;
        }
    }

    /**
     * 填充數據
     * @param data JSON字符串格式
     */
    private void populateData(String data) {
        EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(data);
        if (ToastUtil.checkError(_mActivity, responseObj)) {
            return;
        }

        try {
            EasyJSONArray newGoodsVoList = responseObj.getArray("datas.newGoodsVoList");
            for (Object object : newGoodsVoList) {
                EasyJSONObject newGoodsVo = (EasyJSONObject) object;

                TextView textView = new TextView(_mActivity);

                textView.setPadding(Util.dip2px(_mActivity, 16), Util.dip2px(_mActivity, 6),
                        Util.dip2px(_mActivity, 16), Util.dip2px(_mActivity, 6));
                textView.setTag(newGoodsVo.getInt("commonId"));
                textView.setText(newGoodsVo.getString("goodsName"));
                textView.setTextSize(14);
                textView.setTextColor(getResources().getColor(R.color.tw_black, null));
                textView.setBackgroundResource(R.drawable.search_item_bg);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView tv = (TextView) v;
                        String goodsName = tv.getText().toString();
                        int commonId = (int) tv.getTag();
                        redirectToGoodsDetailFragment(commonId);
                    }
                });

                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                flNewGoodsContainer.addView(textView, layoutParams);
            }

            EasyJSONArray commendGoodsVoList = responseObj.getArray("datas.commendGoodsVoList");
            for (Object object : commendGoodsVoList) {
                EasyJSONObject commendGoodsVo = (EasyJSONObject) object;

                TextView textView = new TextView(_mActivity);

                textView.setPadding(Util.dip2px(_mActivity, 16), Util.dip2px(_mActivity, 6),
                        Util.dip2px(_mActivity, 16), Util.dip2px(_mActivity, 6));
                textView.setTag(commendGoodsVo.getInt("commonId"));
                textView.setText(commendGoodsVo.getString("goodsName"));
                textView.setTextSize(14);
                textView.setTextColor(getResources().getColor(R.color.tw_black, null));
                textView.setBackgroundResource(R.drawable.search_item_bg);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView tv = (TextView) v;
                        String goodsName = tv.getText().toString();
                        int commonId = (int) tv.getTag();
                        redirectToGoodsDetailFragment(commonId);
                    }
                });

                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                flHotGoodsContainer.addView(textView, layoutParams);
            }

        } catch (EasyJSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }

    /**
     * 中轉到商品詳情頁面
     * @param commonId
     */
    private void redirectToGoodsDetailFragment(int commonId) {
        MainFragment mainFragment = MainFragment.getInstance();
        mainFragment.start(GoodsDetailFragment.newInstance(commonId));
    }

    private void loadShopSearchData() {
        EasyJSONObject params = EasyJSONObject.generate(
                "storeId", storeId);

        Api.getUI(Api.PATH_STORE_CATEGORY, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                populateData(responseStr);
            }
        });
    }
}
