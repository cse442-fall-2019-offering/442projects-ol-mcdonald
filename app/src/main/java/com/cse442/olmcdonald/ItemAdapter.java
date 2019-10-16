package com.cse442.olmcdonald;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Custom grid view adapter to bind data to populate GridView
 */
public class ItemAdapter extends ArrayAdapter<Item> {

    public ItemAdapter(Context context, ArrayList<Item> itemArrayList) {
        super(context, 0, itemArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item i = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_item_adapter, parent, false);
        }
        TextView tv_name = convertView.findViewById(R.id.tv_name);
        TextView tv_price = convertView.findViewById(R.id.tv_price);
        TextView tv_amount = convertView.findViewById(R.id.tv_amount);
        ImageView img_view = convertView.findViewById(R.id.imageView);
        img_view.setAdjustViewBounds(true);
        img_view.setImageBitmap(i.getImg_data());
        tv_name.setText(i.getName());
        tv_price.setText("Price: " + i.getPrice());
        tv_amount.setText("Amount: " + i.getAmount());
        return convertView;
    }
}
