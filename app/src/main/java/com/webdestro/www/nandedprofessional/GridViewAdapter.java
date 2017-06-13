package com.webdestro.www.nandedprofessional;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/**
 * Created by Admin on 6/8/2017.
 */

public class GridViewAdapter extends BaseAdapter {

    //Imageloader to load images
    private ImageLoader imageLoader;

    //Context
    private Context context;

    //Array List that would contain the urls and the titles for the images
    private ArrayList<String> images;
    private ArrayList<String> names;

    public GridViewAdapter (Context context, ArrayList<String> images, ArrayList<String> names){
        //Getting all the values
        this.context = context;
        this.images = images;
        this.names = names;
    }



    @Override
    public int getCount() {
        return images.size();


    }

    @Override
    public Object getItem(int position) {


        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Creating a linear layout
        //LinearLayout linearLayout = new LinearLayout(context);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View linearLayout;


            linearLayout = new View(context);

            // get layout from mobile.xml
            linearLayout = inflater.inflate(R.layout.gridviewelement, null);

            // set value into textview
            final TextView textView = (TextView) linearLayout
                    .findViewById(R.id.gridtext);


        //linearLayout.setOrientation(LinearLayout.VERTICAL);
        //linearLayout.setPadding(2,2,2,2);

        //NetworkImageView
        //NetworkImageView networkImageView = new NetworkImageView(context);
        final NetworkImageView networkImageView = (NetworkImageView) linearLayout.findViewById(R.id.gridimage);




        //Initializing ImageLoader
        imageLoader = MainActivity.CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(images.get(position), ImageLoader.getImageListener(networkImageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        //Setting the image url to load
        networkImageView.setImageUrl(images.get(position),imageLoader);

        //Creating a textview to show the title
        /*
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.parseColor("#bb7824"));*/
        textView.setText(names.get(position));

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        //Scaling the imageview
        //networkImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //networkImageView.setPadding(2,2,2,2);
        //networkImageView.setLayoutParams(new GridView.LayoutParams(200,200));
        //Adding views to the layout
        //linearLayout.addView(networkImageView);

        //linearLayout.addView(textView);

        //Returnint the layout

/*
        networkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("code",textView.getText().toString());

                Intent intent = new Intent(networkImageView.getContext(),SubCategory.class);

            }
        });*/
        return linearLayout;
    }
}
