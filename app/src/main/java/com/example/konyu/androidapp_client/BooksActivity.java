package com.example.konyu.androidapp_client;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import model.Book;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BooksActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private BookAdapter bookAdapter;

    private List<Book> list_books;

    private final String URL = "http://10.0.2.2:8000";

    private Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create());

    private Retrofit retrofit = builder.build();

    UserClient userClient = retrofit.create(UserClient.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books_layout);
        //------
        Intent intent = getIntent();
        String Token = intent.getStringExtra(MainActivity.EXTRA_Token);
        list_books = new ArrayList<>();
        list_books.add(new Book(1, "Титл", 1, "http://wiki.kubg.edu.ua/images/d/d7/F11.jpg"));
        initToolbar();
        initNavigationView();

//        TextView test_author = findViewById(R.id.test_author_book);
//        TextView test_title = findViewById(R.id.test_title_book);
//        ImageView test_icon = findViewById(R.id.test_icon_book);
//
//        test_author.setText(Integer.toString(list_books.get(0).getAuthor()));
//        test_title.setText(list_books.get(0).getTitle_book());
//        new DownloadImageTask(test_icon).execute(list_books.get(0).getIcon_book());
//        Picasso.get().load(list_books.get(0).getIcon_book()).into(test_icon);
        recyclerView = (RecyclerView) findViewById(R.id.book_recyclerview);
        download_books(Token);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter((ArrayList<Book>) list_books, "token " + Token);
        recyclerView.setAdapter(bookAdapter);
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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
    }

    private void download_books(final String token) {
        Call<List<Book>> call = userClient.getBooks("token " + token);

        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    list_books = new ArrayList<Book>();
//                    list_books.clear();
                    if (response.body() != null) {
                        list_books.addAll(response.body());
                        recyclerView.setAdapter(new BookAdapter(list_books, "token " + token));
//                        recyclerView.getAdapter().notifyDataSetChanged();
//                        bookAdapter.notifyDataSetChanged();
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
}
