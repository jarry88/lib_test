package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.BlackDropdownMenu;
import com.lxj.xpopup.XPopup;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 店鋪首頁Fragment
 * @author zwm
 */
public class ShopHomeFragment extends BaseFragment implements View.OnClickListener {
    // 店鋪Id
    int shopId;

    TextView tvShopTitle;
    public static ShopHomeFragment newInstance(int shopId) {
        Bundle args = new Bundle();

        args.putInt("shopId", shopId);
        ShopHomeFragment fragment = new ShopHomeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_menu, this);

        tvShopTitle = view.findViewById(R.id.tv_shop_title);

        try {
            Bundle args = getArguments();
            shopId = args.getInt("shopId");
            SLog.info("shopId[%d]", shopId);

            // 獲取店鋪首頁信息
            String path = Api.PATH_SHOP_HOME + "/" + shopId;
            String token = User.getToken();
            EasyJSONObject params = EasyJSONObject.generate();
            if (StringUtil.isEmpty(token)) {
                params.set("token", token);
            }

            Api.postUI(path, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String responseStr = response.body().string();
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        String shopName = responseObj.getString("datas.storeInfo.storeName");
                        tvShopTitle.setText(shopName);


                    } catch (EasyJSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (EasyJSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_menu) {
            SLog.info("here");
            new XPopup.Builder(getContext())
                    .offsetX(-50)
                    .offsetY(-20)
//                        .popupPosition(PopupPosition.Right) //手动指定位置，有可能被遮盖
                    .hasShadowBg(false) // 去掉半透明背景
                    .atView(v)
                    .asCustom(new BlackDropdownMenu(_mActivity))
                    .show();
        }
    }
}
