package com.example.android.bookfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Noah on 7/31/2016.
 */
public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, ArrayList<Book> books){
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link Book} object located at this position in the list
        Book currentBook = getItem(position);

        // Find the TextView ID book_title
        TextView titleTextView = (TextView)listItemView.findViewById(R.id.book_title);
        // Get title from currentBook object and set this as text on book_title
        titleTextView.setText(currentBook.getTitle());

        // Find the TextView ID book_author
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.book_author);
        // Get author from currentBook object and set this as text on book_author
        authorTextView.setText(currentBook.getAuthor());

        // Find the TextView ID book_author
        ImageView bookImageView = (ImageView) listItemView.findViewById(R.id.book_thumbnail);
        // Get author from currentBook object and set this as text on book_author
        bookImageView.setImageBitmap(currentBook.getThumbnail());

     return listItemView;
    }
}


