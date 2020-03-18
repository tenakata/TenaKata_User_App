package com.tenakata.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import com.tenakata.R;
import com.tenakata.databinding.PagerRowViewBinding;
import java.util.List;

public class TutorialsAdapter extends PagerAdapter {
    private Context context;
    private PagerRowViewBinding binding;
    private List<String> text;
    private List<Integer> icons;

    public TutorialsAdapter(Context context,List<Integer>icons, List<String> text) {
        this.context = context;
        this.text = text;
        this.icons = icons;

    }

    @Override
    public int getCount() {
        return text.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View viewItem = LayoutInflater.from(container.getContext()).inflate(R.layout.pager_row_view, container, false);
        binding = DataBindingUtil.bind(viewItem);

        binding.images.setImageResource(icons.get(position));
        binding.pagerText.setText(text.get(position));
        container.addView(viewItem);
        return viewItem;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}

