package com.ftofs.twant.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import android.renderscript.ScriptC;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.QRCode;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.LockableNestedScrollView;
import com.ftofs.twant.widget.TwLoadingPopup;
import com.ftofs.twant.widget.TwQRCodePopup;

import com.ftofs.twant.entity.StoreMapInfo;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.AmapPopup;

import com.lxj.xpopup.XPopup;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;

/**
 * @author gzp
 */
public class StoreAboutFragment extends ScrollableBaseFragment implements View.OnClickListener {
    int storeId;

    LockableNestedScrollView svContainer;
    TextView tvIntroduction;

    // TabFragment是否可以滚动
    boolean scrollable = false;

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }


    public static StoreAboutFragment newInstance(int storeId) {
        StoreAboutFragment fragment = new StoreAboutFragment();
        fragment.setStoreId(storeId);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_about, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        svContainer = view.findViewById(R.id.sv_container);
        svContainer.setScrollable(scrollable);
        tvIntroduction = view.findViewById(R.id.tv_introduction);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }

    @Override
    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
        if (svContainer != null) {
            svContainer.setScrollable(scrollable);
        }
    }

    public void setIntroduction(String introduction) {
        tvIntroduction.setText(introduction);
    }
}
