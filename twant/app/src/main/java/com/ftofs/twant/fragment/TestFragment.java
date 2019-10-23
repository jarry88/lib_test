package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.interfaces.CommonCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.StoreFilterPopup;
import com.ftofs.twant.widget.WalletPayPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.interfaces.XPopupCallback;

/**
 * 測試用Fragment
 * @author zwm
 */
public class TestFragment extends BaseFragment implements View.OnClickListener, CommonCallback {
    int twBlue;
    int twBlack;

    int currSelId;

    StoreFilterPopup popup;

    boolean followStatus;
    TextView tvFollow;
    TextView tvGeneral;
    ImageView iconGeneral;
    TextView tvLocation;
    ImageView iconLocation;
    TextView tvBizCircle;
    ImageView iconBizCircle;

    RelativeLayout btnGeneral;
    RelativeLayout btnLocation;
    RelativeLayout btnBizCircle;

    LinearLayout llFilterBar;

    public static TestFragment newInstance() {
        Bundle args = new Bundle();

        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        twBlue = getResources().getColor(R.color.tw_blue, null);
        twBlack = getResources().getColor(R.color.tw_black, null);

        llFilterBar = view.findViewById(R.id.ll_filter_bar);

        tvFollow = view.findViewById(R.id.tv_follow);
        tvGeneral = view.findViewById(R.id.tv_general);
        iconGeneral = view.findViewById(R.id.icon_general);
        tvLocation = view.findViewById(R.id.tv_location);
        iconLocation = view.findViewById(R.id.icon_location);
        tvBizCircle = view.findViewById(R.id.tv_biz_circle);
        iconBizCircle = view.findViewById(R.id.icon_biz_circle);

        Util.setOnClickListener(view, R.id.btn_test, this);
        Util.setOnClickListener(view, R.id.btn_test2, this);
        Util.setOnClickListener(view, R.id.btn_test3, this);

        Util.setOnClickListener(view, R.id.btn_follow, this);

        btnGeneral = view.findViewById(R.id.btn_general);
        btnGeneral.setOnClickListener(this);
        btnLocation = view.findViewById(R.id.btn_location);
        btnLocation.setOnClickListener(this);
        btnBizCircle = view.findViewById(R.id.btn_biz_circle);
        btnBizCircle.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_test) {
            new XPopup.Builder(_mActivity)
                    .popupAnimation(PopupAnimation.TranslateFromBottom)
                    .hasStatusBarShadow(true)
                    .asCustom(new WalletPayPopup(_mActivity, 0, 25.13f, this))
                    .show();
        } else if (id == R.id.btn_test2) {

        } else if (id == R.id.btn_test3) {

        } else if (id == R.id.btn_follow) {
            followStatus = !followStatus;
            if (followStatus) {
                tvFollow.setTextColor(twBlue);
            } else {
                tvFollow.setTextColor(twBlack);
            }
        } else if (id == R.id.btn_general) {
            togglePopup(view, id);
        } else if (id == R.id.btn_location) {
            togglePopup(view, id);
        } else if (id == R.id.btn_biz_circle) {
            togglePopup(view, id);
        }
    }

    private void togglePopup(View view, int id) {
        currSelId = id;
        if (popup == null) {
            popup = (StoreFilterPopup) new XPopup.Builder(_mActivity)
                    .atView(view)
                    .setPopupCallback(new XPopupCallback() {
                        @Override
                        public void onShow() {
                            SLog.info("显示了");
                            if (currSelId == R.id.btn_general) {
                                btnGeneral.setBackgroundResource(R.drawable.store_filter_btn_sel_bg);
                                tvGeneral.setTextColor(twBlue);
                                iconGeneral.setImageResource(R.drawable.icon_store_filter_expand_blue);
                            } else if (currSelId == R.id.btn_location) {
                                btnLocation.setBackgroundResource(R.drawable.store_filter_btn_sel_bg);
                                tvLocation.setTextColor(twBlue);
                                iconLocation.setImageResource(R.drawable.icon_store_filter_expand_blue);
                            } else if (currSelId == R.id.btn_biz_circle) {
                                btnBizCircle.setBackgroundResource(R.drawable.store_filter_btn_sel_bg);
                                tvBizCircle.setTextColor(twBlue);
                                iconBizCircle.setImageResource(R.drawable.icon_store_filter_expand_blue);
                            }
                        }
                        @Override
                        public void onDismiss() {
                            SLog.info("关闭了");

                            btnGeneral.setBackground(null);
                            tvGeneral.setTextColor(twBlack);
                            iconGeneral.setImageResource(R.drawable.icon_store_filter_expand_black);

                            btnLocation.setBackground(null);
                            tvLocation.setTextColor(twBlack);
                            iconLocation.setImageResource(R.drawable.icon_store_filter_expand_black);

                            btnBizCircle.setBackground(null);
                            tvBizCircle.setTextColor(twBlack);
                            iconBizCircle.setImageResource(R.drawable.icon_store_filter_expand_black);
                        }
                    })
                    .asCustom(new StoreFilterPopup(_mActivity, 1, 1, null, null));
        }
        SLog.info("popup is shown[%s]", popup.isShown());

        popup.toggle();
    }


    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }

    @Override
    public String onSuccess(@Nullable String data) {
        SLog.info("onSuccess");
        return null;
    }

    @Override
    public String onFailure(@Nullable String data) {
        SLog.info("onFailure");
        return null;
    }
}
