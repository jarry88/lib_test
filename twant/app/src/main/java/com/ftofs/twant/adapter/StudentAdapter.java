package com.ftofs.twant.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ftofs.twant.R;
import com.gzp.lib_common.utils.SLog;

import java.util.List;

/**
 * 僅用於測試
 * @author zwm
 */
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private List<String> studentList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        public ViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_name);
        }
    }

    public StudentAdapter(List<String> studentList) {
        this.studentList = studentList;
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        SLog.info("onViewRecycled, %s", holder.tvName.getText());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = studentList.get(position);
        holder.tvName.setText(name);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }
}
