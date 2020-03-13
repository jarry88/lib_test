package com.ftofs.twant.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.entity.Position;
import com.ftofs.twant.entity.SpecButtonData;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class PositionSelectPopup extends BottomPopupView implements View.OnClickListener {
    Context context;
    String currKeyword;
    String[] keyword=new String[3];
    TextView tvKeywordTitle;
    List<ListPopupItem> keywordList;
    OnSelectedListener onSelectedListener;

    public PositionSelectPopup(@Nullable Context context, String currKeyword, List<ListPopupItem> keywordList,OnSelectedListener onSelectedListener){
        super(context);
        this.context = context;
        this.currKeyword =currKeyword;
        this.keywordList = keywordList;
        this.onSelectedListener=onSelectedListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.position_keyword_select_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_ok).setOnClickListener(this);
        tvKeywordTitle=findViewById(R.id.tv_position_keyword_title);
        if(!StringUtil.isEmpty(currKeyword)){
            keyword=currKeyword.split(";");
        }
        SLog.info("keywordlenth[%s][%s]",currKeyword,keyword.toString());
        FlowLayout flPositionButtonContainer = findViewById(R.id.fl_position_button_container);
        //清空不存在于接口列表的字段
        for (int i=0;i<keyword.length;i++) {
            if (!StringUtil.isEmpty(keyword[i])) {
                boolean exist=false;
                for (ListPopupItem job : keywordList) {
                    if (job.title.equals(keyword[i])) {
                        exist=true;
                    }
                }
                if(!exist){
                    keyword[i]=null;
                }
            }
        }
        for (ListPopupItem job : keywordList) {
            TextView button = new TextView(context);
            job.data=false;

            button.setBackgroundResource(R.drawable.spec_item_unselected_bg);
            button.setTextColor(context.getColor(R.color.tw_black));

            button.setText(job.title);
            button.setTextSize(14);
            button.setPadding(Util.dip2px(context, 16), 0, Util.dip2px(context, 16), 0);
            for(int i=0;i<keyword.length&&i<3;i++){
                if(!StringUtil.isEmpty(keyword[i])&&keyword[i].equals(job.title)){
                    job.data=true;
                    button.setTextColor(context.getColor(R.color.tw_blue));
                    button.setBackgroundResource(R.drawable.spec_item_selected_bg);
                }
            }
            updateTitle();
            button.setTag(job);

            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    ListPopupItem currData = (ListPopupItem) view.getTag();
                    TextView currButton = (TextView) view;
                    if (Boolean.parseBoolean(currData.data.toString())) {
                        // 如果已經選中，重復點擊，不處理
                        SLog.info("如果已經選中，重復點擊，不處理");
                        currData.data = false;

                        // 當前選中的按鈕
                        currButton.setTextColor(context.getColor(R.color.tw_black));
                        currButton.setBackgroundResource(R.drawable.spec_item_unselected_bg);
                        for (int i = 0; i < keyword.length; i++) {
                            if(keyword[i]!=null&&keyword[i].equals(currData.title)){
                                keyword[i]=null;
                            }
                        }
                    }
                    else{
                        for (int i = 0; i < keyword.length; i++) {
                            if(keyword[i]==null){
                                keyword[i]=currData.title;
                                currData.data=true;
                                currButton.setTextColor(context.getColor(R.color.tw_blue));
                                currButton.setBackgroundResource(R.drawable.spec_item_selected_bg);;
                                break;
                            }
                        }
                    }
                    updateTitle();

                }
                });

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Util.dip2px(context, 32));
            button.setGravity(Gravity.CENTER);
            flPositionButtonContainer.addView(button, layoutParams);
            }

    }

    private void updateTitle() {
        int count=0;
        for(int i=0;i<keyword.length;i++){
            if(keyword[i]!=null){
                ++count;
            }
        }
        tvKeywordTitle.setText(String.format("職位關鍵詞（%d/3)",count));
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id) {
            case R.id.btn_close:
                dismiss();
                break;
            case R.id.btn_ok:
                StringBuilder str=new StringBuilder();
                for(int i=0;i<keyword.length;i++){
                    if(keyword[i]!=null){
                        str.append(keyword[i]);
                        str.append(";");
                    }
                }
                if(str.length()>0){
                    str.delete(str.length()-1,str.length());
                }
                onSelectedListener.onSelected(PopupType.SELECT_POST_KEYWORD,0,str.toString());
                dismiss();
                break;
                default:break;
        }
    }
    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.85f);
    }

}
