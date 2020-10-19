package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.lxj.xpopup.XPopup;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class DistributionEnrollmentFragment extends BaseFragment implements View.OnClickListener {
    boolean agreeContractChecked;  // 是否勾選了《分銷推廣協議》
    ImageView iconAgreeContract;

    int articleId; // 協議文章的Id
    String articleTitle; // 協議文章的標題
    String articleContent; // 協議文章的內容

    public static DistributionEnrollmentFragment newInstance(int articleId, String articleTitle, String articleContent) {
        Bundle args = new Bundle();

        DistributionEnrollmentFragment fragment = new DistributionEnrollmentFragment();
        fragment.setArguments(args);

        fragment.articleId = articleId;
        fragment.articleTitle = articleTitle;
        fragment.articleContent = articleContent;
        SLog.info("articleContent[%s]", articleContent);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_distribution_enrollment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iconAgreeContract = view.findViewById(R.id.icon_agree_contract);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_agree_contract, this);
        Util.setOnClickListener(view, R.id.btn_view_contract, this);
        Util.setOnClickListener(view, R.id.btn_ok, this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_ok) {
            if (!agreeContractChecked) {
                ToastUtil.error(_mActivity, "請先閱讀並同意《分銷推廣協議》");
                return;
            }

            String url = Api.PATH_DISTRIBUTOR_APPLY;
            Api.postUI(url, null, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtil.uploadAppLog(url, "", "", e.getMessage());
                    ToastUtil.showNetworkError(_mActivity, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    try {
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            LogUtil.uploadAppLog(url, "", responseStr, "");
                            return;
                        }

                        ToastUtil.success(_mActivity, "申請成功");
                        hideSoftInputPop();
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } else if (id == R.id.btn_agree_contract) {
            agreeContractChecked = !agreeContractChecked;
            iconAgreeContract.setImageResource(agreeContractChecked ? R.drawable.ic_baseline_check_box_24 : R.drawable.ic_baseline_check_box_outline_blank_24);
        } else if (id == R.id.btn_view_contract) {
            Util.startFragment(H5GameFragment.newInstance(articleTitle, articleContent, true));
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}
