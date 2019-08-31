package com.example.mehar.newsfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(Context context, ArrayList<News> news) {
        super(context, 0, news);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        News currentListItem = getItem(position);

        TextView webTitle = convertView.findViewById(R.id.web_title);
        webTitle.setText(currentListItem.getWebTitle());

        TextView sectionName = convertView.findViewById(R.id.section_name);
        sectionName.setText(currentListItem.getSectionName());

        TextView contributor = convertView.findViewById(R.id.contributor);
        contributor.setText(currentListItem.getContributor());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ss'Z'");
        Date dateObject = null;
        try {
            dateObject = dateFormat.parse(currentListItem.getPublishDateAndTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView publishDate = convertView.findViewById(R.id.publish_date);
        String date = formatDate(dateObject);
        publishDate.setText(date);

        TextView publishTime = convertView.findViewById(R.id.publish_time);
        String time = formatTime(dateObject);
        publishTime.setText(time);

        return convertView;
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

}