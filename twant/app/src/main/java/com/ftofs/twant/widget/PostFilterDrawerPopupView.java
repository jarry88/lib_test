package com.ftofs.twant.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.gzp.lib_common.constant.PopupType;
import com.gzp.lib_common.base.callback.OnSelectedListener;
import com.lxj.xpopup.core.DrawerPopupView;


/**
 * 想要帖列表的篩選彈窗
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
     * 想要帖篩選描述
     * postView 查看數量 postFavor 最受關注 postLike 讚想數量 postReply 說說數量 expiresDate 截止日期
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

                    TextView currBtn = (TextView) v;
                    if (selectedIndex == index) { // 点击原先选中的按钮，取消选择
                        currBtn.setBackgroundResource(R.drawable.post_filter_button_bg_grey);
                        currBtn.setTextColor(twBlack);
                        selectedIndex = -1;
                    } else {
                        // 选中当前的按钮
                        currBtn.setBackgroundResource(R.drawable.post_filter_button_bg_blue);
                        currBtn.setTextColor(twBlue);

                        if (selectedIndex != -1) {
                            // 如果之前有选中的按钮，取消之前的选择
                            TextView prevBtn = btnArr[selectedIndex];

                            prevBtn.setBackgroundResource(R.drawable.post_filter_button_bg_grey);
                            prevBtn.setTextColor(twBlack);
                        }

                        selectedIndex = index;
                    }


                    String desc = "";
                    if (selectedIndex != -1) {
                        desc = filterDescArr[selectedIndex];
                    }
                    onSelectedListener.onSelected(PopupType.POST_FILTER, index, desc);
                    dismiss();
                }
            });
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

