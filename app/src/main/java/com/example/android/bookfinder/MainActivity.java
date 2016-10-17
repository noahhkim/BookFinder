package com.example.android.bookfinder;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.android.bookfinder.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check for Internet connection
        final ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        // Find search button
        Button search = (Button) findViewById(R.id.search_button);

        // Set a click listener on search button
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get string from EditText
                EditText bookTitleField = (EditText) findViewById(R.id.enter_book_title);
                String searchTitle = bookTitleField.getText().toString();

                // If string is empty, open toast message for title input
                if (searchTitle.equals("")) {
                    Toast.makeText(MainActivity.this, "Please enter a valid title", Toast.LENGTH_SHORT).show();
                    return;
                }

                // If network is available, search button directs to ResultsActivity
                if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                    Intent searchIntent = new Intent(MainActivity.this, ResultsActivity.class);
                    searchIntent.putExtra(EXTRA_MESSAGE, searchTitle);
                    startActivity(searchIntent);
                } else {
                    // If there's no network, display "no network" message
                    Toast.makeText(MainActivity.this, "No Internet connection available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

