package com.example.pe_prm392.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pe_prm392.DetailActivity;
import com.example.pe_prm392.Models.User;
import com.example.pe_prm392.R;
import com.example.pe_prm392.Service.ApiService;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<User> userList;

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);

        holder.idTextView.setText(String.valueOf(user.getId()));
        holder.userNameTextView.setText(user.getFirst_name() + " " + user.getLast_name());
        holder.emailTextView.setText(user.getEmail());


        Glide.with(holder.itemView.getContext())
                .load(user.getAvatar())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.avatarImageView);

        holder.detailUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the user's ID
                int userId = user.getId();

                // Create an intent to start the DetailActivity
                Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);

                // Pass the user's ID as an extra to the DetailActivity
                intent.putExtra("user_id", userId);

                // Start the DetailActivity
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView idTextView;
        public TextView userNameTextView;
        public TextView emailTextView;
        public ImageView avatarImageView;

        public Button detailUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.idTextView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);

            detailUser = itemView.findViewById(R.id.detailButton);

        }
    }
}

