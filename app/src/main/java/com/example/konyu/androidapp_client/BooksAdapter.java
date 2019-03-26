package com.example.konyu.androidapp_client;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import model.Book;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    List<Book> books;
    String token;

    public BookAdapter(List<Book> books, String token) {
        this.books = books;
        this.token = token;
    }

    public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView author_view, title_view;
        Book book_Item;

        public void setBook_Item(Book book){
            book_Item = book;
        }

        public BookViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            imageView = (ImageView) view.findViewById(R.id.icon_book);
            author_view = (TextView) view.findViewById(R.id.author_book);
            title_view = (TextView) view.findViewById(R.id.title_book);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), Book_Review.class);
            intent.putExtra(MainActivity.EXTRA_Token, token );
            intent.putExtra(Book_Review.EXTRA_BOOK_ID, book_Item.getId());
            intent.putExtra(Book_Review.EXTRA_BOOK_NAME, book_Item.getTitle_book());
            v.getContext().startActivity(intent);
        }
    }

    @NonNull
    @Override
    public BookAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_book, viewGroup, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookAdapter.BookViewHolder bookViewHolder, int i) {
        bookViewHolder.setBook_Item(books.get(i));
        bookViewHolder.title_view.setText(books.get(i).getTitle_book());
        Call<User> call = MainActivity.userClient.getUsers("token " + token, books.get(i).getAuthor());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    bookViewHolder.author_view.setText(response.body().getUsername());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
        Picasso.get().load(books.get(i).getIcon_book()).into(bookViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

}