package com.ftofs.twant.widget;

import android.content.Context;
import androidx.annotation.NonNull;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.gzp.lib_common.constant.PopupType;
import com.ftofs.twant.entity.FilterCategoryGroup;
import com.ftofs.twant.entity.FilterCategoryItem;
import com.ftofs.twant.entity.FilterCategoryMeta;
import com.gzp.lib_common.base.callback.OnSelectedListener;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.lxj.xpopup.core.DrawerPopupView;

import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;

/**
 * 產品搜索結果列表的篩選彈窗
 * @author zwm
 */
public class GoodsFilterDrawerPopupView extends DrawerPopupView implements View.OnClickListener {
    Context context;
    List<FilterCategoryGroup> filterCategoryGroupList;
    EasyJSONObject currentFilter;
    OnSelectedListener onSelectedListener;

    String lowestPrice;
    String highestPrice;
    boolean giftEnable;
    boolean discountEnable;
    boolean videoEnable;
    boolean crossBorderEnable;
    boolean consultantEnable;
    private boolean disFreeShipping;
    // 當前選中的category的Id
    int categoryId;
    // 當前選中的category的索引
    int categoryIndex;

    List<TextView> tvCategoryButtonArr = new ArrayList<>();

    EditText etLowestPrice;
    EditText etHighestPrice;
    TextView btnFilterGift;
    TextView btnFilterDiscount;
    TextView btnReset;
    TextView btnOk;

    int twBlue;
    int twBlack;
    private TextView btnFilterShip;
    TextView btnFilterVideo;
    TextView btnFilterCrossBorder;
    TextView btnFilterConsultant;

    public GoodsFilterDrawerPopupView(@NonNull Context context, List<FilterCategoryGroup> filterCategoryGroupList,
                                      OnSelectedListener onSelectedListener) {
        super(context);
        this.context = context;
        this.filterCategoryGroupList = filterCategoryGroupList;
        this.onSelectedListener = onSelectedListener;

        twBlue = getResources().getColor(R.color.tw_blue, null);
        twBlack = getResources().getColor(R.color.tw_black, null);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.goods_filter_drawer_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        etLowestPrice = findViewById(R.id.et_lowest_price);
        etHighestPrice = findViewById(R.id.et_highest_price);

        btnFilterGift = findViewById(R.id.btn_filter_gift);
        btnFilterGift.setOnClickListener(this);

        btnFilterDiscount = findViewById(R.id.btn_filter_discount);
        btnFilterDiscount.setOnClickListener(this);

        btnReset = findViewById(R.id.btn_reset);
        btnReset.setOnClickListener(this);

        btnFilterShip = findViewById(R.id.btn_filter_free_shipping);
        btnFilterShip.setOnClickListener(this);

        btnFilterVideo = findViewById(R.id.btn_filter_video);
        btnFilterVideo.setOnClickListener(this);
        btnFilterCrossBorder = findViewById(R.id.btn_filter_cross_border);
        btnFilterCrossBorder.setOnClickListener(this);
        btnFilterConsultant = findViewById(R.id.btn_filter_consultant);
        btnFilterConsultant.setOnClickListener(this);

        btnOk = findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);

        findViewById(R.id.ll_popup_content_view).setOnClickListener(this);


        //通过设置topMargin，可以让Drawer弹窗进行局部阴影展示
//        ViewGroup.MarginLayoutParams params = (MarginLayoutParams) getPopupContentView().getLayoutParams();
//        params.topMargin = 450;
        reset();
    }

    @Override
    protected void onShow() {
        super.onShow();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
    }

    /**
     * 設置【促銷活動】是否開啟
     * @param button
     * @param enable
     */
    private void setActivityButton(TextView button, boolean enable) {
        SLog.info("button[%s]", button.toString());
        if (enable) {
            button.setBackgroundResource(R.drawable.promotion_activity_button_bg);
            button.setTextColor(twBlue);
        } else {
            button.setBackgroundResource(R.drawable.price_room_edit_text_bg);
            button.setTextColor(twBlack);
        }
    }

    private void reset() {
        lowestPrice = null;
        highestPrice = null;
        etLowestPrice.setText("");
        etHighestPrice.setText("");

        giftEnable = false;
        discountEnable = false;
        disFreeShipping = false;
        videoEnable = false;
        crossBorderEnable = false;
        consultantEnable = false;
        setActivityButton(btnFilterGift, false);
        setActivityButton(btnFilterDiscount, false);
        setActivityButton(btnFilterVideo, false);
        setActivityButton(btnFilterCrossBorder, false);
        setActivityButton(btnFilterConsultant, false);

        categoryId = -1;
        categoryIndex = -1;

        LinearLayout llCategoryGroupContainer = findViewById(R.id.ll_category_group_container);
        llCategoryGroupContainer.removeAllViews();
        tvCategoryButtonArr.clear();

        int index = 0;
        for (FilterCategoryGroup group : filterCategoryGroupList) {
            LinearLayout llFilterCategoryGroup = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.filter_category_group, llCategoryGroupContainer, false);
            FilterCategoryItem head = group.head;
            TextView tvGroupName = llFilterCategoryGroup.findViewById(R.id.tv_group_name);
            tvGroupName.setText(head.categoryName);

            int column = 0;
            List<FilterCategoryItem> list = group.list;
            LinearLayout llFilterCategoryRow = null;
            for (FilterCategoryItem item : list) {
                TextView tvItem;
                FilterCategoryMeta meta = new FilterCategoryMeta(index, item.categoryId);
                if (column % 3 == 0) {
                    llFilterCategoryRow = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.filter_category_row, llFilterCategoryGroup, false);
                    llFilterCategoryGroup.addView(llFilterCategoryRow);
                    tvItem = llFilterCategoryRow.findViewById(R.id.tv_item_1);
                    tvItem.setText(item.categoryName);
                    tvItem.setVisibility(VISIBLE);
                } else if (column % 3 == 1) {
                    tvItem = llFilterCategoryRow.findViewById(R.id.tv_item_2);
                    tvItem.setText(item.categoryName);
                    tvItem.setVisibility(VISIBLE);
                } else {
                    tvItem = llFilterCategoryRow.findViewById(R.id.tv_item_3);
                    tvItem.setText(item.categoryName);
                    tvItem.setVisibility(VISIBLE);
                }
                tvCategoryButtonArr.add(tvItem);

                tvItem.setTag(meta);

                tvItem.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FilterCategoryMeta meta = (FilterCategoryMeta) v.getTag();
                        SLog.info("categoryIndex[%d], categoryId[%d], meta.categoryIndex[%d], meta.categoryId[%d]",
                                categoryIndex, categoryId, meta.categoryIndex, meta.categoryId);
                        if (meta.categoryIndex == categoryIndex) {
                            return;
                        }

                        // 將前一個categoryButton變灰
                        if (categoryIndex != -1) {
                            TextView prevButton = tvCategoryButtonArr.get(categoryIndex);
                            setActivityButton(prevButton, false);
                        }

                        // 高亮現在選中的categoryButton
                        setActivityButton((TextView) v, true);
                        categoryIndex = meta.categoryIndex;
                        categoryId = meta.categoryId;
                    }
                });
                ++column;
                ++index;
            }
            llCategoryGroupContainer.addView(llFilterCategoryGroup);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_filter_gift) {
            setActivityButton(btnFilterGift, !giftEnable);
            giftEnable = !giftEnable;
        } else if (id == R.id.btn_filter_discount) {
            setActivityButton(btnFilterDiscount, !discountEnable);
            discountEnable = !discountEnable;
        } else if (id == R.id.btn_filter_free_shipping) {
            setActivityButton(btnFilterShip,!disFreeShipping);
            disFreeShipping = !disFreeShipping;
        } else if (id == R.id.btn_filter_video) {
            setActivityButton(btnFilterVideo, !videoEnable);
            videoEnable = !videoEnable;
        } else if (id == R.id.btn_filter_cross_border) {
            setActivityButton(btnFilterCrossBorder, !crossBorderEnable);
            crossBorderEnable = !crossBorderEnable;
            if (crossBorderEnable && consultantEnable) { // 互斥操作
                setActivityButton(btnFilterConsultant, false);
                consultantEnable = false;
            }
        } else if (id == R.id.btn_filter_consultant) {
            setActivityButton(btnFilterConsultant, !consultantEnable);
            consultantEnable = !consultantEnable;
            if (consultantEnable && crossBorderEnable) { // 互斥操作
                setActivityButton(btnFilterCrossBorder, false);
                crossBorderEnable = false;
            }
        } else if (id == R.id.btn_reset) {
            reset();
        } else if (id == R.id.btn_ok) {
            // 組裝篩選參數
            EasyJSONObject params = EasyJSONObject.generate();

            lowestPrice = etLowestPrice.getText().toString().trim();
            highestPrice = etHighestPrice.getText().toString().trim();

            try {
                if (StringUtil.isEmpty(lowestPrice) && !StringUtil.isEmpty(highestPrice)) {
                    ToastUtil.error(context, "請輸入最低價");
                    return;
                }

                if (!StringUtil.isEmpty(lowestPrice) && StringUtil.isEmpty(highestPrice)) {
                    ToastUtil.error(context, "請輸入最高價");
                    return;
                }

                if (!StringUtil.isEmpty(lowestPrice) && !StringUtil.isEmpty(highestPrice)) {
                    float lowest = Float.parseFloat(lowestPrice);
                    float highest = Float.parseFloat(highestPrice);

                    if (lowest < 0) {
                        ToastUtil.error(context, "價格不能小于零");
                        return;
                    }

                    if (Math.abs(highest - lowest) < Constant.STORE_DISTANCE_THRESHOLD) {
                        ToastUtil.error(context, "最低價不能與最高價相同");
                        return;
                    }

                    if (lowest > highest) {
                        ToastUtil.error(context, "最低價不能高于最高價");
                        return;
                    }

                    params.set("price", String.format("%s-%s", lowest, highest));
                }


                if (giftEnable) {
                    params.set("gift", 1);
                }
                if (discountEnable) {
                    params.set("promotion", 1);
                }
                if (videoEnable) {
                    params.set("hasVideo", 1);
                }
                if (crossBorderEnable) {
                    params.set("modal", Constant.GOODS_TYPE_CROSS_BORDER);
                } else if (consultantEnable) {
                    params.set("modal", Constant.GOODS_TYPE_CONSULT);
                }
                if (disFreeShipping) {
                    params.set("express", 1);
                }
                if (categoryId != -1) {
                    params.set("cat", categoryId);
                }
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }
            SLog.info("params[%s]", params.toString());

            onSelectedListener.onSelected(PopupType.DEFAULT, 0, params);
            dismiss();
        }
    }
}
