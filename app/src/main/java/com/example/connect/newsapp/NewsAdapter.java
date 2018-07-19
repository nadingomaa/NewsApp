package com.example.connect.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(@NonNull Context context, ArrayList<News> resource) {
        super(context,0, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.news_list_item,parent,false);
        }
        News currentNew=getItem(position);

        TextView titleTextView =convertView.findViewById(R.id.title);
        titleTextView.setText(currentNew.getTitle());

        TextView sectionTextView=convertView.findViewById(R.id.section);
        sectionTextView.setText("Section: "+currentNew.getSectionName());

        TextView publishedDateTextView=convertView.findViewById(R.id.publishedDate);
        publishedDateTextView.setText("Published at : "+currentNew.getPublishedDate());



     return convertView;
    }
}
