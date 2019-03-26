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
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import model.Answer;
import model.Question;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Testing extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    TextView title_of_test;
    AnswerAdapter answerAdapter;
    Button nextBtn, backBtn, finishBtn;
    Toolbar toolbar;

    String token;
    List<Question> questions;
    List<List<Answer>> answers;
    List<List<Boolean>> user_answers;
    int num_of_question; //текущий вопрос

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        Intent intent = getIntent();
        int id_test = intent.getIntExtra(Test_Review.EXTRA_TEST_ID, 0);
        token = intent.getStringExtra(MainActivity.EXTRA_Token);

        answers = new ArrayList<>();
        user_answers = new ArrayList<>();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        initView();
        num_of_question = 0;

        backBtn.setVisibility(View.GONE);
        nextBtn.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Call<List<Question>> call = MainActivity.userClient.getQuestion("token " + token, id_test);

        call.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful()) {
                    questions = response.body();
                    title_of_test.setText(questions.get(num_of_question).getQuestion_text());

                    if (questions.size() > 1) {
                        nextBtn.setVisibility(View.VISIBLE);
                    }
                    toolbar.setTitle("Вопрос " + Integer.toString(num_of_question + 1) + "/" + Integer.toString(questions.size()));
                    Call<List<Answer>> call_answer = MainActivity.userClient
                            .getAnswer("token " + token, questions.get(num_of_question).getId());

                    call_answer.enqueue(new Callback<List<Answer>>() {
                        @Override
                        public void onResponse(Call<List<Answer>> call, Response<List<Answer>> response) {
                            if (response.isSuccessful()) {
                                answers.add(new ArrayList<Answer>(response.body()));
                                answerAdapter = new AnswerAdapter(response.body(), null);
                                recyclerView.setAdapter(answerAdapter);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Answer>> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {

            }
        });
    }

    private void initView() {
        title_of_test = (TextView) findViewById(R.id.question_text);
        recyclerView = (RecyclerView) findViewById(R.id.answer_recyclerview);
        backBtn = (Button) findViewById(R.id.back_btn);
        nextBtn = (Button) findViewById(R.id.next_btn);
        finishBtn = (Button) findViewById(R.id.finish_btn);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_answers.size() == num_of_question) {
                    user_answers.add(answerAdapter.getAnswers());
                } else {
                    user_answers.set(num_of_question, answerAdapter.getAnswers());
                }
                num_of_question += 1;
                if (num_of_question == 1) {
                    backBtn.setVisibility(View.VISIBLE);
                }
                if (num_of_question + 1 == questions.size()) {
                    nextBtn.setVisibility(View.GONE);
                }

                title_of_test.setText(questions.get(num_of_question).getQuestion_text());
                if (answers.size() == num_of_question) {
                    Call<List<Answer>> call = MainActivity.userClient
                            .getAnswer("token " + token, questions.get(num_of_question).getId());

                    call.enqueue(new Callback<List<Answer>>() {
                        @Override
                        public void onResponse(Call<List<Answer>> call, Response<List<Answer>> response) {
                            if (response.isSuccessful()) {
                                answers.add(new ArrayList<Answer>(response.body()));
                                answerAdapter = new AnswerAdapter(response.body(), null);
                                recyclerView.setAdapter(answerAdapter);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Answer>> call, Throwable t) {

                        }
                    });
                } else {
                    answerAdapter = new AnswerAdapter(answers.get(num_of_question), user_answers.get(num_of_question));
                    recyclerView.setAdapter(answerAdapter);
                }
                toolbar.setTitle("Вопрос " + Integer.toString(num_of_question + 1) + "/" + Integer.toString(questions.size()));
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num_of_question == user_answers.size()) {
                    user_answers.add(answerAdapter.getAnswers());
                } else {
                    user_answers.set(num_of_question, answerAdapter.getAnswers());
                }
                num_of_question -= 1;
                if (num_of_question == 0) {
                    backBtn.setVisibility(View.GONE);
                }
                if (num_of_question == questions.size() - 2) {
                    nextBtn.setVisibility(View.VISIBLE);
                }
                title_of_test.setText(questions.get(num_of_question).getQuestion_text());
//                Call<List<Answer>> call = MainActivity.userClient
//                        .getAnswer("token " + token, questions.get(num_of_question).getId());
//
//                call.enqueue(new Callback<List<Answer>>() {
//                    @Override
//                    public void onResponse(Call<List<Answer>> call, Response<List<Answer>> response) {
//                        if (response.isSuccessful()) {
//                            answerAdapter = new AnswerAdapter(response.body());
//                            recyclerView.setAdapter(answerAdapter);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<Answer>> call, Throwable t) {
//
//                    }
//                });

                answerAdapter = new AnswerAdapter(answers.get(num_of_question), user_answers.get(num_of_question));
                recyclerView.setAdapter(answerAdapter);
                toolbar.setTitle("Вопрос " + Integer.toString(num_of_question + 1) + "/" + Integer.toString(questions.size()));
            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num_of_question == user_answers.size()) {
                    user_answers.add(answerAdapter.getAnswers());
                } else {
                    user_answers.set(num_of_question, answerAdapter.getAnswers());
                }
                int score = 0;
                int count = 0;
                for (int i = 0; i < questions.size(); i++) {
                    if (answers.size() <= i) {
                        break;
                    }
                    count = 0;
                    for (int j = 0; j < answers.get(i).size(); j++) {
                        if (!(answers.get(i).get(j).isIs_right() == user_answers.get(i).get(j))) {
                            break;
                        }
                        count += 1;
                    }
                    if (count == answers.get(i).size()) {
                        score += 1;
                    }
                }

                Toast.makeText(Testing.this, "Вы ответили на " + Integer.toString(score)
                        + " из " + Integer.toString(questions.size()), Toast.LENGTH_SHORT).show();
            }
        });
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}