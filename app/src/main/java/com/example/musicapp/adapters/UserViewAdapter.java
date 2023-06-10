package com.example.musicapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.entities.User;
import com.example.musicapp.views.UserViewHolder;

import java.util.ArrayList;

public class UserViewAdapter extends RecyclerView.Adapter<UserViewHolder> {
    private ArrayList<User> usersList;
    MainActivity activity;

    public UserViewAdapter(ArrayList<User> usersList, MainActivity activity) {
        this.usersList = usersList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_view_user_item, parent, false);
        return new UserViewHolder(view, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User currentUser = usersList.get(position);
        holder.userNameTextView.setText(currentUser.getUserName());
        holder.user = usersList.get(position);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}
