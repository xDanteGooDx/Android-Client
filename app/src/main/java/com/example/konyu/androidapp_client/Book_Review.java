package com.example.konyu.androidapp_client;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.webkit.WebView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.FullText;
import model.Header;
import model.Text;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Book_Review extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_BOOK_ID = "EXTRA_BOOK_ID";
    public static final String EXTRA_BOOK_NAME = "EXTRA_BOOK_NAME";
    NavigationView navigationView;
    FullText fullText;
    WebView webView;
    MenuItem menuItem_books;
    Menu menu;
    String fulltext_url;
    List<Header> headers;

    private String book_title;
    private Toolbar toolbar;

    private final String URL = "http://10.0.2.2:8000";

    private Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create());

    private Retrofit retrofit = builder.build();

    UserClient userClient = retrofit.create(UserClient.class);

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__review);

        Intent intent = getIntent();
        int id_book = intent.getIntExtra(EXTRA_BOOK_ID, 0);
        book_title = intent.getStringExtra(EXTRA_BOOK_NAME);
        token = intent.getStringExtra(MainActivity.EXTRA_Token);

        headers = new ArrayList<>();

        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        webView = findViewById(R.id.web_view);
        menuItem_books = findViewById(R.id.menu_item_books);

        initToolbar();

        Call<List<FullText>> call = userClient.getFullText("token " + token, id_book);

        call.enqueue(new Callback<List<FullText>>() {
            @Override
            public void onResponse(Call<List<FullText>> call, Response<List<FullText>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() == 1) {
                        fullText = response.body().get(0);
                        String new_URL = URL + fullText.getText_html();
                        fulltext_url = new_URL;
                        webView.loadUrl(new_URL);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<FullText>> call, Throwable t) {

            }
        });

        Call<List<Header>> call_header = userClient.getHeader("token " + token, id_book);

        call_header.enqueue(new Callback<List<Header>>() {
            @Override
            public void onResponse(Call<List<Header>> call, Response<List<Header>> response) {
                if (response.isSuccessful()) {
                    headers.addAll(response.body());
                    menu = navigationView.getMenu();
                    menu.add(Menu.NONE, 1, Menu.NONE, "Текст целиком");
                    for (int i = 0; i < response.body().size(); i++) {
                        MenuItem add = menu.add(Menu.NONE, response.body().get(i).getId(), Menu.NONE, response.body().get(i).getText_header()
                                .replace("\t", "").replace("\n", ""));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Header>> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.menu_item_books:
                Intent intent = new Intent(this, BooksActivity.class);
                intent.putExtra(MainActivity.EXTRA_Token, token);
                startActivity(intent);
//                finish();
                break;
            case R.id.menu_item_tests:
                Intent intent1 = new Intent(this, TestActivity.class);
                intent1.putExtra(MainActivity.EXTRA_Token, token);
                startActivity(intent1);
                finish();
                break;
            case 1:
                webView.loadUrl(fulltext_url);
                break;

        }
        for (int i = 0; i < headers.size(); i++) {
            if (id == headers.get(i).getId()) {
                Call<List<Text>> call = userClient.getText("token " + token, headers.get(i).getId());

                call.enqueue(new Callback<List<Text>>() {
                    @Override
                    public void onResponse(Call<List<Text>> call, Response<List<Text>> response) {
                        if (response.isSuccessful()) {
                            if (response.body().size() == 1) {
                                webView.loadUrl(URL + response.body().get(0).getText_html());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Text>> call, Throwable t) {

                    }
                });
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(book_title);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.menu_save);
    }
}
