package com.example.zhang.thinmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zhang on 2018/1/22.
 */

public class navigationadapter extends ArrayAdapter<navigation> {
    private int resourceId;

    public navigationadapter(Context context, int textViewResourceId,
                             List<navigation> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        navigation navigation = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent,
                false);
        ImageView navigationImage = (ImageView) view.findViewById(R.id.navigation_image);
        TextView navigationText = (TextView) view.findViewById(R.id.navigation_name);
        navigationImage.setImageResource(navigation.getImageId());
        navigationText.setText(navigation.getName());
        return  view;
    }
}

