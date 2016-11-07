package com.example.android.bookfinder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


// Displays book information
public class ResultsActivity extends AppCompatActivity {

    private BookAdapter bookAdapter;
    private ArrayList<Book> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);

        // Set ArrayList on bookAdapter
        books = new ArrayList<>();
        bookAdapter = new BookAdapter(this, books);
        ListView bookListView = (ListView) findViewById(R.id.list);
        bookListView.setAdapter(bookAdapter);

        // Set click listener to open URl for each item
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book currentBook = bookAdapter.getItem(i);
                Uri bookUri = Uri.parse(currentBook.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);
                startActivity(websiteIntent);
            }
        });

        // Execute BookFinder AsyncTask
        BookFinderTask task = new BookFinderTask();
        task.execute();
    }

    // BookFinder AsyncTask
    private class BookFinderTask extends AsyncTask<String, Void, ArrayList<Book>> {

        // Initialize progress message
        private ProgressDialog dialog = new ProgressDialog(ResultsActivity.this);

        // Retrieve String input from EditText and add to search_url
        String searchInput = getIntent().getStringExtra(MainActivity.EXTRA_MESSAGE);
        String search_url = "https://www.googleapis.com/books/v1/volumes?q=" + searchInput;

        // Get array of book list items from URL
        @Override
        protected ArrayList<Book> doInBackground(String... urls) {
            ArrayList<Book> books = Utils.getBooksData(search_url);
            return books;
        }

        // Show loading message
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Loading...");
            this.dialog.show();
        }

        // Add list of books to bookAdapter
        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            // Dismiss loading dialogue
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            // Set data to bookAdapter
            if (books != null && !books.isEmpty()) {
                bookAdapter.addAll(books);
            }
        }
    }
}