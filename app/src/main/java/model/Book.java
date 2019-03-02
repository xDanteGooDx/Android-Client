package model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Book {
    private int id;
    private String title_book;
    private int author;
    private String icon_book;

    public Book(int id, String title_book, int author, String icon_book){
        this.id = id;
        this.title_book = title_book;
        this.author = author;
        this.icon_book = icon_book;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle_book() {
        return title_book;
    }

    public void setTitle_book(String title_book) {
        this.title_book = title_book;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public String getIcon_book() {
        return icon_book;
    }

    public void setIcon_book(String icon_book) {
        this.icon_book = icon_book;
    }


}
