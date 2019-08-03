package com.ftofs.twant.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CommonUsedSpeechAdapter;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.CommonUsedSpeech;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天常用語彈窗
 * @author zwm
 */
public class CommonUsedSpeechPopup extends BottomPopupView implements View.OnClickListener, OnSelectedListener {
    Context context;


    OnSelectedListener onSelectedListener;

    // 常用語
    List<CommonUsedSpeech> commonUsedSpeechList = new ArrayList<>();
    // 常用版式
    List<CommonUsedSpeech> commonUsedTemplateList = new ArrayList<>();


    public CommonUsedSpeechPopup(@NonNull Context context, OnSelectedListener onSelectedListener) {
        super(context);

        this.context = context;
        this.onSelectedListener = onSelectedListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.common_used_speech_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_dismiss).setOnClickListener(this);

        LinearLayout llCommonUsedSpeechContainer = findViewById(R.id.ll_common_used_speech_container);
        CommonUsedSpeechAdapter commonUsedSpeechAdapter = new CommonUsedSpeechAdapter(context, llCommonUsedSpeechContainer, R.layout.common_used_speech);

        commonUsedSpeechList.add(new CommonUsedSpeech(CommonUsedSpeech.DATA_TYPE_SPEECH, "親，有什麼可以幫到您？"));
        commonUsedSpeechList.add(new CommonUsedSpeech(CommonUsedSpeech.DATA_TYPE_SPEECH, "不客氣，這是我們應該做的"));
        commonUsedSpeechList.add(new CommonUsedSpeech(CommonUsedSpeech.DATA_TYPE_SPEECH, "很高興能為您服務～"));

        commonUsedSpeechAdapter.setItemClickListener(new ViewGroupAdapter.OnItemClickListener() {
            @Override
            public void onClick(ViewGroupAdapter adapter, View view, int position) {
                CommonUsedSpeech speech = commonUsedSpeechList.get(position);
                SLog.info("speech[%s]", speech.content);
                onSelectedListener.onSelected(PopupType.IM_CHAT_COMMON_USED_SPEECH, 0, speech);

                dismiss();
            }
        });
        commonUsedSpeechAdapter.setData(commonUsedSpeechList);

        LinearLayout llCommonUsedTemplateContainer = findViewById(R.id.ll_common_used_template_container);
        CommonUsedSpeechAdapter commonUsedTemplateAdapter = new CommonUsedSpeechAdapter(context, llCommonUsedTemplateContainer, R.layout.common_used_speech);
        commonUsedTemplateList.add(new CommonUsedSpeech(CommonUsedSpeech.DATA_TYPE_TEMPLATE, "我們的聯繫方式..."));
        commonUsedTemplateList.add(new CommonUsedSpeech(CommonUsedSpeech.DATA_TYPE_TEMPLATE, "售後服務..."));
        commonUsedTemplateList.add(new CommonUsedSpeech(CommonUsedSpeech.DATA_TYPE_TEMPLATE, "公司介紹..."));
        commonUsedTemplateList.add(new CommonUsedSpeech(CommonUsedSpeech.DATA_TYPE_TEMPLATE, "快遞收費制度..."));
        commonUsedTemplateList.add(new CommonUsedSpeech(CommonUsedSpeech.DATA_TYPE_TEMPLATE, "促銷活動介紹..."));

        commonUsedTemplateAdapter.setItemClickListener(new ViewGroupAdapter.OnItemClickListener() {
            @Override
            public void onClick(ViewGroupAdapter adapter, View view, int position) {
                CommonUsedSpeech speech = commonUsedTemplateList.get(position);
                SLog.info("speech[%s]", speech.content);
                onSelectedListener.onSelected(PopupType.IM_CHAT_COMMON_USED_SPEECH, 0, speech);

                dismiss();
            }
        });

        commonUsedTemplateAdapter.setData(commonUsedTemplateList);
    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
    }

    //完全消失执行
    @Override
    protected void onDismiss() {

    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.85f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_dismiss) {
            dismiss();
        }
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        SLog.info("onSelected, id[%d], extra[%s]", id, extra);

        onSelectedListener.onSelected(PopupType.IM_CHAT_COMMON_USED_SPEECH, id, extra);
        dismiss();
    }
}
