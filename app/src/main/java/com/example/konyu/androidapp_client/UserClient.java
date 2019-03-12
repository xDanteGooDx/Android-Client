package com.example.konyu.androidapp_client;

import java.util.List;

import model.Auth;
import model.Auth_header;
import model.Book;
import model.FullText;
import model.Login;
import model.Test;
import model.Text;
import model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserClient {

    @POST("/api/login")
    Call<Auth> login(
            @Body Login login);

    @GET("/api/book")
    Call<List<Book>> getBooks(@Header("Authorization") String auth_header);

    @GET("/api/user/{num}")
    Call<User> getUsers(@Header("Authorization") String auth_header, @Path("num") int num);

    @GET("api/getfulltext/{num}")
    Call<List<FullText>> getFullText(@Header("Authorization") String auth_token, @Path("num") int num);

    @GET("api/getheader/{num}")
    Call<List<model.Header>> getHeader(@Header("Authorization") String auth_token, @Path("num") int num);

    @GET("api/gettext/{num}")
    Call<List<Text>> getText(@Header("Authorization") String auth_token, @Path("num") int num);

    @GET("api/test")
    Call<List<Test>> getTests(@Header("Authorization") String auth_token);
}
