package com.example.konyu.androidapp_client;

import model.Auth;
import model.Login;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserClient {

    @POST("/api/login")
    Call<Auth> login(
            @Body Login login);
}
