package com.example.konyu.androidapp_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class BooksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books_layout);
        //------
        Intent intent = getIntent();
        String Token = intent.getStringExtra(MainActivity.EXTRA_Token);
    }
}
