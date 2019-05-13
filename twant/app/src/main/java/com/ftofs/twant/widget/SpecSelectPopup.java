package com.ftofs.twant.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.Spec;
import com.ftofs.twant.entity.SpecValue;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.nex3z.flowlayout.FlowLayout;

import java.util.List;

/**
 * 規格選擇框
 * @author zwm
 */
public class SpecSelectPopup extends BottomPopupView {
    Context context;
    List<Spec> specList;
    int selectedIndex;  // 當前選中的索引
    ImageView skuImage;

    public SpecSelectPopup(@NonNull Context context, List<Spec> specList) {
        super(context);

        this.context = context;
        this.specList = specList;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.spec_select_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        skuImage = findViewById(R.id.sku_image);

        SLog.info("specList.size[%d]", specList.size());
        if (specList.size() < 1) {
            return;
        }

        // 找到第1個規格的照片
        Spec firstSpec = specList.get(0);
        String firstImageSrc = firstSpec.specValueList.get(0).imageSrc;
        SLog.info("firstImageSrc[%s]", firstImageSrc);
        Glide.with(this).load(firstImageSrc).into(skuImage);

        LinearLayout llSpecContainer = findViewById(R.id.ll_spec_container);
        for (Spec spec : specList) {
            LinearLayout llSpec = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_spec,
                    null, false);
            TextView tvSpecName = llSpec.findViewById(R.id.tv_spec_name);
            tvSpecName.setText(spec.specName);

            int index = 0;
            FlowLayout flSpecButtonContainer = llSpec.findViewById(R.id.fl_spec_button_container);
            for (SpecValue specValue : spec.specValueList) {
                TextView button = new TextView(context);
                if (index == selectedIndex) {
                    button.setBackgroundResource(R.drawable.spec_item_selected_bg);
                } else {
                    button.setBackgroundResource(R.drawable.spec_item_unselected_bg);
                }

                button.setText(specValue.specValueName);
                button.setTextSize(14);

                int color = getResources().getColor(R.color.tw_black, null);
                button.setTextColor(color);
                button.setPadding(Util.dip2px(context, 16), Util.dip2px(context, 6),
                        Util.dip2px(context, 16), Util.dip2px(context, 6));
                flSpecButtonContainer.addView(button);
            }
            llSpecContainer.addView(llSpec);
        }

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
}
