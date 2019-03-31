package com.example.konyu.androidapp_client;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.NavigableMap;

import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Test_Review extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String EXTRA_TEST_ID = "EXTRA_TEST_ID";
    public static final String EXTRA_TEST_NAME = "EXTRA_TEST_NAME";
    public static final String EXTRA_TEST_AUTHOR = "EXTRA_TEST_AUTHOR";
    public static final String EXTRA_TEST_ABOUT = "EXTRA_TEST_ABOUT";

    TextView nameView, authorView, aboutView;
    Button startBtn;
    Toolbar toolbar;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test__review);

        Intent intent = getIntent();
        final int id_test = intent.getIntExtra(EXTRA_TEST_ID, 0);
        String name_test = intent.getStringExtra(EXTRA_TEST_NAME);
        int author_test = intent.getIntExtra(EXTRA_TEST_AUTHOR, 0);
        String about_test = intent.getStringExtra(EXTRA_TEST_ABOUT);
        token = intent.getStringExtra(MainActivity.EXTRA_Token);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        initView();

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Test_Review.this, Testing.class);
                intent.putExtra(MainActivity.EXTRA_Token, token);
                intent.putExtra(EXTRA_TEST_ID, id_test);
                startActivity(intent);
                finish();
            }
        });

        initToolbar();

        nameView.setText(name_test);
        aboutView.setText(about_test);

        Call<User> call = MainActivity.userClient.getUsers("token " + token, author_test);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    authorView.setText("@" + response.body().getUsername());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void initView() {
        nameView = (TextView) findViewById(R.id.text_name_test);
        authorView = (TextView) findViewById(R.id.text_author_test);
        aboutView = (TextView) findViewById(R.id.text_about_test);
        startBtn = (Button) findViewById(R.id.start_test_btn);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Тест");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.menu_item_books:
                Intent intent = new Intent(this, BooksActivity.class);
                intent.putExtra(MainActivity.EXTRA_Token, token);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_item_tests:
                Intent intent1 = new Intent(this, TestActivity.class);
                intent1.putExtra(MainActivity.EXTRA_Token, token);
                startActivity(intent1);
                finish();
                break;
            case R.id.menu_item_save_books:
                Intent intent2 = new Intent(this, SavedBooksActivity.class);
                intent2.putExtra(MainActivity.EXTRA_Token, token);
                startActivity(intent2);
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
