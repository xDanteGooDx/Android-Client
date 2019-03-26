package com.example.konyu.androidapp_client;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import model.Answer;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {
    List<Answer> answers;
    List<Boolean> user_answers;

    public AnswerAdapter(List<Answer> answers, List<Boolean> user_answers) {
        this.answers = answers;
        if(user_answers == null) {
            this.user_answers = new ArrayList<>();
            for (int i = 0; i < answers.size(); i++) {
                this.user_answers.add(false);
            }
        }
        else{
            this.user_answers = user_answers;
        }
    }

    public class AnswerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        CheckBox checkBox;
        int num_Item;

        public void set_Num_Item(int i){
            num_Item = i;
        }

        public AnswerViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            textView = (TextView) view.findViewById(R.id.text_answer);
            checkBox = (CheckBox) view.findViewById(R.id.checkbox_answer);
        }

        @Override
        public void onClick(View v) {
            checkBox.setChecked(!checkBox.isChecked());
            user_answers.set(num_Item, checkBox.isChecked());
        }
    }

    @NonNull
    @Override
    public AnswerAdapter.AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_answer, viewGroup, false);
        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AnswerAdapter.AnswerViewHolder answerViewHolder, int i) {
        answerViewHolder.set_Num_Item(i);
        answerViewHolder.textView.setText(answers.get(i).getAnswer_text());
        answerViewHolder.checkBox.setChecked(user_answers.get(i));
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public List<Boolean> getAnswers() {
        return user_answers;
    }
}
