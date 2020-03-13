package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.PostGoodsAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 發表想要帖產品搜索
 * @author zwm
 */
public class AddPostSearchGoodsFragment extends BaseFragment implements View.OnClickListener {
    String keyword;

    List<Goods> goodsList = new ArrayList<>();

    PostGoodsAdapter postGoodsAdapter;
    RecyclerView rvList;
    EditText etKeyword;

    public static AddPostSearchGoodsFragment newInstance(String keyword) {
        Bundle args = new Bundle();

        args.putString("keyword", keyword);
        AddPostSearchGoodsFragment fragment = new AddPostSearchGoodsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post_search_goods, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        keyword = args.getString("keyword");

        etKeyword = view.findViewById(R.id.et_keyword);
        etKeyword.setText(keyword);
        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                int keyCode = 0;
                if (event != null) {
                    event.getKeyCode();
                }

                SLog.info("actionId[%d], keyCode[%d]", actionId, keyCode);
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_UNSPECIFIED || keyCode == KeyEvent.KEYCODE_ENTER // 兼容倉頡中文輸入法
                ) {
                    doSearch(textView.getText().toString().trim());
                    return true;
                }

                return false;
            }
        });


        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_clear_all, this);

        rvList = view.findViewById(R.id.rv_list);
        postGoodsAdapter = new PostGoodsAdapter(R.layout.post_goods_item, goodsList);
        postGoodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_SELECT_POST_GOODS, goodsList.get(position));

                Fragment fragment = Util.getFragmentByLayer(_mActivity, 3);
                if (fragment != null) {
                    popTo(fragment.getClass(), false);
                }
            }
        });
        rvList.setLayoutManager(new GridLayoutManager(_mActivity, 3));
        rvList.setAdapter(postGoodsAdapter);

        doSearch(keyword);
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

        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_clear_all) {
            etKeyword.setText("");
        }
    }

    private void doSearch(String keyword) {
        String token = User.getToken();
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "keyword", keyword
        );

        Api.getUI(Api.PATH_POST_GOODS_SEARCH, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.isError(responseObj)) {
                    return;
                }

                try {
                    goodsList.clear();
                    EasyJSONArray goodsCommonVoList = responseObj.getSafeArray("datas.goodsCommonVoList");
                    for (Object object : goodsCommonVoList) {
                        EasyJSONObject goodsCommonVo = (EasyJSONObject) object;

                        Goods goods = new Goods();
                        goods.id = goodsCommonVo.getInt("commonId");
                        goods.imageUrl = goodsCommonVo.getSafeString("imageName");
                        goods.name = goodsCommonVo.getSafeString("goodsName");
                        goods.price = goodsCommonVo.getDouble("batchPrice0");

                        goodsList.add(goods);
                    }

                    postGoodsAdapter.setNewData(goodsList);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }

            }
        });
    }
}
