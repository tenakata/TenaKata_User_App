package com.tenakata.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.tenakata.Fragments.FragmentCashFlow;
import com.tenakata.Fragments.FragmentHome;
import com.tenakata.Fragments.FragmentPurchaseFlow;
import com.tenakata.R;
import com.tenakata.Utilities.DimentionSessionManager;
import com.tenakata.Utilities.HRValidationHelper;

import java.util.List;

import javax.security.auth.callback.Callback;

public class HomeViewPagerAdapter extends RecyclerView.Adapter<HomeViewPagerAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<String>filterBean;
    private String salePrice,purchasePrice;
    private Callback callback;
    public HomeViewPagerAdapter(List<String> beans, Context context, String salePrice,String purchasePrice,Callback callback) {
        this.context = context;
        this.filterBean = beans;
        this.salePrice= salePrice;
        this.purchasePrice = purchasePrice;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.callback=callback;
    }

    public void refresh(List<String> beans,String purchasePrice,String salePrice) {
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
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onSaleClick();
                }
            });
        }else {
            holder.priceTv.setText("KES "+HRValidationHelper.optional(purchasePrice));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callback!=null){
                        callback.onPurchaseClick();
                    }

                }
            });
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
        ImageView next;

        public ViewHolder(View itemView) {
            super(itemView);
            sales = itemView.findViewById(R.id.textView16);
            priceTv = itemView.findViewById(R.id.textView17);
            next= itemView.findViewById(R.id.textView18);
            viewCircleTextView = itemView.findViewById(R.id.viewCircleTextView);
        }
    }

    public interface Callback {
        void onSaleClick();
        void onPurchaseClick();
    }


}
