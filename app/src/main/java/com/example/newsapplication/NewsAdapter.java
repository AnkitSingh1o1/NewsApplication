package com.example.newsapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<ANews> {
    public NewsAdapter(Context context, List<ANews> news) {

        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)  getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        ANews currNews = getItem(position);

        ImageView newsThumbnail = convertView.findViewById(R.id.imageView);
        try{
            URL imageURL = new URL(currNews.getThumbnail());
            Glide.with(getContext()).load(imageURL).into(newsThumbnail);}
        catch (IOException e){
            e.printStackTrace();
        }

        TextView heading = convertView.findViewById(R.id.heading);
        heading.setText(currNews.getHeading());

        TextView time = convertView.findViewById(R.id.time);
        time.setText(currNews.getTime());

        TextView source = convertView.findViewById(R.id.source);
        source.setText(currNews.getSource());

        return convertView;

    }
}
