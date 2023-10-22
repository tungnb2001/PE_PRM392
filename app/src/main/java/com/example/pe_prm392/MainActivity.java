package com.example.pe_prm392;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.pe_prm392.Adapter.UserAdapter;
import com.example.pe_prm392.Models.User;
import com.example.pe_prm392.Service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        UserAdapter userAdapter = new UserAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://reqres.in/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);


        Call<User.UsersResponse> call = apiService.getUsers(1);
        call.enqueue(new Callback<User.UsersResponse>() {
            @Override
            public void onResponse(Call<User.UsersResponse> call, Response<User.UsersResponse> response) {
                if (response.isSuccessful()) {
                    List<User> userList = response.body().getData();
//                    Log.d("Data", "Received " + userList.size() + " users.");
                    userAdapter.setUserList(userList);
                    userAdapter.notifyDataSetChanged();
                } else {
                    Log.e("API Call", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User.UsersResponse> call, Throwable t) {
                Log.e("API Call", "Error: " + t.getMessage());
            }
        });
    }

}