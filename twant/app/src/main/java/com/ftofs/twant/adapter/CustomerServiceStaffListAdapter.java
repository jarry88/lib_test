package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.CustomerServiceStaff;
import com.ftofs.twant.fragment.ShopCustomerServiceFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class CustomerServiceStaffListAdapter extends BaseQuickAdapter<CustomerServiceStaff, BaseViewHolder>
                                        implements Animation.AnimationListener {
    /**
     * Adapter所在的Fragment
     */
    ShopCustomerServiceFragment shopCustomerServiceFragment;
    Animation animation;
    TextView animatingTextView;
    public CustomerServiceStaffListAdapter(ShopCustomerServiceFragment shopCustomerServiceFragment, int layoutResId, @Nullable List<CustomerServiceStaff> data) {
        super(layoutResId, data);

        this.shopCustomerServiceFragment = shopCustomerServiceFragment;
    }

    @Override
    protected void convert(BaseViewHolder helper, CustomerServiceStaff item) {
        if (animation == null) {
            animation = AnimationUtils.loadAnimation(mContext, R.anim.welcome_message);
            animation.setAnimationListener(this);
        }

        ImageView imgStaffAvatar = helper.getView(R.id.img_staff_avatar);

        if (StringUtil.useDefaultAvatar(item.avatar)) {
            Glide.with(mContext).load(R.drawable.grey_default_avatar).centerCrop().into(imgStaffAvatar);
        } else {
            Glide.with(mContext).load(item.avatar).centerCrop().into(imgStaffAvatar);
        }


        helper.setText(R.id.tv_staff_name, item.staffName);

        TextView tvWelcomeMessage = helper.getView(R.id.tv_welcome_message);
        tvWelcomeMessage.setText(item.welcomeMessage);

        if (item.showWelcomeMessageAnimation) {
            tvWelcomeMessage.setVisibility(View.INVISIBLE);
            animatingTextView = tvWelcomeMessage;
            tvWelcomeMessage.startAnimation(animation);
        } else {
            tvWelcomeMessage.setVisibility(View.INVISIBLE);
        }


        int itemCount = getItemCount();
        int position = helper.getAdapterPosition();
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) helper.itemView.getLayoutParams();
        if (position == itemCount - 1 || (itemCount % 2 == 0 && position == itemCount - 2)) { // 最后一行，設置大一點的bottomMargin
            layoutParams.bottomMargin = (int) mContext.getResources().getDimension(R.dimen.bottom_toolbar_max_height);
        } else {
            layoutParams.bottomMargin = 0;
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        SLog.info("onAnimationEnd");
        if (animatingTextView != null) {
            animatingTextView.setVisibility(View.VISIBLE);
            animatingTextView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    animatingTextView.setVisibility(View.INVISIBLE);
                    shopCustomerServiceFragment.onWelcomeMessageAnimationEnd();
                }
            }, 1500);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
