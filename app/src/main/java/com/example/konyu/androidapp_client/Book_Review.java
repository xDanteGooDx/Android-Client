package com.example.konyu.androidapp_client;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.Book;
import model.FullText;
import model.Header;
import model.Resource;
import model.Text;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private int id_book;
    private String book_title;
    private Toolbar toolbar;

    private String token;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__review);

        Intent intent = getIntent();
        id_book = intent.getIntExtra(EXTRA_BOOK_ID, 0);
        book_title = intent.getStringExtra(EXTRA_BOOK_NAME);
        token = intent.getStringExtra(MainActivity.EXTRA_Token);

        headers = new ArrayList<>();

        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        webView = findViewById(R.id.web_view);
        menuItem_books = findViewById(R.id.menu_item_books);

        initToolbar();

        Call<List<FullText>> call = MainActivity.userClient.getFullText("token " + token, id_book);

        call.enqueue(new Callback<List<FullText>>() {
            @Override
            public void onResponse(Call<List<FullText>> call, Response<List<FullText>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() == 1) {
                        fullText = response.body().get(0);
                        String new_URL = MainActivity.URL + fullText.getText_html();
                        fulltext_url = new_URL;
                        webView.loadUrl(new_URL);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<FullText>> call, Throwable t) {

            }
        });

        Call<List<Header>> call_header = MainActivity.userClient.getHeader("token " + token, id_book);

        call_header.enqueue(new Callback<List<Header>>() {
            @Override
            public void onResponse(Call<List<Header>> call, Response<List<Header>> response) {
                if (response.isSuccessful()) {
                    headers.addAll(response.body());
                    menu = navigationView.getMenu();
                    MenuItem menuItem = menu.add(Menu.NONE, 1, Menu.NONE, "Текст целиком");
                    menuItem.setIcon(R.drawable.chevron_right);
                    for (int i = 0; i < response.body().size(); i++) {
                        MenuItem add = menu.add(Menu.NONE, response.body().get(i).getId(), Menu.NONE, response.body().get(i).getText_header()
                                .replace("\t", "").replace("\n", ""));
                        add.setIcon(R.drawable.chevron_right);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Header>> call, Throwable t) {

            }
        });
        dbHelper = new DBHelper(this);
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
            case 1:
                webView.loadUrl(fulltext_url);
                break;

        }
        for (int i = 0; i < headers.size(); i++) {
            if (id == headers.get(i).getId()) {
                Call<List<Text>> call = MainActivity.userClient.getText("token " + token, headers.get(i).getId());

                call.enqueue(new Callback<List<Text>>() {
                    @Override
                    public void onResponse(Call<List<Text>> call, Response<List<Text>> response) {
                        if (response.isSuccessful()) {
                            if (response.body().size() == 1) {
                                webView.loadUrl(MainActivity.URL + response.body().get(0).getText_html());
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
                switch (menuItem.getItemId()) {
                    case R.id.savemenu:
                        SQLiteDatabase db = dbHelper.getReadableDatabase();
                        String query = "SELECT " + DBHelper.BOOK_ID + " FROM " + DBHelper.TABLE_BOOK
                                + " WHERE " + DBHelper.BOOK_ID + " = " + id_book;
                        Cursor cursor = db.rawQuery(query, null);

                        if(cursor.moveToFirst()){
                            Toast.makeText(Book_Review.this, "Книга уже скачена", Toast.LENGTH_SHORT).show();
                            break;
                        }

                        Call<Book> call = MainActivity.userClient.getBook("token " + token, id_book);

                        call.enqueue(new Callback<Book>() {
                            @Override
                            public void onResponse(Call<Book> call, Response<Book> response) {
                                if (response.isSuccessful()) {
                                    final SQLiteDatabase database = dbHelper.getWritableDatabase();
                                    final ContentValues contentValues = new ContentValues();

                                    contentValues.put(DBHelper.BOOK_ID, id_book);
                                    contentValues.put(DBHelper.BOOK_TITLE, book_title);
                                    final String path = getApplicationInfo().dataDir + "/image";
                                    final String name_of_file = UUID.randomUUID().toString() + ".jpeg";
                                    final String Full_path = path + "/" + name_of_file;
                                    Target target = new Target() {
                                        @Override
                                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                            try {
                                                File file = new File(path);
                                                if (!file.exists()) {
                                                    file.mkdir();
                                                }
                                                file = new File(Full_path);
                                                file.createNewFile();
                                                FileOutputStream ostream = new FileOutputStream(file);
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                                                ostream.close();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                        }

                                        @Override
                                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                                        }
                                    };

                                    Picasso.get().load(response.body().getIcon_book()).into(target);
                                    contentValues.put(DBHelper.BOOK_ICON, Full_path);
                                    database.insert(DBHelper.TABLE_BOOK, null, contentValues);

                                    //скачивание fulltext
                                    Call<List<FullText>> call_fulltext = MainActivity.userClient.getFullText("token " + token, id_book);

                                    call_fulltext.enqueue(new Callback<List<FullText>>() {
                                        @Override
                                        public void onResponse(Call<List<FullText>> call, Response<List<FullText>> response) {
                                            if (response.isSuccessful()) {
                                                ContentValues contentValues1 = new ContentValues();
                                                contentValues1.put(DBHelper.FULLTEXT_ID, response.body().get(0).getId());
                                                contentValues1.put(DBHelper.FULLTEXT_BOOK, response.body().get(0).getId_book());
                                                File file = new File(getApplicationInfo().dataDir + "/html");
                                                if (!file.exists()) {
                                                    file.mkdir();
                                                }
                                                final String path_to_file = getApplicationInfo().dataDir + "/html/" + response.body().get(0).getText_html().substring(response.body().get(0).getText_html().lastIndexOf('/'), response.body().get(0).getText_html().length());

                                                Call<ResponseBody> call_download = MainActivity.userClient.downloadFile("token " + token, MainActivity.URL + response.body().get(0).getText_html());

                                                call_download.enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                                                        if (response.isSuccessful()) {
                                                            new AsyncTask<Void, Void, Void>() {

                                                                @Override
                                                                protected Void doInBackground(Void... voids) {
                                                                    boolean writtenToDisk = writeResponseBodyToDisk(response.body(), path_to_file);
                                                                    return null;
                                                                }
                                                            }.execute();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                    }
                                                });

                                                contentValues1.put(DBHelper.FULLTEXT_HTML, path_to_file);
                                                database.insert(DBHelper.TABLE_FULLTEXT, null, contentValues1);

                                                //скачивание ресурсов
                                                Call<List<Resource>> call_resource = MainActivity.userClient.getResource("token " + token, id_book);

                                                call_resource.enqueue(new Callback<List<Resource>>() {
                                                    @Override
                                                    public void onResponse(Call<List<Resource>> call, Response<List<Resource>> response) {
                                                        if (response.isSuccessful()) {
                                                            for (int i = 0; i < response.body().size(); i++) {
                                                                final String path_to_resource = path_to_file.substring(0, path_to_file.lastIndexOf('.')) + ".files";
                                                                File file1 = new File(path_to_resource);
                                                                if (!file1.exists()) {
                                                                    file1.mkdir();
                                                                }
                                                                final String path_res = path_to_resource + "/" + response.body().get(i).getPath().substring(response.body().get(i).getPath().lastIndexOf('/'));
                                                                Call<ResponseBody> responseBodyCall = MainActivity.userClient.downloadFile("token " + token, MainActivity.URL + response.body().get(i).getPath());

                                                                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                                                                    @Override
                                                                    public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                                                                        if (response.isSuccessful()) {
                                                                            new AsyncTask<Void, Void, Void>() {

                                                                                @Override
                                                                                protected Void doInBackground(Void... voids) {
                                                                                    boolean writtenToDisk = writeResponseBodyToDisk(response.body(), path_res);
                                                                                    return null;
                                                                                }
                                                                            }.execute();
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                                    }
                                                                });

                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<List<Resource>> call, Throwable t) {

                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<List<FullText>> call, Throwable t) {

                                        }
                                    });
                                    //загрузка глав
                                    Call<List<Header>> call_header = MainActivity.userClient.getHeader("token " + token, id_book);

                                    call_header.enqueue(new Callback<List<Header>>() {
                                        @Override
                                        public void onResponse(Call<List<Header>> call, Response<List<Header>> response) {
                                            if (response.isSuccessful()) {
                                                for (int i = 0; i < response.body().size(); i++) {
                                                    ContentValues contentValues1 = new ContentValues();
                                                    contentValues1.put(DBHelper.HEADER_ID, response.body().get(i).getId());
                                                    contentValues1.put(DBHelper.HEADER_TITLE, response.body().get(i).getText_header());
                                                    contentValues1.put(DBHelper.HEADER_BOOK, response.body().get(i).getId_book());
                                                    database.insert(DBHelper.TABLE_HEADER, null, contentValues1);

                                                    Call<List<Text>> call_text = MainActivity.userClient.getText("token " + token, response.body().get(i).getId());

                                                    call_text.enqueue(new Callback<List<Text>>() {
                                                        @Override
                                                        public void onResponse(Call<List<Text>> call, Response<List<Text>> response) {
                                                            if (response.isSuccessful()) {
                                                                if (response.body().size() == 1) {
                                                                    ContentValues contentValues2 = new ContentValues();
                                                                    contentValues2.put(DBHelper.TEXT_ID, response.body().get(0).getId());
                                                                    contentValues2.put(DBHelper.TEXT_HEADER, response.body().get(0).getId_header());

                                                                    final String path_to_file = getApplicationInfo().dataDir + "/html/" + UUID.randomUUID().toString() + ".html";

                                                                    Call<ResponseBody> call_download = MainActivity.userClient.downloadFile("token " + token, MainActivity.URL + response.body().get(0).getText_html());

                                                                    call_download.enqueue(new Callback<ResponseBody>() {
                                                                        @Override
                                                                        public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                                                                            if (response.isSuccessful()) {
                                                                                new AsyncTask<Void, Void, Void>() {

                                                                                    @Override
                                                                                    protected Void doInBackground(Void... voids) {
                                                                                        boolean writtenToDisk = writeResponseBodyToDisk(response.body(), path_to_file);
                                                                                        return null;
                                                                                    }
                                                                                }.execute();
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                                        }
                                                                    });

                                                                    contentValues2.put(DBHelper.TEXT_HTML, path_to_file);
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<List<Text>> call, Throwable t) {

                                                        }
                                                    });
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<List<Header>> call, Throwable t) {

                                        }
                                    });

                                    Toast.makeText(Book_Review.this, "Книга загружена", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Book> call, Throwable t) {

                            }
                        });
                        break;
                }
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.menu_save);
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String path) {
        try {
            File futureStudioIconFile = new File(path);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }

    }
}
