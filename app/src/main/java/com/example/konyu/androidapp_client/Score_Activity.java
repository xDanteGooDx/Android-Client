package com.example.konyu.androidapp_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.konyu.androidapp_client.MainActivity.userClient;

public class Score_Activity extends AppCompatActivity {
    public final static String EXTRA_ALLSCORE = "EXTRA_all_score";
    public final static String EXTRA_SCORE = "EXTRA_score";

    Button btn;
    TextView textView;

    String token;
    int score, all_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_);

        final Intent intent = getIntent();
        token = intent.getStringExtra(MainActivity.EXTRA_Token);
        score = intent.getIntExtra(EXTRA_SCORE, 0);
        all_score = intent.getIntExtra(EXTRA_ALLSCORE, 0);

        btn = findViewById(R.id.btn_end);
        textView = findViewById(R.id.tv_score);
        textView.setText("Ваш результат " + Integer.toString(score) + " из " + Integer.toString(all_score));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(v.getContext(), TestActivity.class);
                intent1.putExtra(MainActivity.EXTRA_Token, token);
                startActivity(intent1);
                finish();
            }
        });
    }
}
