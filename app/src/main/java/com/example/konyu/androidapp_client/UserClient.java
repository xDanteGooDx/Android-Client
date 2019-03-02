package com.example.konyu.androidapp_client;

import java.util.List;

import model.Auth;
import model.Auth_header;
import model.Book;
import model.Login;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserClient {

    @POST("/api/login")
    Call<Auth> login(
            @Body Login login);

    @GET("/api/book")
    Call<List<Book>> getBooks(@Header("Authorization") String auth_header);
}
