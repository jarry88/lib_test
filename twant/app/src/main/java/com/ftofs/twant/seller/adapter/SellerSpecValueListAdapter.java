package com.ftofs.twant.seller.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.seller.entity.SellerSpecValueListItem;

import java.util.List;

public class SellerSpecValueListAdapter extends BaseMultiItemQuickAdapter<SellerSpecValueListItem, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public SellerSpecValueListAdapter(List<SellerSpecValueListItem> data) {
        super(data);

        addItemType(SellerSpecValueListItem.ITEM_TYPE_NORMAL, R.layout.spec_value_list_item);
        addItemType(SellerSpecValueListItem.ITEM_TYPE_FOOTER, R.layout.add_spec_value_list_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, SellerSpecValueListItem item) {
        if (item.getItemType() == SellerSpecValueListItem.ITEM_TYPE_NORMAL) {
            helper.addOnClickListener(R.id.btn_remove);
            
            EditText etSpecValue = helper.getView(R.id.et_spec_value);
            etSpecValue.setText(item.specValueName);
            etSpecValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int position = helper.getAdapterPosition();
                    List<SellerSpecValueListItem> data = getData();
                    SellerSpecValueListItem item = data.get(position);
                    item.specValueName = s.toString();
                }
            });
            SLog.info("specValueName[%s]", item.specValueName);
        } else {
            helper.addOnClickListener(R.id.btn_add_spec_value);
        }
    }
}


