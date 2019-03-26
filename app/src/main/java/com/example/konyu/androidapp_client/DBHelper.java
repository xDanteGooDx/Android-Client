package com.example.konyu.androidapp_client;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "bookDb";

    public static final String TABLE_BOOK = "book";
    public static final String BOOK_ID = "_id";
    public static final String BOOK_TITLE = "title";
    public static final String BOOK_ICON = "icon";

    public static final String TABLE_FULLTEXT = "full_text";
    public static final String FULLTEXT_ID = "_id";
    public static final String FULLTEXT_HTML = "text_html";
    public static final String FULLTEXT_BOOK = "id_book";

    public static final String TABLE_HEADER = "header";
    public static final String HEADER_ID = "_id";
    public static final String HEADER_BOOK = "id_book";
    public static final String HEADER_TITLE = "title_header";


    public static final String TABLE_TEXT = "t_Text";
    public static final String TEXT_ID = "_id";
    public static final String TEXT_HTML = "text_html";
    public static final String TEXT_HEADER = "id_header";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_BOOK + "(" + BOOK_ID + " integer primary key,"
                + BOOK_TITLE + " text, " + BOOK_ICON + " text)");
        db.execSQL("create table " + TABLE_FULLTEXT + "(" + FULLTEXT_ID + " integer primary key, "
                + FULLTEXT_HTML + " text, " + FULLTEXT_BOOK + " integer)");
        db.execSQL("create table " + TABLE_HEADER + "(" + HEADER_ID + " integer primary key,"
                + HEADER_TITLE + " text," + HEADER_BOOK + " integer)");
        db.execSQL("create table " + TABLE_TEXT + "(" + TEXT_ID + " integer primary key, "
                + TEXT_HTML + " text, " + TEXT_HEADER + " integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_BOOK);
        db.execSQL("drop table if exists " + TABLE_HEADER);
        db.execSQL("drop table if exists " + TABLE_FULLTEXT);
        db.execSQL("drop table if exists " + TABLE_TEXT);

        onCreate(db);
    }
}
