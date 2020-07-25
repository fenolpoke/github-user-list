package com.example.accessapp.model;


import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class UserModel {
    private MutableLiveData<ArrayList<User>> users = new MutableLiveData<ArrayList<User>>();
    private MutableLiveData<Integer> position = new MutableLiveData<Integer>();

    public UserModel(){
    }

    public void setUsers(ArrayList<User> users){
        this.users.setValue(users);
    }

    public void setPosition(int position) {
        this.position.setValue(position);
    }

    public MutableLiveData<ArrayList<User>> getUsers() {
        return users;
    }

    public MutableLiveData<Integer> getPosition() {
        return position;
    }
}
