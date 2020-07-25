package com.example.accessapp.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.accessapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserAdapter extends BaseAdapter {
    ArrayList<User> users;

    Context context;
    RequestQueue queue;

     public UserAdapter(Context context, ArrayList<User> users){
         this.users = users;
         this.context = context;
         queue = Volley.newRequestQueue(context);
     }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = ((LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.user_item_layout, null);

        ImageView img = (ImageView) view.findViewById(R.id.imageView);

        ImageLoader imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
            Map<String, Bitmap> cache = new HashMap<String, Bitmap>();
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }
            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
        imageLoader.get(users.get(position).avatar_url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                img.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("user adapter", "volley error : " + error.getMessage());

            }
        });

        TextView text = (TextView) view.findViewById(R.id.textView);
        text.setText(users.get(position).login);

        if(!users.get(position).site_admin)
            text.setCompoundDrawables(null, null, null, null);

        return view;
    }
}
