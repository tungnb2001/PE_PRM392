package com.example.pe_prm392;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import com.example.pe_prm392.Adapter.UserAdapter;
import com.example.pe_prm392.Models.User;
import com.example.pe_prm392.Service.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText searchByNameAndEmail;
    private Spinner spinnerPage;
    private UserAdapter userAdapter;
    private ApiService apiService;
    private int selectedPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchByNameAndEmail = findViewById(R.id.editTextSearch);
        spinnerPage = findViewById(R.id.spinnerPage);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(new ArrayList<>());
        recyclerView.setAdapter(userAdapter);

        setupRetrofit();

        setupSearchFunctionality();
    }

    private void setupRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://reqres.in/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
        loadUsers(selectedPage);
    }

    private void setupSearchFunctionality() {
        searchByNameAndEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = s.toString();
                performSearch(searchText);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.page, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPage.setAdapter(arrayAdapter);

        spinnerPage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPageStr = spinnerPage.getSelectedItem().toString();
                selectedPage = Integer.parseInt(selectedPageStr);
                loadUsers(selectedPage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadUsers(int page) {
        Call<User.UsersResponse> call = apiService.getUsers(page);
        call.enqueue(new Callback<User.UsersResponse>() {
            @Override
            public void onResponse(Call<User.UsersResponse> call, Response<User.UsersResponse> response) {
                if (response.isSuccessful()) {
                    List<User> userList = response.body().getData();
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

    private void performSearch(String searchText) {
        Call<User.SearchResponse> call = apiService.searchUsers(searchText, searchText);
        call.enqueue(new Callback<User.SearchResponse>() {
            @Override
            public void onResponse(Call<User.SearchResponse> call, Response<User.SearchResponse> response) {
                if (response.isSuccessful()) {
                    List<User> userList = response.body().getData();
                    List<User> filteredUserList = new ArrayList<>();

                    for (User user : userList) {
                        if (user.getFirst_name().toLowerCase().contains(searchText.toLowerCase()) ||
                                user.getLast_name().toLowerCase().contains(searchText.toLowerCase()) ||
                                user.getEmail().toLowerCase().contains(searchText.toLowerCase())) {
                            filteredUserList.add(user);
                        }
                    }
                    userAdapter.setUserList(filteredUserList);
                    userAdapter.notifyDataSetChanged();
                } else {
                    Log.e("API Call", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User.SearchResponse> call, Throwable t) {
                Log.e("API Call", "Error: " + t.getMessage());
            }
        });
    }
}
