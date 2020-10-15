package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.DistributionWithdrawRecord;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class DistributionWithdrawRecordAdapter extends BaseQuickAdapter<DistributionWithdrawRecord, BaseViewHolder> {
    Context context;
    int gray;
    int blue;
    int red;

    public DistributionWithdrawRecordAdapter(Context context, int layoutResId, @Nullable List<DistributionWithdrawRecord> data) {
        super(layoutResId, data);

        this.context = context;
        this.gray = context.getColor(R.color.gray_cc);
        this.blue = context.getColor(R.color.tw_blue);
        this.red = context.getColor(R.color.tw_red);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DistributionWithdrawRecord item) {
        helper.setGone(R.id.ll_withdraw_progress_container, item.expanded)
                .setGone(R.id.fl_withdraw_sn_container, item.expanded)
                .setText(R.id.tv_amount, "+" + StringUtil.formatFloat(item.marketingCommissionAmount))
                .setText(R.id.tv_ssn, String.valueOf(item.billSn));

        TextView tvCurrentStateTime = helper.getView(R.id.tv_current_state_time);
        TextView tvStatusText = helper.getView(R.id.tv_status_text);
        TextView tvTimeApply = helper.getView(R.id.tv_time_apply);
        TextView tvTimeAudit = helper.getView(R.id.tv_time_audit);
        TextView tvTimeArrive = helper.getView(R.id.tv_time_arrive);

        View vwIndicatorLine1 = helper.getView(R.id.vw_indicator_line_1);
        View vwIndicatorLine2 = helper.getView(R.id.vw_indicator_line_2);
        ImageView imgProgressIndAudit = helper.getView(R.id.img_progress_ind_audit);
        ImageView imgProgressIndArrive = helper.getView(R.id.img_progress_ind_arrive);

        tvTimeApply.setText(item.createTime);
        tvTimeAudit.setText("");
        tvTimeArrive.setText("");
        vwIndicatorLine1.setBackgroundColor(gray);
        vwIndicatorLine2.setBackgroundColor(gray);
        imgProgressIndAudit.setImageResource(R.drawable.blue_r6dp_circle);
        imgProgressIndArrive.setImageResource(R.drawable.blue_r6dp_circle);

        if (item.billState == DistributionWithdrawRecord.STATE_TO_BE_AUDIT) {
            tvCurrentStateTime.setText(item.createTime);
            tvStatusText.setText("待審核");
        } else if (item.billState == DistributionWithdrawRecord.STATE_PASS || item.billState == DistributionWithdrawRecord.STATE_NOT_PASS ||
                item.billState == DistributionWithdrawRecord.STATE_PAID) {
            tvCurrentStateTime.setText(item.adminTime);
            if (item.billState == DistributionWithdrawRecord.STATE_PASS) {
                tvStatusText.setText("審核通過");
                vwIndicatorLine1.setBackgroundColor(blue);
                imgProgressIndAudit.setImageResource(R.drawable.icon_checked);
            } else if (item.billState == DistributionWithdrawRecord.STATE_NOT_PASS) {
                tvStatusText.setText("審核不通過");
                vwIndicatorLine1.setBackgroundColor(red);
                imgProgressIndAudit.setImageResource(R.drawable.icon_remove_red);
            }
            tvTimeAudit.setText(item.adminTime);

            if (item.billState == DistributionWithdrawRecord.STATE_PAID) {
                tvCurrentStateTime.setText(item.payTime);
                tvStatusText.setText("財務已付款");
                tvTimeArrive.setText(item.payTime);
                vwIndicatorLine2.setBackgroundColor(blue);
                imgProgressIndArrive.setImageResource(R.drawable.icon_checked);
            }
        }
    }
}
