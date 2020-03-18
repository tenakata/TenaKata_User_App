package com.tenakata.Adapters;

import android.content.Context;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.tenakata.Models.CashSalesCreditModel;
import com.tenakata.R;
import com.tenakata.Utilities.HRValidationHelper;
import com.tenakata.databinding.HomeCashSalesAdapterBinding;

import java.util.List;

public class HomeSalesBaseAdapter extends BaseAdapter {
    private Context context;
    private List<CashSalesCreditModel.ResultBean> list;
    private RowClick callBack;
    private String type;

    private String receiptpath;

    public HomeSalesBaseAdapter(Context context, List<CashSalesCreditModel.ResultBean> list,RowClick callBack,String type) {
        this.list = list;
        this.context = context;
        this.callBack = callBack;
        this.type = type;
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
        final ViewHolder holder;

        if (convertView == null) {
            HomeCashSalesAdapterBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                    , R.layout.home_cash_sales_adapter, parent, false);
            holder = new ViewHolder(itemBinding);
            holder.view.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Glide.with(context)
                .load(list.get(position).getAttach_recepit())
                .apply(new RequestOptions()
                        .transform(new RoundedCorners(20)).placeholder(R.drawable.offer_sel))
                .into(holder.binding.imageView11);
        holder.binding.viewDate.setText("Captured: "+HRValidationHelper.optional(list.get(position).getDate()));
        holder.binding.viewPrice.setText("KES "+HRValidationHelper.optional(list.get(position).getAmount()));
        holder.binding.viewShopName.setText(HRValidationHelper.optional(list.get(position).getName()));
        holder.binding.viewTitle.setText(HRValidationHelper.optional(list.get(position).getItem_list()));

        if (type!=null && !type.equals("") && (type.equalsIgnoreCase("cashSale")||
                type.equalsIgnoreCase("cashPurchase"))){
            holder.binding.viewRemindBtn.setVisibility(View.GONE);
            holder.binding.viewPayBtn.setVisibility(View.GONE);
        }else if (type!=null && !type.equals("") && type.equalsIgnoreCase("purchaseCredit")){
            holder.binding.viewRemindBtn.setVisibility(View.GONE);
            holder.binding.viewPayBtn.setVisibility(View.VISIBLE);
        } else {
            holder.binding.viewRemindBtn.setVisibility(View.VISIBLE);
            holder.binding.viewPayBtn.setVisibility(View.VISIBLE);
        }

        holder.binding.viewRemindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onRemindClick(list.get(position).getId(),list.get(position).getAmount());
            }
        });

        holder.binding.viewPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onPayClick(list.get(position).getId(),list.get(position).getAmount());
            }
        });


        holder.binding.viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("yoooo","yoooo");
                if (list.get(position).getAttach_recepit()!=null){
                    Uri uri=Uri.parse(list.get(position).getAttach_recepit());
                     receiptpath=uri.toString();
                }


                callBack.onViewDetailsClick(position,list.get(position).getId(),list.get(position).getName(),
                        receiptpath,list.get(position).getAmount(),list.get(position).getItem_list()
                        );
            }
        });

        return holder.view;
    }

    public void refresh(List<CashSalesCreditModel.ResultBean> list1) {
        this.list = list1;
        notifyDataSetChanged();
    }

    public void onMoreDataReq(List<CashSalesCreditModel.ResultBean> list1) {
        this.list.addAll(list1);
        notifyDataSetChanged();
    }

    public interface RowClick {
        void onPayClick(String id,String totalAmount);
        void onRemindClick(String id,String totalAmount);
        void onViewDetailsClick(int position,String id, String name, String receiptpath,String amount,String list);
    }

    private class ViewHolder {
        View view;
        HomeCashSalesAdapterBinding binding;

        ViewHolder(HomeCashSalesAdapterBinding binding) {
            this.view = binding.getRoot();
            this.binding = binding;
        }
    }
}
