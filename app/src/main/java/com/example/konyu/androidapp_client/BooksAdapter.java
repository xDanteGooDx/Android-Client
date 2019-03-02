package com.example.konyu.androidapp_client;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import model.Book;

class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    List<Book> books;
    String token;

    public BookAdapter(List<Book> books, String token) {
        this.books = books;
        this.token = token;
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView author_view, title_view;

        public BookViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.icon_book);
            author_view = (TextView) view.findViewById(R.id.author_book);
            title_view = (TextView) view.findViewById(R.id.title_book);
        }
    }

    @NonNull
    @Override
    public BookAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_book, viewGroup, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.BookViewHolder bookViewHolder, int i) {
        bookViewHolder.title_view.setText(books.get(i).getTitle_book());
        bookViewHolder.author_view.setText(Integer.toString(books.get(i).getAuthor()));

        Picasso.get().load(books.get(i).getIcon_book()).into(bookViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

}