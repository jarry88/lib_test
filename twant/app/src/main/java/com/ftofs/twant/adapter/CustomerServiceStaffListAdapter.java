package com.ftofs.twant.adapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
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
import com.ftofs.twant.entity.CustomerServiceStaffPair;
import com.ftofs.twant.fragment.ShopCustomerServiceFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class CustomerServiceStaffListAdapter extends BaseQuickAdapter<CustomerServiceStaffPair, BaseViewHolder>
                                        implements Animation.AnimationListener {
    /**
     * Adapter所在的Fragment
     */
    ShopCustomerServiceFragment shopCustomerServiceFragment;
    Animation animation;
    TextView animatingTextView;
    public CustomerServiceStaffListAdapter(ShopCustomerServiceFragment shopCustomerServiceFragment, int layoutResId, @Nullable List<CustomerServiceStaffPair> data) {
        super(layoutResId, data);

        this.shopCustomerServiceFragment = shopCustomerServiceFragment;
    }

    @Override
    protected void convert(BaseViewHolder helper, CustomerServiceStaffPair item) {
        helper.addOnClickListener(R.id.btn_init_cs_left, R.id.btn_init_cs_right);

        if (animation == null) {
            animation = AnimationUtils.loadAnimation(mContext, R.anim.welcome_message);
            animation.setAnimationListener(this);
        }

        if (item.left != null) {
            CustomerServiceStaff leftItem = item.left;

            ImageView imgStaffAvatar = helper.getView(R.id.img_staff_avatar_left);

            if (StringUtil.useDefaultAvatar(leftItem.avatar)) {
                Glide.with(mContext).load(R.drawable.grey_default_avatar).centerCrop().into(imgStaffAvatar);
            } else {
                Glide.with(mContext).load(StringUtil.normalizeImageUrl(leftItem.avatar)).centerCrop().into(imgStaffAvatar);
            }


            helper.setText(R.id.tv_staff_name_left, leftItem.staffName);

            if (leftItem.staffType == 1) {
                helper.setBackgroundRes(R.id.tv_staff_type_left, R.drawable.customer_service_type_indicator_bg_red)
                    .setText(R.id.tv_staff_type_left, "售前");
            } else {
                helper.setBackgroundRes(R.id.tv_staff_type_left, R.drawable.customer_service_type_indicator_bg_blue)
                        .setText(R.id.tv_staff_type_left, "售後");
            }

            TextView tvWelcomeMessage = helper.getView(R.id.tv_welcome_message_left);
            tvWelcomeMessage.setText(leftItem.welcomeMessage);

            if (leftItem.showWelcomeMessageAnimation) {
                tvWelcomeMessage.setVisibility(View.INVISIBLE);
                animatingTextView = tvWelcomeMessage;
                tvWelcomeMessage.startAnimation(animation);
            } else {
                tvWelcomeMessage.setVisibility(View.INVISIBLE);
            }
        }

        if (item.right != null) {
            CustomerServiceStaff rightItem = item.right;

            ImageView imgStaffAvatar = helper.getView(R.id.img_staff_avatar_right);

            if (StringUtil.useDefaultAvatar(rightItem.avatar)) {
                Glide.with(mContext).load(R.drawable.grey_default_avatar).centerCrop().into(imgStaffAvatar);
            } else {
                Glide.with(mContext).load(StringUtil.normalizeImageUrl(rightItem.avatar)).centerCrop().into(imgStaffAvatar);
            }


            helper.setText(R.id.tv_staff_name_right, rightItem.staffName);

            if (rightItem.staffType == 1) {
                helper.setBackgroundRes(R.id.tv_staff_type_right, R.drawable.customer_service_type_indicator_bg_red)
                        .setText(R.id.tv_staff_type_right, "售前");
            } else {
                helper.setBackgroundRes(R.id.tv_staff_type_right, R.drawable.customer_service_type_indicator_bg_blue)
                        .setText(R.id.tv_staff_type_right, "售後");
            }

            TextView tvWelcomeMessage = helper.getView(R.id.tv_welcome_message_right);
            tvWelcomeMessage.setText(rightItem.welcomeMessage);

            if (rightItem.showWelcomeMessageAnimation) {
                tvWelcomeMessage.setVisibility(View.INVISIBLE);
                animatingTextView = tvWelcomeMessage;
                tvWelcomeMessage.startAnimation(animation);
            } else {
                tvWelcomeMessage.setVisibility(View.INVISIBLE);
            }
            helper.setVisible(R.id.ll_right_container, true);
        } else {
            helper.setVisible(R.id.ll_right_container, false);
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
