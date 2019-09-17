package com.ftofs.twant.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.lxj.xpopup.core.DrawerPopupView;


/**
 * 貼文列表的篩選彈窗
 * @author zwm
 */
public class PostFilterDrawerPopupView extends DrawerPopupView implements View.OnClickListener {
    Context context;
    OnSelectedListener onSelectedListener;

    int twBlue;
    int twBlack;
    int selectedIndex;

    int btnIdArr[] = new int[] {
            R.id.btn_viewed_most, R.id.btn_focused_most,
            R.id.btn_thumbed_most, R.id.btn_commented_most,
            R.id.btn_deadline
    };


    /**
     * 貼文篩選描述
     * postView 查看最多 postFavor 最受關注 postLike 點讚最多 postReply 評論最多 expiresDate 截止日期
     */
    String filterDescArr[] = new String[] {
           "postView",  "postFavor", "postLike", "postReply", "expiresDate"
    };

    TextView btnArr[] = new TextView[5];

    public PostFilterDrawerPopupView(@NonNull Context context, OnSelectedListener onSelectedListener, int selectedIndex) {
        super(context);
        this.context = context;
        this.onSelectedListener = onSelectedListener;
        this.selectedIndex = selectedIndex;

        twBlue = getResources().getColor(R.color.tw_blue, null);
        twBlack = getResources().getColor(R.color.tw_black, null);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.post_filter_drawer_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.ll_popup_content_view).setOnClickListener(this);

        for (int i = 0; i < btnIdArr.length; i++) {
            TextView btn = findViewById(btnIdArr[i]);
            btnArr[i] = btn;
            btnArr[i].setTag(i);
            btnArr[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = (int) v.getTag();
                    String desc = filterDescArr[index];
                    onSelectedListener.onSelected(PopupType.POST_FILTER, index, desc);
                    dismiss();
                }
            });

            if (selectedIndex == i) {
                btn.setBackgroundResource(R.drawable.post_filter_button_bg_blue);
                btn.setTextColor(twBlue);
            } else {
                btn.setBackgroundResource(R.drawable.post_filter_button_bg_grey);
                btn.setTextColor(twBlack);
            }
        }

        //通过设置topMargin，可以让Drawer弹窗进行局部阴影展示
//        ViewGroup.MarginLayoutParams params = (MarginLayoutParams) getPopupContentView().getLayoutParams();
//        params.topMargin = 450;
    }

    @Override
    protected void onShow() {
        super.onShow();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
    }
}

