package com.example.konyu.androidapp_client;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.Book;
import model.Test;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TestActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    Toolbar toolbar;
    RecyclerView recyclerView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TestAdapter testAdapter;

    private final String URL = "http://10.0.2.2:8000";

    private Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create());

    private Retrofit retrofit = builder.build();

    UserClient userClient = retrofit.create(UserClient.class);

    private String token;
    private List<Test> list_tests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = getIntent();
        String Token = intent.getStringExtra(MainActivity.EXTRA_Token);
        token = Token;

        list_tests = new ArrayList<>();
        initToolbar();
        initNavigationView();
        recyclerView = (RecyclerView) findViewById(R.id.test_recyclerview);
        download_tests(Token);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        testAdapter = new TestAdapter((ArrayList<Test>) list_tests, Token);
        recyclerView.setAdapter(testAdapter);
    }

    private void download_tests(final String token) {
        Call<List<Test>> call = userClient.getTests("token " + token);

        call.enqueue(new Callback<List<Test>>() {
            @Override
            public void onResponse(Call<List<Test>> call, Response<List<Test>> response) {
                if (response.isSuccessful()) {
                    list_tests = new ArrayList<>();
                    if (response.body() != null) {
                        list_tests.addAll(response.body());
                        Collections.reverse(list_tests);
                        recyclerView.setAdapter(new TestAdapter(list_tests, token));
                    }
                } else {
                    Toast.makeText(TestActivity.this, "Не удалось загрузить тесты", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Test>> call, Throwable t) {
                Toast.makeText(TestActivity.this, "Не удалось загрузить тесты", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Тесты");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.menu);

        Menu menu = toolbar.getMenu();
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (s.length() == 0 && recyclerView.getAdapter().getItemCount() != list_tests.size()) {
            recyclerView.setAdapter(new TestAdapter(list_tests, token));
        } else {
            List<Test> newListOfBook = new ArrayList<>();
            for (int i = 0; i < list_tests.size(); i++) {
                if (list_tests.get(i).getTest_title().indexOf(s) != -1) {
                    newListOfBook.add(list_tests.get(i));
                }
                recyclerView.setAdapter(new TestAdapter(newListOfBook, token));
            }
        }

        return false;
    }
}
