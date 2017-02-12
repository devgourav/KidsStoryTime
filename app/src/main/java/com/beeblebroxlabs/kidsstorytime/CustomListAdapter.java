package com.beeblebroxlabs.kidsstorytime;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * class:CustomListAdapter
 * method:getView()
 * This is used to display the custom list of stories
 * */

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> titles;
    private final ArrayList<String> imgid;

    public CustomListAdapter(Activity context, ArrayList titles, ArrayList imgId) {
        super(context, R.layout.mylist, titles);
        this.context=context;
        this.titles=titles;
        this.imgid=imgId;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.titleText);
        ImageView storyImage = (ImageView) rowView.findViewById(R.id.storyImage);

        titleText.setText(titles.get(position));

        int resId=context.getResources().getIdentifier(imgid.get(position),"raw",context.getPackageName());
        storyImage.setImageResource(resId);

        return rowView;
    }
}