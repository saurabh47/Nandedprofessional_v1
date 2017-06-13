package com.webdestro.www.nandedprofessional;

/*Saurabh Gangamwar*/

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Web api url  .
   public static final String DATA_URL = "http://webdestro.com/newapi/mcategory";

  //  public static final String DATA_URL = "http://webdestro.com/newapi/scategory";

    //Tag values to read from json
    public static final String TAG_IMAGE_URL = "cat_icon_url";


    public static final String TAG_NAME = "cat_title";

    //GridView Object
    private GridView gridView;

    //ArrayList for Storing image urls and titles
    private ArrayList<String> images;
    private ArrayList<String> names;
    private AdView mAdView;


    private void getData(){
        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(this, "Please wait...","Fetching data...",false,false);

        //Creating a json array request to get the json from our api
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(DATA_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing the progressdialog on response
                        loading.dismiss();

                        //Displaying our grid
                        showGrid(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding our request to the queue
        requestQueue.add(jsonArrayRequest);
    }

    private void showGrid(JSONArray jsonArray){
        //Looping through all the elements of json array
        for(int i = 0; i<jsonArray.length(); i++){
            //Creating a json object of the current index
            JSONObject obj = null;
            try {
                //getting json object from current index
                obj = jsonArray.getJSONObject(i);

                //getting image url and title from json object
                images.add("http://webdestro.com/np/images/icons/"+obj.getString(TAG_IMAGE_URL));


                names.add(obj.getString(TAG_NAME));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //Creating GridViewAdapter Object
        GridViewAdapter gridViewAdapter = new GridViewAdapter(this,images,names);

        //Adding adapter to gridview
        gridView.setAdapter(gridViewAdapter);
    }

    public static class CustomVolleyRequest {

        private static CustomVolleyRequest customVolleyRequest;
        private static Context context;
        private RequestQueue requestQueue;
        private ImageLoader imageLoader;

        private CustomVolleyRequest(Context context) {
            this.context = context;
            this.requestQueue = getRequestQueue();

            imageLoader = new ImageLoader(requestQueue,
                    new ImageLoader.ImageCache() {
                        private final LruCache<String, Bitmap>
                                cache = new LruCache<String, Bitmap>(20);

                        @Override
                        public Bitmap getBitmap(String url) {
                            return cache.get(url);
                        }

                        @Override
                        public void putBitmap(String url, Bitmap bitmap) {
                            cache.put(url, bitmap);
                        }
                    });
        }

        public static synchronized CustomVolleyRequest getInstance(Context context) {
            if (customVolleyRequest == null) {
                customVolleyRequest = new CustomVolleyRequest(context);
            }
            return customVolleyRequest;
        }

        public RequestQueue getRequestQueue() {
            if (requestQueue == null) {
                Cache cache = new DiskBasedCache(context.getCacheDir(), 10 * 1024 * 1024);
                Network network = new BasicNetwork(new HurlStack());
                requestQueue = new RequestQueue(cache, network);
                requestQueue.start();
            }
            return requestQueue;
        }

        public ImageLoader getImageLoader() {
            return imageLoader;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridview);


        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
/*
To Display banner ADS
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

*/

        images = new ArrayList<>();
        names = new ArrayList<>();



        //Calling the getData method
        getData();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("hello",String.valueOf(position));
                Intent intent = new Intent(getApplicationContext(),SubCategory.class);
                Bundle bundle = new Bundle();
                bundle.putString("sub_cat",names.get(position));
               //Add your data to bundle
                bundle.putString("position",String.valueOf(position+1));
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }

}
