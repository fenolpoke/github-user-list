package com.example.accessapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.accessapp.model.User;
import com.example.accessapp.model.UserModel;
import com.example.accessapp.view.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserViewModel extends ViewModel {

    private UserModel userModel;
    private MainActivity activity;
    private RequestQueue queue;
    private ArrayList<User> users;

    public void init(MainActivity activity){
        queue = Volley.newRequestQueue(activity);
        userModel = new UserModel();
        users = new ArrayList<>();
        // hard coded 100
        fillUsers(100);
    }


    public LiveData<ArrayList<User>> getUsers() {
        return userModel.getUsers();
    }

    public LiveData<Integer> getPosition(){
        return userModel.getPosition();
    }

    private void fillUsers(int totalUser){
        // get from github API
//        ArrayList<User> users = userModel.getUsers().getValue();

        String url = "https://api.github.com/users?since="
                +(users.isEmpty() ? 0 : users.get(users.size()-1).id);
        final UserViewModel userViewModel = this;
        int lastSize = users.size();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, response -> {
                    // fill users list
                    Log.d("Response", response.length()+"");
//                        ArrayList<User> temp = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.length(); i++){
                            JSONObject obj = response.getJSONObject(i);
                            Log.d("loop response", obj.toString());
                            User user = new User();
                            user.id = obj.getInt("id");
                            user.avatar_url = obj.getString("avatar_url");
                            user.site_admin = obj.getBoolean("site_admin");
                            user.login = obj.getString("login");
                            users.add(user);

                            // the easiest way of thinking i can do SORRY :(
                            if(lastSize + i + 1 >= totalUser) {
                                userModel.setUsers(users);
                                break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(users.size() < totalUser)
                        userViewModel.fillUsers(totalUser);
                }, error -> {
                    // TODO: Handle error
                    Log.e("volley error", "error:"+error.getMessage());
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("getHeader","get header");
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/vnd.github.v3+json");
                return headers;
            }
        };
        queue.add(jsonArrayRequest);
//        queue.start();
    }

    public void fillUser(int position){
        User user = userModel.getUsers().getValue().get(position);
        if(user.filled){
            userModel.setPosition(position);
        }
        else{
            // get from github API
            String url = "https://api.github.com/users/" + user.login;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, response -> {
                        // fill user
                        Log.d("Response", response.toString());
                        ArrayList<User> users = new ArrayList<>();
                        try {
                            user.filled = true;
                            user.bio = response.getString("bio");
                            user.blog = response.getString("blog");
                            user.location = response.getString("location");
                            user.name = response.getString("name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        userModel.setPosition(position);
                    }, error -> {
                        // TODO: Handle error
                        Log.e("volley error", ""+error.getMessage());
                    }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Log.d("getHeader","get header");
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Accept", "application/vnd.github.v3+json");
                    return headers;
                }
            };
            queue.add(jsonObjectRequest);
//            queue.start();

        }
    }
}
