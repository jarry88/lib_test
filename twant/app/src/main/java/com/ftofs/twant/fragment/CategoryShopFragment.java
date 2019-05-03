package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.util.ToastUtil;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 店鋪分類Fragment
 * @author zwm
 */
public class CategoryShopFragment extends BaseFragment implements View.OnClickListener {
    RecyclerView rvCategoryMenu;
    RecyclerView rvShopList;

    public static CategoryShopFragment newInstance() {
        Bundle args = new Bundle();

        CategoryShopFragment fragment = new CategoryShopFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_shop, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCategoryMenu = view.findViewById(R.id.rv_category_menu);
        rvShopList = view.findViewById(R.id.rv_shop_list);


    }

    @Override
    public void onClick(View v) {

    }

    private void loadCategoryMenuData() {
        Api.getUI(Api.PATH_SHOP_CATEGORY, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseStr = response.body().string();
                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }


            }
        });
    }
}
