package com.example.accessapp.view;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.accessapp.R;
import com.example.accessapp.databinding.ActivityMainBinding;
import com.example.accessapp.model.User;
import com.example.accessapp.model.UserAdapter;
import com.example.accessapp.viewmodel.UserViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String USER_DETAIL_TAG = "user_detail";

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
    }

    private void initDataBinding(){
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.init(this);
        activityMainBinding.setUserViewModel(userViewModel);
        setUpUsersChangeListener();
        setUpPositionChangeListener();
    }

    private void setUpUsersChangeListener() {
        userViewModel.getUsers().observe(this, this::onUserChanged);
    }

    private void setUpPositionChangeListener() {
        userViewModel.getPosition().observe(this, this::onPositionChanged);
    }

    @VisibleForTesting
    public void onUserChanged(ArrayList<User> users) {

        Log.d("on user change", users.size()+"");

        ListView listview = (ListView) findViewById(R.id.listview);

        listview.setAdapter(new UserAdapter(this, users));

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userViewModel.fillUser(position);
                if(users.get(position).filled){
                    UserDetailFragment fragment = UserDetailFragment.newInstance(users.get(position));
                    fragment.show(getSupportFragmentManager(), USER_DETAIL_TAG);
                }
                else{

                }
            }
        });

    }
    @VisibleForTesting
    public void onPositionChanged(int position) {
        UserDetailFragment fragment =
                UserDetailFragment.newInstance(userViewModel.getUsers().getValue().get(position));
        fragment.show(getSupportFragmentManager(), USER_DETAIL_TAG);


    }

}