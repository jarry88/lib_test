package com.ftofs.twant.adapter;

import android.content.Context;
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
    public DistributionWithdrawRecordAdapter(Context context, int layoutResId, @Nullable List<DistributionWithdrawRecord> data) {
        super(layoutResId, data);

        this.context = context;
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

        tvTimeApply.setText(item.createTime);
        tvTimeAudit.setText("");
        tvTimeArrive.setText("");
        if (item.billState == DistributionWithdrawRecord.STATE_TO_BE_AUDIT) {
            tvCurrentStateTime.setText(item.createTime);
            tvStatusText.setText("待審核");
        } else if (item.billState == DistributionWithdrawRecord.STATE_PASS || item.billState == DistributionWithdrawRecord.STATE_NOT_PASS) {
            tvCurrentStateTime.setText(item.adminTime);
            if (item.billState == DistributionWithdrawRecord.STATE_PASS) {
                tvStatusText.setText("審核通過");
            } else {
                tvStatusText.setText("審核不通過");
            }
            tvTimeAudit.setText(item.adminTime);
        } else  if (item.billState == DistributionWithdrawRecord.STATE_PAID) {
            tvCurrentStateTime.setText(item.payTime);
            tvStatusText.setText("財務已付款");
            tvTimeArrive.setText(item.payTime);
        }
    }
}
