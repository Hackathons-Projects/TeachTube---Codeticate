package com.hackathon.teachtube.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hackathon.teachtube.R;

import java.util.ArrayList;

public class CustomSpinnerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> spin_item;
    private int default_textColor;
    private int textColor;

    private boolean center = false;

    public CustomSpinnerAdapter(Context context, ArrayList<String> spin_item , int default_textColor, int textColor) {
        this.context = context;
        this.spin_item = spin_item;
        this.default_textColor = default_textColor;
        this.textColor = textColor;
    }

    public CustomSpinnerAdapter(Context context, ArrayList<String> spin_item, int default_textColor, int textColor, boolean center) {
        this.context = context;
        this.spin_item = spin_item;
        this.default_textColor = default_textColor;
        this.textColor = textColor;
        this.center = center;
    }

    @Override
    public int getCount() {
        return spin_item.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.custom_spin_item , null);
        TextView textView = convertView.findViewById(R.id.custom_spin_item_text);
        textView.setText(spin_item.get(position));

        if (position == 0)
        {
            textView.setTextColor(default_textColor);
            textView.setAlpha(1f);
        }
        else
        {
            textView.setTextColor(textColor);
            textView.setAlpha(1f);
        }

        if (center != false)
        {
            textView.setGravity(Gravity.CENTER);
        }else{
            textView.setGravity(Gravity.CENTER|Gravity.START);
        }

        return convertView;
    }


}
