package com.awesome.dravin.appdemo1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Dravin on 6/30/2016.
 */
public class CustomVideoList extends ArrayAdapter<String> {


    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;
    public CustomVideoList(Activity context,
                      String[] web, Integer[] imageId) {
        super(context, R.layout.video_list_single, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.video_list_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.textViewVideoList);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageViewVideoList);
        txtTitle.setText(web[position]);

        imageView.setImageResource(imageId[0]);
        return rowView;
    }
}

