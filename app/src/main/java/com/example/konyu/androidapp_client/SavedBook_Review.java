package com.example.konyu.androidapp_client;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import java.util.ArrayList;
import java.util.List;

import model.Header;
import model.Text;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedBook_Review extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    SQLiteDatabase db;
    DBHelper dbHelper;

    NavigationView navigationView;
    WebView webView;
    Toolbar toolbar;
    Menu menu;

    private String token;
    private String fulltext;
    private String book_title;
    private List<Header> headers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__review);

        Intent intent = getIntent();
        int id_book = intent.getIntExtra(Book_Review.EXTRA_BOOK_ID, 0);
        token = intent.getStringExtra(MainActivity.EXTRA_Token);

        headers = new ArrayList<>();

        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();

        String query = "SELECT " + DBHelper.BOOK_TITLE + " FROM " + DBHelper.TABLE_BOOK;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(DBHelper.BOOK_TITLE);

            book_title = cursor.getString(index);
        }

        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        webView = findViewById(R.id.web_view);

        query = "SELECT * FROM " + DBHelper.TABLE_FULLTEXT + " WHERE "
                + DBHelper.FULLTEXT_BOOK + " = " + id_book;

        cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(DBHelper.FULLTEXT_HTML);
            fulltext = cursor.getString(index);
            webView.loadUrl("file://"+fulltext);
        }

        query = "SELECT * FROM " + DBHelper.TABLE_HEADER + " WHERE " + DBHelper.HEADER_BOOK
                + " = " + id_book;

        cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int header_id_index = cursor.getColumnIndex(DBHelper.HEADER_ID);
            int header_title_index = cursor.getColumnIndex(DBHelper.HEADER_TITLE);

            do {
                headers.add(new Header(cursor.getInt(header_id_index), 0, cursor.getString(header_title_index)));

            } while (cursor.moveToNext());
        }

        menu = navigationView.getMenu();
        MenuItem menuItem = menu.add(Menu.NONE, 1, Menu.NONE, "Текст целиком");
        menuItem.setIcon(R.drawable.chevron_right);
        for (int i = 0; i < headers.size(); i++) {
            MenuItem add = menu.add(Menu.NONE, headers.get(i).getId(), Menu.NONE, headers.get(i).getText_header()
                    .replace("\t", "").replace("\n", ""));
            add.setIcon(R.drawable.chevron_right);
        }

        initToolbar();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(book_title);
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
            case 1:
                webView.loadUrl("file://" + fulltext);
                break;

        }
        for (int i = 0; i < headers.size(); i++) {
            if (id == headers.get(i).getId()) {
                String query = "SELECT * FROM " + DBHelper.TABLE_TEXT + " WHERE "
                        + DBHelper.TEXT_HEADER + " = " + headers.get(i).getId();

                Cursor cursor = db.rawQuery(query, null);

                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(DBHelper.TEXT_HTML);

                    webView.loadUrl("file://" + cursor.getString(index));
                }
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
