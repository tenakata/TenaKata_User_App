package com.tenakata.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tenakata.R;

    public class GraphSpinnerAdapter extends BaseAdapter {

        private Context context;
        private String[] type;
        private LayoutInflater inflter;

        public GraphSpinnerAdapter(Context applicationContext,String[] type) {
            this.context = applicationContext;
            this.type = type;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return type.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.graph_spinner_layout, null);
            TextView names = view.findViewById(R.id.tvLanguageName);
            names.setText(type[i]);
            return view;
        }

}
