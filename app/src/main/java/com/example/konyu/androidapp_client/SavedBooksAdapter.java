package com.example.konyu.androidapp_client;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import model.Book;

class SavedBooksAdapter extends RecyclerView.Adapter<SavedBooksAdapter.SavedBookViewHolder> {
    SQLiteDatabase db;
    DBHelper dbHelper;
    List<Book> books;
    String token;

    public SavedBooksAdapter(Context context) {
        dbHelper = new DBHelper(context);
        db = dbHelper.getReadableDatabase();
        String query = "Select * FROM " + DBHelper.TABLE_BOOK;

        Cursor cursor = db.rawQuery(query, null);

        books = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.BOOK_ID);
            int titleIndex = cursor.getColumnIndex(DBHelper.BOOK_TITLE);
            int iconIndex = cursor.getColumnIndex(DBHelper.BOOK_ICON);
            do {
                books.add(new Book(cursor.getInt(idIndex), cursor.getString(titleIndex),
                        0, cursor.getString(iconIndex)));
            }
            while (cursor.moveToNext());
        }
    }

    public class SavedBookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView author_view, title_view;
        Book book_Item;

        public void setBook_Item(Book book) {
            book_Item = book;
        }

        public SavedBookViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            imageView = (ImageView) view.findViewById(R.id.icon_book);
            author_view = (TextView) view.findViewById(R.id.author_book);
            title_view = (TextView) view.findViewById(R.id.title_book);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), SavedBook_Review.class);
            intent.putExtra(Book_Review.EXTRA_BOOK_ID, book_Item.getId());
            v.getContext().startActivity(intent);
        }
    }

    @NonNull
    @Override
    public SavedBooksAdapter.SavedBookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_book, viewGroup, false);
        return new SavedBooksAdapter.SavedBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedBookViewHolder savedBookViewHolder, int i) {
        savedBookViewHolder.setBook_Item(books.get(i));
        savedBookViewHolder.title_view.setText(books.get(i).getTitle_book());
        savedBookViewHolder.author_view.setText("");

        savedBookViewHolder.imageView.setImageDrawable(Drawable.createFromPath(books.get(i).getIcon_book()));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
