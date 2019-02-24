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
        String login = intent.getStringExtra(MainActivity.EXTRA_login);
        String password = intent.getStringExtra(MainActivity.EXTRA_password);

        TextView text_login = (TextView) findViewById(R.id.textview_login);
        text_login.setText(login);
        TextView text_password = (TextView) findViewById(R.id.textview_password);
        text_password.setText(password);
    }
}
