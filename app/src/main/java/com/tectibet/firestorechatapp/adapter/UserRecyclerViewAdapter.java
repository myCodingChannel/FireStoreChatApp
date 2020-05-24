package com.tectibet.firestorechatapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tectibet.firestorechatapp.ChatActivity;
import com.tectibet.firestorechatapp.HomeActivity;
import com.tectibet.firestorechatapp.R;
import com.tectibet.firestorechatapp.domain.Users;

import java.util.List;

/**
 * Created by kharag on 20-05-2020.
 */
public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {
    Context context;
    List<Users> usersList;
    public UserRecyclerViewAdapter(Context context, List<Users> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.single_user_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.mName.setText(usersList.get(position).getName());
        holder.mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("id",usersList.get(position).userId);
                intent.putExtra("fcm",usersList.get(position).getFcm());
                intent.putExtra("login",usersList.get(position).getLogin());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mName =itemView.findViewById(R.id.name);
        }
    }
}
