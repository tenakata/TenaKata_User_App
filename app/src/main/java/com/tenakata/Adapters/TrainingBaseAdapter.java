package com.tenakata.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import androidx.databinding.DataBindingUtil;

import com.tenakata.Models.TrainingListModel;
import com.tenakata.R;
import com.tenakata.databinding.TrainingBaseAdapetrBinding;
import java.util.List;

public class TrainingBaseAdapter extends BaseAdapter {

    private Context context;
    private List<TrainingListModel.ResultBean> list;
    private RowClick callBack;

    public TrainingBaseAdapter(Context context, List<TrainingListModel.ResultBean> list, RowClick callBack) {
        this.list = list;
        this.context = context;
        this.callBack = callBack;
    }

    @Override
    public int getCount() {
        if (list.size()>0){
            return list.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            TrainingBaseAdapetrBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                    , R.layout.training_base_adapetr, parent, false);
            holder = new ViewHolder(itemBinding);
            holder.binding.tvSubTitle.setText(list.get(position).getTitle());
            holder.view.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onRowClick(position,list.get(position).getId());
            }
        });
        return holder.view;
    }

    public interface RowClick {
        void onRowClick(int position,String id);

    }

    private class ViewHolder {
        View view;
        TrainingBaseAdapetrBinding binding;

        ViewHolder(TrainingBaseAdapetrBinding binding) {
            this.view = binding.getRoot();
            this.binding = binding;
        }
    }
}
