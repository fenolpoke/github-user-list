package com.example.accessapp.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.accessapp.R;
import com.example.accessapp.model.User;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserDetailFragment extends DialogFragment implements View.OnClickListener {

    private User user;

    public UserDetailFragment() {
        // Required empty public constructor
    }

    public static UserDetailFragment newInstance(User user) {
        UserDetailFragment fragment = new UserDetailFragment();
        fragment.user = user;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = initViews();
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setView(view)
//                .setCancelable(true)
//                .setPositiveButton(R.string.done, ((dialog, which) -> onNewGame()))
                .create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setCancelable(true);

        return alertDialog;

    }

    private View initViews() {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_user_detail, null, false);

        RequestQueue queue = Volley.newRequestQueue(getContext());

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
        imageLoader.get(user.avatar_url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                ((ImageView) view.findViewById(R.id.avatarImageView)).setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("user adapter", "volley error : " + error.getMessage());

            }
        });

        String name = user.name == "null" ? "" : user.name;
        if(user.site_admin)
            name = name + " (Staff)";

        ((TextView) view.findViewById(R.id.nameTextView)).setText(name);

        if(user.bio == "null")
            ((TextView) view.findViewById(R.id.bioTextView)).setVisibility(View.GONE);
        else
            ((TextView) view.findViewById(R.id.bioTextView)).setText(user.bio);

        ((TextView) view.findViewById(R.id.loginTextView)).setText(user.login);

        if(user.blog == "null")
            ((TextView) view.findViewById(R.id.blogTextView)).setVisibility(View.GONE);
        else
            ((TextView) view.findViewById(R.id.blogTextView)).setText(user.blog);

        if(user.location == "null")
            ((TextView) view.findViewById(R.id.locationTextView)).setVisibility(View.GONE);
        else
            ((TextView) view.findViewById(R.id.locationTextView)).setText(user.location);

        ((Button) view.findViewById(R.id.exitButton)).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
    }
}