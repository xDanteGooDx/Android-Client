package com.example.konyu.androidapp_client;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import model.Auth;
import model.Login;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);

        (findViewById(R.id.button_registr)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri address = Uri.parse("http://10.0.2.2:8000/student_registration/");
                Intent openlinkIntent = new Intent(Intent.ACTION_VIEW, address);

                startActivity(openlinkIntent);
            }
        });

        (findViewById(R.id.button_signin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
            }
        });

        (findViewById(R.id.button_savedbooks)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SavedBooksActivity.class);
                startActivity(intent);
            }
        });
    }

    public static final String EXTRA_Token = "EXTRA_Token";

    public final static String URL = "http://10.0.2.2:8000";

    public static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    public static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create());

    public static Retrofit retrofit = builder.build();

    public static UserClient userClient = retrofit.create(UserClient.class);

    public void signin() {
        EditText editText_login = (EditText) findViewById(R.id.editText_login);
        String string_login = editText_login.getText().toString();

        EditText editText_password = (EditText) findViewById(R.id.editText_password);
        String string_password = editText_password.getText().toString();

        Call<Auth> call = userClient.login(new Login(string_login, string_password));

        call.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(MainActivity.this, BooksActivity.class);
                    intent.putExtra(EXTRA_Token, response.body().getToken());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "not correct =(", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error =(", Toast.LENGTH_LONG).show();
            }
        });
    }
}

