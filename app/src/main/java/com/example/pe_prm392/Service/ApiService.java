package com.example.pe_prm392.Service;

import com.example.pe_prm392.Models.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("users")
    Call<User.UsersResponse> getUsers(@Query("page") int page);

    @GET("users/{id}")
    Call<User.UserDetailResponse> getUserDetails(@Path("id") int userId);

    @GET("users")
    Call<User.SearchResponse> searchUsers(@Query("name") String name, @Query("email") String email);
}
