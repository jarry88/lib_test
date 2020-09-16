package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.Util;

/**
 * 添加常用語、常用版式Fragment
 * @author zwm
 */
public class AddCommonUsedSpeechFragment extends BaseFragment implements View.OnClickListener {
    /**
     * 常用語
     */
    public static final int TAB_COMMON_USED_SPEECH = 1;

    /**
     * 常用版式
     */
    public static final int TAB_COMMON_USED_TEMPLATE = 2;


    /**
     * 當前在哪個Tab
     */
    int currentTab = TAB_COMMON_USED_SPEECH;
    RelativeLayout rlCommonUsedSpeechContainer;
    LinearLayout llCommonUsedTemplateContainer;

    TextView btnCommonUsedSpeech;
    TextView btnCommonUsedTemplate;

    public static AddCommonUsedSpeechFragment newInstance() {
        Bundle args = new Bundle();

        AddCommonUsedSpeechFragment fragment = new AddCommonUsedSpeechFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_common_used_speech, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_save, this);

        btnCommonUsedSpeech = view.findViewById(R.id.btn_common_used_speech);
        btnCommonUsedTemplate = view.findViewById(R.id.btn_common_used_template);
        btnCommonUsedSpeech.setOnClickListener(this);
        btnCommonUsedTemplate.setOnClickListener(this);

        rlCommonUsedSpeechContainer = view.findViewById(R.id.rl_common_used_speech_container);
        llCommonUsedTemplateContainer = view.findViewById(R.id.ll_common_used_template_container);
        llCommonUsedTemplateContainer.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_save) {

        } else if (id == R.id.btn_common_used_speech) {
            if (currentTab == TAB_COMMON_USED_SPEECH) {
                return;
            }

            currentTab = TAB_COMMON_USED_SPEECH;
            rlCommonUsedSpeechContainer.setVisibility(View.VISIBLE);
            llCommonUsedTemplateContainer.setVisibility(View.GONE);

            btnCommonUsedSpeech.setTextColor(_mActivity.getColor(R.color.tw_blue));
            btnCommonUsedTemplate.setTextColor(_mActivity.getColor(R.color.tw_black));
        } else if (id == R.id.btn_common_used_template) {
            if (currentTab == TAB_COMMON_USED_TEMPLATE) {
                return;
            }

            currentTab = TAB_COMMON_USED_TEMPLATE;
            rlCommonUsedSpeechContainer.setVisibility(View.GONE);
            llCommonUsedTemplateContainer.setVisibility(View.VISIBLE);

            btnCommonUsedSpeech.setTextColor(_mActivity.getColor(R.color.tw_black));
            btnCommonUsedTemplate.setTextColor(_mActivity.getColor(R.color.tw_blue));
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}
