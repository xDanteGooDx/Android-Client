package com.example.konyu.androidapp_client;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import model.Book;
import model.Test;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {
    private final String URL = "http://10.0.2.2:8000";

    private Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create());

    private Retrofit retrofit = builder.build();

    UserClient userClient = retrofit.create(UserClient.class);

    List<Test> tests;
    String token;

    public TestAdapter(List<Test> tests, String token){
        this.tests = tests;
        this.token = token;
    }

    public class TestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title_view, about_view;
        Test test_Item;

        public void setTest_Item(Test test){
            this.test_Item = test;
        }

        public TestViewHolder(View view){
            super(view);
            view.setOnClickListener(this);

            title_view = (TextView) view.findViewById(R.id.text_title_test);
            about_view = (TextView) view.findViewById(R.id.text_about_test);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), Test_Review.class);
            intent.putExtra(MainActivity.EXTRA_Token, token);
            intent.putExtra(Test_Review.EXTRA_TEST_ID, test_Item.getId());
            intent.putExtra(Test_Review.EXTRA_TEST_NAME, test_Item.getTest_title());
            intent.putExtra(Test_Review.EXTRA_TEST_AUTHOR, test_Item.getAuthor());
            intent.putExtra(Test_Review.EXTRA_TEST_ABOUT,test_Item.getAbout());
            v.getContext().startActivity(intent);
        }
    }

    @NonNull
    @Override
    public TestAdapter.TestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_test, viewGroup, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TestAdapter.TestViewHolder testViewHolder, int i){
        testViewHolder.setTest_Item(tests.get(i));
        testViewHolder.title_view.setText(tests.get(i).getTest_title());
        testViewHolder.about_view.setText(tests.get(i).getAbout());
    }

    @Override
    public int getItemCount(){
        return tests.size();
    }
}
