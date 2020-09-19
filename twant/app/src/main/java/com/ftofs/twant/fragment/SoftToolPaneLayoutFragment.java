package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ftofs.twant.R;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SoftToolPaneLayout;

import org.jetbrains.annotations.NotNull;

/**
 * SoftToolPaneLayout實驗Fragment
 * @author zwm
 */
public class SoftToolPaneLayoutFragment extends BaseFragment implements View.OnClickListener {
    LinearLayout llToolbar;
    RelativeLayout rlToolPane;
    EditText etContent;

    SoftToolPaneLayout silContainer;

    public static SoftToolPaneLayoutFragment newInstance() {
        Bundle args = new Bundle();
        SoftToolPaneLayoutFragment fragment = new SoftToolPaneLayoutFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_soft_tool_pane_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        llToolbar = view.findViewById(R.id.ll_toolbar);
        rlToolPane = view.findViewById(R.id.rl_tool_pane);
        etContent = view.findViewById(R.id.et_content);

        silContainer = view.findViewById(R.id.sil_container);

        Util.setOnClickListener(view, R.id.btn_test, this);
        Util.setOnClickListener(view, R.id.btn_emoji, this);
        Util.setOnClickListener(view, R.id.btn_soft_input, this);

        silContainer.setStatusChangedListener(new SoftToolPaneLayout.OnStatusChangedListener() {
            @Override
            public void onStatusChanged(int oldStatus, int newStatus) {
                SLog.info("oldStatus[%d], newStatus[%d]", oldStatus, newStatus);
                if (oldStatus == SoftToolPaneLayout.STATUS_NONE_SHOWN &&
                        newStatus == SoftToolPaneLayout.STATUS_SOFT_INPUT_SHOWN) {
                    llToolbar.setVisibility(View.VISIBLE);
                }

                if (newStatus == SoftToolPaneLayout.STATUS_NONE_SHOWN) {
                    llToolbar.setVisibility(View.GONE);
                }
            }
        });
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

        if (id == R.id.btn_test) {
            int softInputHeight = silContainer.getSoftInputHeight();
            SLog.info("softInputHeight[%d]", softInputHeight);
        } else if (id == R.id.btn_emoji) {
            silContainer.showToolPane();
        } else if (id == R.id.btn_soft_input) {
            silContainer.showSoftInput(etContent);
        }
    }
}



