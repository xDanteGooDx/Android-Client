package com.example.konyu.androidapp_client;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import model.Book;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BooksActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private BookAdapter bookAdapter;
    private ActionBarDrawerToggle mDrawerToggle;

    private List<Book> list_books;

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books_layout);

        Intent intent = getIntent();
        String Token = intent.getStringExtra(MainActivity.EXTRA_Token);
        token = Token;
        list_books = new ArrayList<>();
        initToolbar();
        initNavigationView();
        recyclerView = (RecyclerView) findViewById(R.id.book_recyclerview);
        download_books(Token);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter((ArrayList<Book>) list_books, Token);
        recyclerView.setAdapter(bookAdapter);
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Книги");
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

//        SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setOnQueryTextListener(this);
    }

    private void download_books(final String token) {
        Call<List<Book>> call = MainActivity.userClient.getBooks("token " + token);

        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    list_books = new ArrayList<Book>();
                    if (response.body() != null) {
                        list_books.addAll(response.body());
                        Collections.reverse(list_books);
                        recyclerView.setAdapter(new BookAdapter(list_books, token));
                    }
                } else {
                    Toast.makeText(BooksActivity.this, "Не удалось загрузить книги", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Toast.makeText(BooksActivity.this, "Не удалось загрузить книги", Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
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
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() == 0 && recyclerView.getAdapter().getItemCount() != list_books.size()) {
            recyclerView.setAdapter(new BookAdapter(list_books, token));
        } else {
            List<Book> newListOfBook = new ArrayList<>();
            for (int i = 0; i < list_books.size(); i++) {
                if (list_books.get(i).getTitle_book().indexOf(newText) != -1) {
                    newListOfBook.add(list_books.get(i));
                }
                recyclerView.setAdapter(new BookAdapter(newListOfBook, token));
            }
        }

        return false;
    }
}
