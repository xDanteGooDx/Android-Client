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

import java.util.ArrayList;
import java.util.List;

import model.Book;

public class SavedBooksActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    SavedBooksAdapter savedBooksAdapter;
    RecyclerView recyclerView;
    String token;
    List<Book> list_books;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        savedBooksAdapter = new SavedBooksAdapter(this);
        recyclerView.setAdapter(savedBooksAdapter);
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Сохраненные книги");
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
            case R.id.menu_item_save_books:
                Intent intent2 = new Intent(this, SavedBooksActivity.class);
                startActivity(intent2);
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
