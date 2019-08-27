package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.PostItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 個人專頁-互動-我的評論Fragment
 * @author zwm
 */
public class MyCommentFragment extends BaseFragment implements View.OnClickListener {

    public static MyCommentFragment newInstance() {
        Bundle args = new Bundle();

        MyCommentFragment fragment = new MyCommentFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_comment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        loadData();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            pop();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }

    private void loadData() {
        try {
            String memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, null);
            if (StringUtil.isEmpty(memberName)) {
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate("memberName", memberName);
            String token = User.getToken();
            if (!StringUtil.isEmpty(token)) {
                params.set("token", token);
            }

            int year = 2019;
            int month = 8;

            params.set("year", year);
            params.set("month", month);

            final BasePopupView loadingPopup = new XPopup.Builder(_mActivity)
                    .asLoading(getString(R.string.text_loading))
                    .show();

            SLog.info("params[%s]", params);

            Api.postUI(Api.PATH_MEMBER_PAGE_ALL_COMMENT, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                    loadingPopup.dismiss();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    loadingPopup.dismiss();
                    try {
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }
                    } catch (Exception e) {
                        SLog.info("Error!%s", e.getMessage());
                    }
                }
            });
        } catch (Exception e) {

        }
    }
}


