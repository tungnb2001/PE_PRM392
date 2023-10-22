package com.example.pe_prm392;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pe_prm392.Models.User;
import com.example.pe_prm392.Service.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {
    private TextView idTextView, emailTextView, firstNameTextView, lastNameTextView;
    private ImageView avatarImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_user);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Retrieve the user's ID from the Intent
        int userId = getIntent().getIntExtra("user_id", -1);

        idTextView = findViewById(R.id.id_text_view);
        emailTextView = findViewById(R.id.email_text_view);
        firstNameTextView = findViewById(R.id.first_name_text_view);
        lastNameTextView = findViewById(R.id.last_name_text_view);
        avatarImageView = findViewById(R.id.avatar_image_view);

        if (userId != -1) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://reqres.in/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiService apiService = retrofit.create(ApiService.class);

            Call<User.UserDetailResponse> call = apiService.getUserDetails(userId);

            call.enqueue(new Callback<User.UserDetailResponse>() {
                @Override
                public void onResponse(Call<User.UserDetailResponse> call, Response<User.UserDetailResponse> response) {
                    if (response.isSuccessful()) {
                        User user = response.body().getData();

                        // Update the UI elements with user details
                        idTextView.setText("ID: " + user.getId());
                        emailTextView.setText("Email: " + user.getEmail());
                        firstNameTextView.setText("First Name: " + user.getFirst_name());
                        lastNameTextView.setText("Last Name: " + user.getLast_name());

                        Glide.with(getApplicationContext())
                                .load(user.getAvatar())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(avatarImageView);

                    } else {
                        Log.e("API Call", "Error: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<User.UserDetailResponse> call, Throwable t) {
                    Log.e("API Call", "Error: " + t.getMessage());
                }
            });
        } else {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
