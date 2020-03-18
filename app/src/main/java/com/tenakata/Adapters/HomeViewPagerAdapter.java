package com.tenakata.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.tenakata.R;
import com.tenakata.Utilities.DimentionSessionManager;
import com.tenakata.Utilities.HRValidationHelper;

import java.util.List;

public class HomeViewPagerAdapter extends RecyclerView.Adapter<HomeViewPagerAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<String>filterBean;
    private String salePrice,purchasePrice;

    public HomeViewPagerAdapter(List<String> beans, Context context,String salePrice,String purchasePrice) {
        this.context = context;
        this.filterBean = beans;
        this.salePrice= salePrice;
        this.purchasePrice = purchasePrice;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(List<String> beans,String salePrice,String purchasePrice) {
        this.filterBean.clear();
        this.filterBean.addAll(beans);
        this.salePrice= salePrice;
        this.purchasePrice = purchasePrice;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return filterBean.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_view_pager, parent, false);

        itemView.setLayoutParams(new FrameLayout.LayoutParams(new DimentionSessionManager(context).getWidth(), FrameLayout.LayoutParams.WRAP_CONTENT));
        return new HomeViewPagerAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        if (position == 0){
            holder.priceTv.setText("KES "+HRValidationHelper.optional(salePrice));
        }else {
            holder.priceTv.setText("KES "+HRValidationHelper.optional(purchasePrice));
        }

        holder.sales.setText(HRValidationHelper.optional(filterBean.get(position)));
        if (filterBean.get(position).equalsIgnoreCase("Sales")){
            holder.viewCircleTextView.setBackground(context.getResources().getDrawable(R.drawable.circle_blue));
            holder.viewCircleTextView.setText("S");
        }else {
            holder.viewCircleTextView.setBackground(context.getResources().getDrawable(R.drawable.circle_blue_light));
            holder.viewCircleTextView.setText("P");
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView sales,viewCircleTextView,priceTv;

        public ViewHolder(View itemView) {
            super(itemView);
            sales = itemView.findViewById(R.id.textView16);
            priceTv = itemView.findViewById(R.id.textView17);
            viewCircleTextView = itemView.findViewById(R.id.viewCircleTextView);
        }
    }


}
