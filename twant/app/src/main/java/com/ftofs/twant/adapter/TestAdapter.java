package com.ftofs.twant.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftofs.twant.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 測試RecyclerView用的adapter
 * @author zwm
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    private List<String> textList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTest;

        public ViewHolder(View view) {
            super(view);
            tvTest = view.findViewById(R.id.tv_test);
        }
    }

    public TestAdapter() {
        textList = new ArrayList<>();
        textList.add("aa");
        textList.add("bb");
        textList.add("cc");
        textList.add("dd");
        textList.add("aa");
        textList.add("bb");
        textList.add("cc");
        textList.add("dd");
        textList.add("aa");
        textList.add("bb");
        textList.add("cc");
        textList.add("dd");
        textList.add("aa");
        textList.add("bb");
        textList.add("cc");
        textList.add("dd");
        textList.add("aa");
        textList.add("bb");
        textList.add("cc");
        textList.add("dd");
        textList.add("aa");
        textList.add("bb");
        textList.add("cc");
        textList.add("dd");
        textList.add("aa");
        textList.add("bb");
        textList.add("cc");
        textList.add("dd");
        textList.add("111");
        textList.add("222");
        textList.add("333");
        textList.add("444");
        textList.add("111");
        textList.add("222");
        textList.add("333");
        textList.add("444");
        textList.add("222");
        textList.add("333");
        textList.add("444");


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_test_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String text = textList.get(position);
        holder.tvTest.setText(text);
    }

    @Override
    public int getItemCount() {
        return textList.size();
    }
}
